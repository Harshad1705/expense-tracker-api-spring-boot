package com.harrySpringBoot.expensetrackerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// to build  - mvnw clean install
// to run    - mvnw spring-boot:run

@SpringBootApplication
public class ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);
		System.out.println("Started");
	} 

}
