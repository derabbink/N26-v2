package com.abbink.n26.challenge.common.metrics.timing;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.ext.Provider;

import com.codahale.metrics.MetricRegistry;
import com.sun.jersey.spi.container.ResourceMethodDispatchAdapter;
import com.sun.jersey.spi.container.ResourceMethodDispatchProvider;

@Singleton
@Provider
public class TimingResourceMethodDispatchAdapter implements ResourceMethodDispatchAdapter {
    private final MetricRegistry metricRegistry;

    @Inject
    public TimingResourceMethodDispatchAdapter(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    public ResourceMethodDispatchProvider adapt(ResourceMethodDispatchProvider provider) {
        return new TimingResourceMethodDispatchProvider(metricRegistry, provider);
    }
}
