package com.camunda.consulting.metrics.base;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public abstract class AbstractMetricProvider implements MetricsProvider {


    @Autowired
    private MetricsProviderRegistry metricsProviderRegistry;

    @PostConstruct
    private void register(){
        metricsProviderRegistry.registerMetricsProvider(this);
    }

    @PreDestroy
    private void unregister(){
        metricsProviderRegistry.unregisterMetricsProvider(this);
    }


}
