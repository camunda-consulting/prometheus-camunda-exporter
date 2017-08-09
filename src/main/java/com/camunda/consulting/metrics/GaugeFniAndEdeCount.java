package com.camunda.consulting.metrics;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;
import io.prometheus.client.Gauge;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GaugeFniAndEdeCount extends AbstractMetricProvider {

    @Autowired
    ManagementService managementService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    HistoryService historyService;

    private static final Gauge fniCount = Gauge.build()
            .name("camunda_activity_instances")
            .help("Total number of activity instances (BPMN FNI).")
            .labelNames("processDefinitionId")
            .register();

    private static final Gauge edeCount = Gauge.build()
            .name("camunda_executed_decision_instances")
            .help("Total number of executed decision instances (DMN EDE).")
            .labelNames("decisionDefinitionId")
            .register();

    @Override
    public void updateMetrics() {

        // Get total number of FNIs

        fniCount.labels("total").set(managementService.createMetricsQuery().name("activity-instance-start").sum());

        // Get FNIs by process definition (this only includes the currently deployed definitions and may be lower than the total count)

        List<String> processDefinitionIds = new ArrayList<>();

        repositoryService.createProcessDefinitionQuery().list().forEach(processDefinition -> processDefinitionIds.add(processDefinition.getId()));

        if (processDefinitionIds.size() > 0) {
            processDefinitionIds.forEach(processDefinitionId -> fniCount.labels(processDefinitionId)
                    .set(historyService.createHistoricActivityInstanceQuery()
                            .processDefinitionId(processDefinitionId)
                            .count()
                    )
            );
        }

        // Get total number of EDEs

        edeCount.labels("total").set(managementService.createMetricsQuery().name("executed-decision-elements").sum());

        // Get EDEs by decision definition (this only includes the currently deployed definitions and may be lower than the total count)

        List<String> decisionDefinitionIds = new ArrayList<>();

        repositoryService.createDecisionDefinitionQuery().list().forEach(decisionDefinition -> decisionDefinitionIds.add(decisionDefinition.getId()));

        if (decisionDefinitionIds.size() > 0) {
            decisionDefinitionIds.forEach(decisionDefinitionId -> fniCount.labels(decisionDefinitionId)
                    .set(historyService.createHistoricDecisionInstanceQuery()
                            .decisionDefinitionId(decisionDefinitionId)
                            .count()
                    )
            );
        }


    }
}
