package com.abbink.n26.challenge.webapp;

import javax.servlet.ServletContextEvent;

import com.abbink.n26.challenge.service.TransactionFlushService;
import com.abbink.n26.challenge.webapp.di.N26Module;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import java.time.Instant;

public class N26ServletContextListener extends GuiceServletContextListener {

    private Injector injector;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        JmxReporter reporter = JmxReporter.forRegistry(injector.getInstance(MetricRegistry.class)).build();
        reporter.start();

        injector.getInstance(TransactionFlushService.class).reScheduleSelf(Instant.now());
    }

    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new N26Module());
        return injector;
    }
}
