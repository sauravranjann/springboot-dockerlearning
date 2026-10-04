package com.example.dockerproject1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Dockerproject1Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Dockerproject1Application.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("\n==================================================");
		System.out.println("docker project one 1");
		System.out.println("==================================================\n");
	}

	@GetMapping("/")
	public String home() {
		return "docker project one 1";
	}
}
