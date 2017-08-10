package com.camunda.consulting.metrics;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;
import io.prometheus.client.Gauge;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GaugeIncidentCounts extends AbstractMetricProvider{

    @Autowired
    RuntimeService runtimeService;

    private static final Gauge openIncidents = Gauge.build()
            .name("camunda_incidents_open")
            .help("Number of open incidents.")
            .register();


    @Override
    public void updateMetrics() {

        openIncidents.set(runtimeService.createIncidentQuery().count());

    }
}
