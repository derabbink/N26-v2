package com.abbink.n26.challenge.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.Timer;

/**
 * This service flushes out old transactions.
 * It is invoked by a timer, and then re-schedules itself every time.
 */
@Singleton
public class TransactionFlushService {
    public static final long INTERVAL_MILLIS = 200;

    private Timer timer;
    private TransactionService transactionService;

    @Inject
    public TransactionFlushService(Timer timer, TransactionService transactionService) {
        this.timer = timer;
        this.transactionService = transactionService;
    }

    public void reScheduleSelf(Instant lastRun) {
        long remainingInterval = Math.min(
                Duration.between(lastRun, Instant.now()).toMillis(),
                INTERVAL_MILLIS);
        remainingInterval = Math.max(remainingInterval, 10L); // leave a >=10ms gap, so the computer can take a breath
        timer.schedule(new TimerTask(this), remainingInterval);
    }

    public void flushOld() {
        Instant now = Instant.now();
        Instant threshold = now.minusSeconds(60);
        transactionService.flushOld(threshold);
        reScheduleSelf(now);
    }

    /**
     * Glue code for {@link Timer} that doesn't cause me to pollute the interface of {@link TransactionFlushService}
     * with a non-descriptive {@link #run()} method.
     */
    private static class TimerTask extends java.util.TimerTask {
        private TransactionFlushService flushService;

        public TimerTask(TransactionFlushService flushService) {
            this.flushService = flushService;
        }

        @Override
        public void run() {
            flushService.flushOld();
        }
    }
}
