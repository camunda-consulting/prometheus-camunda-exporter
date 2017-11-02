### Intro
This is a sample spring-boot application that serves prometheus metrics for Camunda BPM. It
includes an embedded engine that connects to an existing Camunda database (same fashion as the standalone webapp).

*Heads up:* If your database contains very large amounts of data, some of the queries may be slow.

This archtitecture is not considered best practice to serve prometheus metrics in a production environment. 
It is a good starting point to get something running and might be fine if the performance and load
works for you. 
As an alternative you might create a Camunda process engine plugin instead, that registers listeners to update
the metrics on the fly - and the REST API just return them. This way at least some of the metrics are far
easier to resolve, but it requires some more serious thoughts on e.g. clustering. 

### Configure it

The exporter currently only supports PostgreSQL as a database. If you need to connect to any other 
database provider, you'll have to add the JDBC driver and build the project from source.

In any case, you need to specify the following database parameters either as environment variables or 
in the application.properties file:

  - DB_URL
  - DB_USERNAME
  - DB_PASSWORD
  
### Run it

You can run the application as an executable jar or with docker. Check out the provided `Dockerfile` and `docker-compose.yml
 if you are interested in this option.
 
### Extend it

Each metric is implemented by a component that extends the class `AbstractMetricsProvider`. 
You can easily provide additional metrics by following the same pattern.

### Provided metrics

Tasks:
- camunda_open_tasks: Number of open tasks in total and by status.
- camunda_open_tasks_group_assignment: Number of open tasks by group.

FNI / EDE: 

- camunda_activity_instances: Number of activity instances (BPMN FNI) in total and by deployed process definition.
- camunda_executed_decision_instances:  Total number of executed decision instances (DMN EDE).

Jobs and Incidents:
- camunda_jobs_future:  Number of jobs where the due date is in the future
- camunda_jobs_out_of_retries: Number of jobs with no retries left
- camunda_jobs_executable: Number of jobs which are executable, ie. retries > 0 and due date is null or due date is in the past
- camunda_jobs_suspended: Number of suspended jobs
- camunda_incidents_open: Number of open incidents.


Process definitions and instances:
- camunda_deployed_process_definitions: Number of deployed process definitions.
- camunda_deployed_process_definitions_unique:  Number of deployed process definitions, ignoring different versions of the same definition.
- camunda_process_instance_duration_count: Finished process instances by process definition key - count.
- camunda_process_instance_duration_sum: Running process instances by process definition key - sum of durations.

General metrics:
- camunda_prometheus_metrics_update: Requests for prometheus metrics count and duration in seconds
- requests_total: Total requests (serves as a sample counter).
