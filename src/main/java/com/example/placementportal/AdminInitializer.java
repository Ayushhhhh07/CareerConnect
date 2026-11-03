
package com.example.placementportal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner createAdminAccount(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            String adminEmail =
                    System.getenv("CAREERCONNECT_ADMIN_EMAIL");

            String adminPassword =
                    System.getenv("CAREERCONNECT_ADMIN_PASSWORD");

            if (adminEmail == null || adminEmail.isBlank()
                    || adminPassword == null || adminPassword.isBlank()) {
                System.out.println(
                        "Admin account not created. Configure "
                        + "CAREERCONNECT_ADMIN_EMAIL and "
                        + "CAREERCONNECT_ADMIN_PASSWORD.");
                return;
            }

            String email = adminEmail.trim().toLowerCase();

            if (studentRepository.existsByEmail(email)) {
                System.out.println(
                        "An account with the configured admin email "
                        + "already exists. No account changes made.");
                return;
            }

            Student admin = new Student();
            admin.setFullName("CareerConnect Administrator");
            admin.setEmail(email);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole("ADMIN");

            studentRepository.save(admin);

            System.out.println("Initial admin account created.");
        };
    }
}
