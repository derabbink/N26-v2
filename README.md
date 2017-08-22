# N26 Code Chalenge
This project uses Java 8 and Gradle. You will need these to compile the project.

This project contains a servlet that hosts a simple RESTful JSON API. For this, I'm mainly using Guice, Jersey, Jackson and Dropwizard's Metrics. Some of this structure was inspired by [this post][1].


## How To Run
### Using Gradle
```
./gradlew webapp:jettyRunWar
```

Due to a quirk in the Gradle Jetty plugin, this will not allow any SLF4J logging to pass to your stdout. All you'll see is Gradle's output.
Additionally, the Jersey endpoint statistics won't be available via JMX.


### Standalone
```
./gradlew webapp:assemble
java -jar jetty-runner.jar webapp/build/libs/webapp-1.0-SNAPSHOT.war
```

For this you need the [Jetty Runner][2] jar.


## Data Structures & Complexity
All transactions are maintained in memory; there is no persistence, or even an in-memory database involved.

In order to accomplish O(1) insertions and O(1) statistics readouts, the project uses `com.abbink.n26.challenge.service.stats.StatsQueue`.
This is a `Queue` implementation based on two stacks: `com.abbink.n26.challenge.service.stats.StatsStack`.

A queue consisting of two stacks (an input stack and output stack) can do O(1) additions, but requires O(n) time for removals, as it might need to move the entire input stack into the output stack first.
This is not a problem, however, since the API endpoints' code paths (`POST /transactions`, `GET /statistics`) never reach the removal method.
The `StatsQueue` receives its statistics information from two `StatsStack`s that it delegates most of the bookkeeping to. Combining the information from these two happens in O(1) time as well.

The `StatsStack` class continuously maintains the `sum`, `avg`, `max`, `min` and `count` upon every addition or removal.
`min` and `max` are maintained by delegating to two parallel stacks that keep track of what the respective `min`/`max` is at each level of the stack. The top of these stacks contain the overall `min`/`max`. This adds some bookkeeping to stack operations, but this overhead takes O(1) time.
A `sum`, `avg` and `count` can be maintained in O(1) time as well, upon addition/removal. This requires just a little bit of arithmetic.

With both insertions and statistics readouts taking O(1) time, there is a price to pay when it comes to flushing out transactions that are older than 1 minute.
In this implementation, that takes O(n) time. The code accounts for the case where transactions might arrive out of order. Therefor, a scheduled task (`com.abbink.n26.challenge.service.TransactionFlushService`) comes along every 200ms and iterates through transactions to find any that are expired.
This is not worse than the `StatsQueue`'s remove operation that this method is based on, as that also takes O(n) time in the worst case, and all removals of every value in the queue combined will also not be worse than O(n).


## Concurrency
All forms of thread safety are handled by the `com.abbink.n26.challenge.service.TransactionService`, which relies on a `ReentrantLock` to both provide exclusive access to the `StatsQueue` underneath, and grant fair access to this exclusive resource to multiple callers.

I am aware that - under heavy load - this may cause starvation for either of these three:

1. The `/transactions` POST endpoint
2. The `/statistics` GET endpoint
3. The `TransactionFlushService` that removes expired transactions

In case of 1. and 2. suffering from starvation, the servlet container is often gracious enough to allow the request to take a while, while it acquires the lock, after which it should complete just fine. If not, that means the request will fail with a timeout or a status 500 (depending on the configuration).
In case of 3. suffering from starvation, the `/statistics` endpoint would show signs of stale data still being maintained by the `StatsQueue`.

## Project Structure
You can think of each nested Gradle project as a Maven module.

* `n26.challenge` wrapper to hold everything together and declare common dependencies
  * `api` API endpoints (Jersey resource classes)
  * `common` Shared logic (e.g. Jersey AOP)
  * `service` Business logic, data "storage"
  * `webapp` Servlet definition, application entry point

## Testing
There are unit tests and integration tests in the `service` module. These tests make sure the business logic is correct.
For manual acceptance testing, I provided a `curl-commands.sh` file that will POST new transactions to `/transactions` every other second, followed by a GET on `/statistics`.
Key details about those request pairs are printed to stdout. Please note that there is a chance that the script might not work on OS X (see inside the file for an explanation).

To check out the long-term performance of the app, you can use e.g. `jconsole` to view some JMX-reported data about the service. Besides familiar data, you'll find some [Metrics][3]-based MBeans containing statistics about the endpoint's performance.

[1]: http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
[2]: https://www.eclipse.org/jetty/documentation/current/runner.html
[3]: http://metrics.dropwizard.io/