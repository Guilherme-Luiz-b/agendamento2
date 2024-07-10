package br.chronos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication(scanBasePackages = { "br.chronos", "br.util"})
@EntityScan(basePackages = {"br.chronos.*", "br.util.*"})
public class Agendamento2Application {

	public static void main(String[] args) {
		SpringApplication.run(Agendamento2Application.class, args);
	}

}
