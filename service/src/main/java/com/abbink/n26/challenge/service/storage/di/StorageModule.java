package com.abbink.n26.challenge.service.storage.di;

import com.abbink.n26.challenge.service.storage.TransactionStore;
import com.abbink.n26.challenge.service.storage.TransactionStoreImpl;
import com.abbink.n26.challenge.service.storage.TypeStore;
import com.abbink.n26.challenge.service.storage.TypeStoreImpl;
import com.google.inject.AbstractModule;

public class StorageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TransactionStore.class).to(TransactionStoreImpl.class);
        bind(TypeStore.class).to(TypeStoreImpl.class);
    }

}
