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

For this you need the [Jetty runner jar][2]

[1]: http://blog.palominolabs.com/2011/08/15/a-simple-java-web-stack-with-guice-jetty-jersey-and-jackson/
[2]: https://www.eclipse.org/jetty/documentation/current/runner.html


## Data Structures & Complexity
All transactions are maintained in memory; there is no persistence, or even an in-memory database involved.

In order to accomplish O(1) insertions and O(1) statistics readouts, the project uses `com.abbink.n26.challenge.service.stats.StatsQueue`.
This is a `Queue` implementation based on two stacks: `com.abbink.n26.challenge.service.stats.StatsStack`.

A queue consisting of two stacks (an input stack and output stack) can do O(1) additions, but requires O(n) time for removals, as it might need to move the entire input stack into the output stack first.
This is not a problem, however, since the API endpoints' code paths never reach the removal method.
The `StatsQueue` receives its statistics information from two `StatsStack`s that it delegates most of the bookkeeping to. Combining the information from these two happens in O(1) time as well.

The `StatsStack` class continuously maintains the `sum`, `avg`, `max`, `min` and `count`.
`min` and `max` are maintained by delegating to two parallel stacks that keep track of what the respective `min`/`max` is at each level of the stack. The top of these stacks contain the overall `min`/`max`. This adds some bookkeeping to the add and remove operations, but this overhead takes O(1) time.
A `sum`, `avg` and `count` can be maintained in O(1) time as well, upon addition/removal. This requires just a little bit of arithmetic.

With both insertions and statistics readouts taking O(1) time, there is a price to pay when it comes to flushing out transactions that are older than 1 minute.
In this implementation, that takes O(n) time. The code accounts for the case where transactions might arrive out of order. Therefor, a scheduled task (`com.abbink.n26.challenge.service.TransactionFlushService`) comes along every 200ms and iterates through transactions to find any that are expired.
This is not worse than the `StatsQueue`'s remove operation that this method is based on, as that also takes O(n) time in the worst case, and all removals of every value in the queue combined will also not be worse than O(n).


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
Key details about those request pairs are printed to stdout.

