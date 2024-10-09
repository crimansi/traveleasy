package com.skysavvy.traveleasy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories
@SpringBootApplication
public class TraveleasyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraveleasyApplication.class, args);
	}

}
