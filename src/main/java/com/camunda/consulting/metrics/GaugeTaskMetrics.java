package com.camunda.consulting.metrics;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;
import io.prometheus.client.Gauge;
import org.camunda.bpm.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GaugeTaskMetrics extends AbstractMetricProvider {

    @Autowired
    TaskService taskService;


    private static final Gauge openTasksByGroup = Gauge.build()
            .name("camunda_open_tasks_group_assignment")
            .help("Number of open tasks by group.")
            .labelNames("candidateGroup")
            .register();

    private static final Gauge openTasks = Gauge.build()
            .name("camunda_open_tasks")
            .help("Number of open tasks.")
            .labelNames("status")
            .register();


    @Override
    public void updateMetrics() {

        taskService.createTaskReport().taskCountByCandidateGroup().forEach(result -> Optional.ofNullable(result.getGroupName())
                .map(taskCountByCandidateGroupResult -> {
                    openTasksByGroup.labels(result.getGroupName()).set(result.getTaskCount());
                    return true;
                })
                .orElseGet(() -> {
                    openTasksByGroup.labels("nogroup").set(result.getTaskCount());
                    return false;
                }));

        openTasks.labels("assigned").set(taskService.createTaskQuery().taskAssigned().count());
        openTasks.labels("unassigned").set(taskService.createTaskQuery().taskUnassigned().count());
        openTasks.labels("hasCandidateGroups").set(taskService.createTaskQuery().withCandidateGroups().count());
        openTasks.labels("hasCandidateUsers").set(taskService.createTaskQuery().withCandidateUsers().count());
        openTasks.labels("unassignedWithNoCandidates").set(taskService.createTaskQuery().taskUnassigned().withoutCandidateGroups().withoutCandidateUsers().count());

        openTasks.labels("total").set(taskService.createTaskQuery().count());


    }
}
