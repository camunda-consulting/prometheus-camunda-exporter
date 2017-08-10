package com.camunda.consulting.metrics;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;
import io.prometheus.client.Gauge;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GaugesProcessDefinitionAndInstanceCounts extends AbstractMetricProvider {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    RepositoryService repositoryService;

    private static final Gauge processDefinitionCount = Gauge.build()
            .name("camunda_deployed_process_definitions")
            .help("Number of deployed process definitions.")
            .register();

    private static final Gauge processDefinitionCountUnique = Gauge.build()
            .name("camunda_deployed_process_definitions_unique")
            .help("Number of deployed process definitions, ignoring different versions of the same definition.")
            .register();

    private static final Gauge processInstanceCount = Gauge.build()
            .name("camunda_running_process_instances")
            .help("Running process instances by process definition key.")
            .labelNames("processDefinitionKey")
            .register();

    @Override
    public void updateMetrics() {

        List<String> processDefinitionKeys = new ArrayList<>();

        repositoryService.createProcessDefinitionQuery().list().forEach(processDefinition -> processDefinitionKeys.add(processDefinition.getKey()));

        processDefinitionCount.set(processDefinitionKeys.size());
        processDefinitionCountUnique.set(processDefinitionKeys.stream().distinct().count());

        if (processDefinitionKeys.size() > 0) {
            processDefinitionKeys.forEach(processDefinitionKey -> processInstanceCount.labels(processDefinitionKey)
                    .set(runtimeService.createProcessInstanceQuery()
                            .processDefinitionKey(processDefinitionKey)
                            .count()
                    )
            );
        } else {
            processInstanceCount.labels("NA").set(0);
        }


    }
}
