package com.abbink.n26.challenge.service.di;

import com.abbink.n26.challenge.service.TransactionService;
import com.abbink.n26.challenge.service.storage.di.StorageModule;
import com.google.inject.AbstractModule;

public class ServiceModule extends AbstractModule{

    @Override
    protected void configure() {
        install(new StorageModule());
        bind(TransactionService.class);
    }

}
