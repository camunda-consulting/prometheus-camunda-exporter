package com.camunda.consulting.metrics;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;
import io.prometheus.client.Counter;
import org.springframework.stereotype.Component;

@Component
public class CounterExample extends AbstractMetricProvider {

    private static final Counter counter = Counter.build()
            .name("requests_total")
            .help("Total requests.")
            .register();

    @Override
    public void updateMetrics() {

        counter.inc();

    }
}
