FROM docker.consulting.camunda.com/jdk8

COPY target/prometheus-camunda-exporter-springboot.jar .

ENTRYPOINT java -jar ./prometheus-camunda-exporter-springboot.jar
