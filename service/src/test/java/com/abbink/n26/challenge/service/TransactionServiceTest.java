package com.abbink.n26.challenge.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import com.abbink.n26.challenge.service.data.Transaction;
import com.abbink.n26.challenge.service.stats.StatsQueue;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * This is mostly an integration test that makes sure only non-expired values remain in the StatsQueue
 */
public class TransactionServiceTest {
    private StatsQueue statsQueue;
    private TransactionService service;

    @Before
    public void before() {
        statsQueue = spy(new StatsQueue());
        service = spy(new TransactionService(statsQueue));
    }

    @Test
    public void orderDoesNotAffectFlush() {
        verifyZeroInteractions(service, statsQueue);
        doReturn(true).when(service).isTransactionAllowed(any(Transaction.class));

        long[] timestamps = new long[] {11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int i = 0; i < timestamps.length; i++) {
            Transaction transaction = new Transaction(BigDecimal.TEN, Instant.ofEpochMilli(timestamps[i]));
            service.add(transaction);
        }

        assertThat(statsQueue.size(), equalTo(timestamps.length));

        service.flushOld(Instant.ofEpochMilli(10));

        assertThat(statsQueue.size(), equalTo(timestamps.length / 2));
    }
}
