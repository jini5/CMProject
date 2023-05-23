package com.example.cmproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CmProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmProjectApplication.class, args);
	}

}
