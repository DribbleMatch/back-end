package com.sideProject.ClutchShot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ClutchShotApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClutchShotApplication.class, args);
	}

}
