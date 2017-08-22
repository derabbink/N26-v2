package com.abbink.n26.challenge.service.di;

import com.abbink.n26.challenge.service.TransactionService;
import com.abbink.n26.challenge.service.stats.di.StatsModule;
import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule{

    @Override
    protected void configure() {
        install(new StatsModule());
        bind(TransactionService.class);
    }

}
