package com.abbink.n26.challenge.api.http.transactionservice.di;

import com.abbink.n26.challenge.api.http.transactionservice.StatisticsResource;
import com.abbink.n26.challenge.api.http.transactionservice.TransactionResource;
import com.google.inject.AbstractModule;

public class TransactionServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransactionResource.class);
        bind(StatisticsResource.class);
    }
}
