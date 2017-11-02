package com.camunda.consulting.metrics;

import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;

import io.prometheus.client.Summary;

/**
 * Simple metric to export durations as {@link Summary} to calculate durations.
 * 
 * Hint: this implementation is very inefficient, as it queries all finished instances
 * from the database every time. A much better way would be to collect the metrics during
 * engine runtime (e.g. by implementing a {@link ProcessEnginePlugin} using a 
 * {@link ExecutionListener} on process instance end events).
 */
@Component
public class HistogramProcessInstanceDurations extends AbstractMetricProvider{

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    HistoryService historyService;

    private static final Summary processInstanceDurations = Summary.build()
            .name("camunda_process_instance_duration")
            .help("Duration of finished process instances in millis.")
            .labelNames("processDefinitionKey")
            .register();


    @Override
    public void updateMetrics() {
      List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
      if (processDefinitions.size() > 0) {
        processDefinitions.forEach(pd -> {
          
          List<HistoricProcessInstance> finishedInstances = historyService.createHistoricProcessInstanceQuery() //
            .processDefinitionKey(pd.getKey()) //
            .finished() //
            .list();
          
          finishedInstances.forEach( pi ->
            processInstanceDurations.labels(pd.getKey()) //
              .observe(pi.getDurationInMillis()));
        });
      } else {
        processInstanceDurations.labels("NA").observe(0);
      }
    }
}
