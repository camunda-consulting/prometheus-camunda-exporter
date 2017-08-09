package com.camunda.consulting;

import io.prometheus.client.spring.web.EnablePrometheusTiming;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnablePrometheusTiming
@SpringBootApplication
public class PrometheusCamundaExporterApplication {

	public static void main(String[] args) {

		SpringApplication.run(PrometheusCamundaExporterApplication.class, args);

	}


}
