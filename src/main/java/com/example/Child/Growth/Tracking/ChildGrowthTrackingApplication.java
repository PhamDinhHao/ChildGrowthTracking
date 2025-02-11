package com.example.Child.Growth.Tracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.example.Child.Growth.Tracking")
@EnableJpaRepositories("com.example.Child.Growth.Tracking")
@EnableScheduling
public class ChildGrowthTrackingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChildGrowthTrackingApplication.class, args);
	}

}
