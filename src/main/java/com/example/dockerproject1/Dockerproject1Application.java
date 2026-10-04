package com.example.dockerproject1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@RestController
public class Dockerproject1Application implements CommandLineRunner {

	@Autowired(required = false)
	private LogRepository logRepository;

	public static void main(String[] args) {
		SpringApplication.run(Dockerproject1Application.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("\n==================================================");
		System.out.println("docker project one 1");
		if (logRepository != null) {
			try {
				LogEntry entry = logRepository.save(new LogEntry("Startup test from Project 1"));
				System.out.println("DB Connection SUCCESS! Inserted record with ID: " + entry.getId());
			} catch (Exception e) {
				System.out.println("DB Connection Note: " + e.getMessage());
			}
		}
		System.out.println("==================================================\n");
	}

	@GetMapping("/")
	public String home() {
		return "docker project one 1";
	}

	@GetMapping("/db-test")
	public List<LogEntry> testDb() {
		if (logRepository != null) {
			logRepository.save(new LogEntry("Web request at " + LocalDateTime.now()));
			return logRepository.findAll();
		}
		return List.of();
	}
}

@Entity
@Table(name = "app_logs")
class LogEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String message;
	private LocalDateTime createdAt;

	public LogEntry() {
		this.createdAt = LocalDateTime.now();
	}

	public LogEntry(String message) {
		this.message = message;
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getMessage() {
		return message;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}

@Repository
interface LogRepository extends JpaRepository<LogEntry, Long> {}
