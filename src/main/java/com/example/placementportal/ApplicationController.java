
package com.example.placementportal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ApplicationController {

    private final StudentRepository studentRepository;
    private final JobApplicationRepository applicationRepository;

    public ApplicationController(
            StudentRepository studentRepository,
            JobApplicationRepository applicationRepository) {
        this.studentRepository = studentRepository;
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/my-applications")
    public String myApplications(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName()
                .trim()
                .toLowerCase();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Logged-in student not found."));

        List<JobApplication> applications =
                applicationRepository.findByStudentId(student.getId());

        List<String> notifications = new ArrayList<>();

        for (JobApplication application : applications) {
            String status = application.getStatus();

            if (status != null && !status.equalsIgnoreCase("Pending")) {
                String message;

                switch (status) {
                    case "Under Review":
                        message = "Your application for "
                                + application.getInternshipTitle()
                                + " is under review.";
                        break;

                    case "Shortlisted":
                        message = "Congratulations! Your application for "
                                + application.getInternshipTitle()
                                + " has been shortlisted.";
                        break;

                    case "Rejected":
                        message = "Your application for "
                                + application.getInternshipTitle()
                                + " was not selected.";
                        break;

                    default:
                        message = "Your application for "
                                + application.getInternshipTitle()
                                + " has a status update: "
                                + status + ".";
                        break;
                }

                notifications.add(message);
            }
        }

        model.addAttribute("applications", applications);
        model.addAttribute("studentName", student.getFullName());
        model.addAttribute("notifications", notifications);

        return "my-applications";
    }
}
