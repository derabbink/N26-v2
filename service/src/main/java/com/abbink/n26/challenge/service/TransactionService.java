package com.abbink.n26.challenge.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.abbink.n26.challenge.service.data.Statistics;
import com.abbink.n26.challenge.service.data.Transaction;
import com.abbink.n26.challenge.service.stats.StatsQueue;

/**
 * This class takes care of coordinating data reads & writes.
 * It essentially encapsulates the {@link StatsQueue} inside it, adds validation, a {@link #flushOld(Instant)} helper
 * method, and thread safety.
 */
@Singleton
public class TransactionService {
    private ReentrantLock lock;
    private StatsQueue statsQueue;

    @Inject
    public TransactionService(StatsQueue statsQueue) {
        this.lock = new ReentrantLock();
        this.statsQueue = statsQueue;
    }

    public void add(Transaction transaction) {
        lock.lock();
        try {
            // do this inside the lock, because acquiring it might take some time
            if (!isTransactionAllowed(transaction)) {
                throw new TransactionExpiredError();
            }
            statsQueue.add(transaction);
        } finally {
            lock.unlock();
        }
    }

    public boolean isTransactionAllowed(Transaction transaction) {
        return !transaction.getTimestamp().isBefore(Instant.now().minusSeconds(60));
    }

    /**
     * This takes all items out of the {@link #statsQueue}, and only adds unexpired items back in.
     * The performance is O(n), but that's not worse than the overall performance of all calls to
     * {@link StatsQueue#remove()}.
     * @param threshold Everything up to (and including) this timestamp will be removed“
     */
    public void flushOld(Instant threshold) {
        lock.lock();
        try { // Just in case something odd goes wrong
            int count = statsQueue.size();
            for (int i = 0; i < count; i++) {
                // Take everything out and only put the relevant stuff back in
                Transaction transaction = statsQueue.remove();
                if (transaction.getTimestamp().isAfter(threshold)) {
                    statsQueue.add(transaction);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public Statistics getStatistics() {
        lock.lock();
        try { // Just in case the conversion throws
            return new Statistics(
                    statsQueue.getAvg(),
                    statsQueue.getSize(),
                    statsQueue.getMax(),
                    statsQueue.getMin(),
                    statsQueue.getSum());
        } finally {
            lock.unlock();
        }
    }

    public final class TransactionExpiredError extends RuntimeException {
    }
}
