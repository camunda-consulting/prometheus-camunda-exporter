FROM docker.consulting.camunda.com/jdk8

COPY target/prometheus-camunda-exporter-springboot.jar .

RUN java -jar ./prometheus-camunda-exporter-springboot.jar