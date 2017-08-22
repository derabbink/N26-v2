package com.abbink.n26.challenge.api.http;

import javax.annotation.Nonnull;

import com.abbink.n26.challenge.service.data.Transaction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * A container type specifically designed to pass back-and-forth
 * between API clients in JSON format.
 */
@JsonInclude(Include.NON_NULL)
public class JsonTransaction {
    private double amount;
    private long timestamp;

    @JsonCreator
    public JsonTransaction(
            @JsonProperty("amount") double amount,
            @JsonProperty("timestamp") long timestamp
    ) {
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @JsonGetter
    public double getAmount() {
        return amount;
    }

    @JsonGetter
    public long getTimestamp() {
        return timestamp;
    }

    public Transaction toTransaction() {
        return new Transaction(
                BigDecimal.valueOf(getAmount()),
                Instant.ofEpochMilli(getTimestamp()));
    }
}
