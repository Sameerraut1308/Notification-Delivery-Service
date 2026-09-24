package com.sameer.notifyservice;

import com.sameer.notifyservice.model.User;
import com.sameer.notifyservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotifyserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifyserviceApplication.class, args);
	}

	// This runs once after the app starts — just for testing today
	@Bean
	CommandLineRunner testDatabase(UserRepository userRepository) {
		return args -> {
			User testUser = User.builder()
					.name("Sameer Raut")
					.email("sameer@test.com")
					.passwordHash("hashed_password_placeholder")
					.role(User.Role.ADMIN)
					.build();

			User saved = userRepository.save(testUser);
			System.out.println("✅ Test user saved: " + saved);

			// Fetch it back
			User fetched = userRepository.findByEmail("sameer@test.com").orElseThrow();
			System.out.println("✅ Fetched back: " + fetched.getName() + " | Role: " + fetched.getRole());
		};
	}
}