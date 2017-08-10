package com.camunda.consulting.metrics;

import com.camunda.consulting.metrics.base.AbstractMetricProvider;
import io.prometheus.client.Gauge;
import org.camunda.bpm.engine.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class GaugesJobCounts extends AbstractMetricProvider {

    @Autowired
    ManagementService managementService;


    private static final Gauge jobsExecutable = Gauge.build()
            .name("camunda_jobs_executable")
            .help("Number of jobs which are executable, ie. retries > 0 and due date is null or due date is in the past")
            .register();

    private static final Gauge jobsFuture = Gauge.build()
            .name("camunda_jobs_future")
            .help("Number of jobs where the due date is in the future")
            .register();

    private static final Gauge jobsNoRetries = Gauge.build()
            .name("camunda_jobs_out_of_retries")
            .help("Number of jobs with no retries left")
            .register();

    private static final Gauge jobsSuspended = Gauge.build()
            .name("camunda_jobs_suspended")
            .help("Number of suspended jobs")
            .register();


    @Override
    public void updateMetrics() {

        jobsExecutable.set(managementService.createJobQuery().executable().count());

        jobsFuture.set(managementService.createJobQuery().duedateHigherThan(new Date()).count());

        jobsNoRetries.set(managementService.createJobQuery().noRetriesLeft().count());

        jobsSuspended.set(managementService.createJobQuery().suspended().count());

    }
}
