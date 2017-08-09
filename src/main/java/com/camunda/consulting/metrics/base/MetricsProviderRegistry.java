package com.camunda.consulting.metrics.base;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 04.04.17.
 */
@Component
public class MetricsProviderRegistry {

    private List<MetricsProvider> metricsProviders = new ArrayList<>();

    public List<MetricsProvider> getMetricsProviders() {
        return metricsProviders;
    }

    public void registerMetricsProvider(MetricsProvider metricsProvider){
        metricsProviders.add(metricsProvider);
    }

    public void unregisterMetricsProvider(MetricsProvider metricsProvider){
        metricsProviders.remove(metricsProvider);
    }
}
