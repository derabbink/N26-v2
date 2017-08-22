package com.abbink.n26.challenge.service.stats.di;

import com.abbink.n26.challenge.service.data.Statistics;
import com.abbink.n26.challenge.service.stats.Stats;
import com.abbink.n26.challenge.service.stats.StatsQueue;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class StatsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(StatsQueue.class);
    }
}
