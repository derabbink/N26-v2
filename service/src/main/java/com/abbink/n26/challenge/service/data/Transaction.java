package com.abbink.n26.challenge.service.data;

import java.math.BigDecimal;
import java.time.Instant;

public class Transaction {
    private BigDecimal amount;
    private Instant timestamp;

    public Transaction(BigDecimal amount, Instant timestamp) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

}
