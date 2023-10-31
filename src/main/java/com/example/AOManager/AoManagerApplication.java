package com.example.AOManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AoManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AoManagerApplication.class, args);
	}

}
