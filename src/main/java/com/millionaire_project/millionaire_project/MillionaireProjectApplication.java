package com.millionaire_project.millionaire_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class MillionaireProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MillionaireProjectApplication.class, args);
	}
	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Phnom_Penh"));
	}

}
