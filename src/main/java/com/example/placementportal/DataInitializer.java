
package com.example.placementportal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadInternships(InternshipRepository repository) {
        return args -> {
            if (repository.count() == 0) {

                repository.save(new Internship(
                    "Java Developer Intern",
                    "Tech Solutions",
                    "Java, Spring Boot",
                    "Mumbai",
                    "₹10,000/month"
                ));

                repository.save(new Internship(
                    "Web Developer Intern",
                    "WebWorks",
                    "HTML, CSS, JavaScript",
                    "Navi Mumbai",
                    "₹8,000/month"
                ));

                repository.save(new Internship(
                    "AI/ML Intern",
                    "AI Innovations",
                    "Python, Machine Learning",
                    "Remote",
                    "₹12,000/month"
                ));

                repository.save(new Internship(
                    "Data Analyst Intern",
                    "Data Insights",
                    "Python, SQL, Excel",
                    "Thane",
                    "₹9,000/month"
                ));

                System.out.println("Sample internships inserted successfully!");
            }
        };
    }
}
