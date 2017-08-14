FROM docker.consulting.camunda.com/jdk8

COPY target/prometheus-camunda-exporter-springboot.jar .
COPY wait-for-connection.sh .

ENTRYPOINT ./wait-for-connection.sh java -jar ./prometheus-camunda-exporter-springboot.jar
