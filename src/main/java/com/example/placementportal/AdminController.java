
package com.example.placementportal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {

    private final JobApplicationRepository applicationRepository;

    private static final int PAGE_SIZE = 5;

    public AdminController(
            JobApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {

        List<JobApplication> applications =
                applicationRepository.findAll();

        model.addAttribute("totalApplications", applications.size());

        model.addAttribute("pendingApplications",
                applications.stream()
                        .filter(app -> "Pending".equals(app.getStatus()))
                        .count());

        model.addAttribute("underReviewApplications",
                applications.stream()
                        .filter(app -> "Under Review".equals(app.getStatus()))
                        .count());

        model.addAttribute("shortlistedApplications",
                applications.stream()
                        .filter(app -> "Shortlisted".equals(app.getStatus()))
                        .count());

        model.addAttribute("rejectedApplications",
                applications.stream()
                        .filter(app -> "Rejected".equals(app.getStatus()))
                        .count());

        return "admin-dashboard";
    }

    @GetMapping("/admin/applications")
    public String viewApplications(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String internship,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        List<JobApplication> allApplications =
                applicationRepository.findAll();

        Stream<JobApplication> filtered = allApplications.stream();

        if (email != null && !email.isBlank()) {
            String emailQuery = email.trim().toLowerCase();

            filtered = filtered.filter(app ->
                    app.getEmail() != null
                    && app.getEmail().toLowerCase().contains(emailQuery));
        }

        if (internship != null && !internship.isBlank()) {
            String internshipQuery = internship.trim().toLowerCase();

            filtered = filtered.filter(app ->
                    app.getInternshipTitle() != null
                    && app.getInternshipTitle()
                            .toLowerCase().contains(internshipQuery));
        }

        if (status != null && !status.isBlank()) {
            if (List.of("Pending", "Under Review",
                    "Shortlisted", "Rejected").contains(status)) {

                filtered = filtered.filter(app ->
                        status.equals(app.getStatus()));
            } else {
                filtered = filtered.filter(app -> false);
            }
        }

        List<JobApplication> matchingApplications =
                filtered.collect(Collectors.toList());

        int totalApplications = matchingApplications.size();

        int totalPages = Math.max(
                1, (int) Math.ceil(totalApplications / (double) PAGE_SIZE));

        // Keep the requested page within valid bounds.
        int currentPage = Math.max(0, Math.min(page, totalPages - 1));

        int startIndex = currentPage * PAGE_SIZE;
        int endIndex = Math.min(
                startIndex + PAGE_SIZE, totalApplications);

        List<JobApplication> applications =
                matchingApplications.subList(startIndex, endIndex);

        model.addAttribute("applications", applications);
        model.addAttribute("emailFilter", email == null ? "" : email);
        model.addAttribute(
                "internshipFilter", internship == null ? "" : internship);
        model.addAttribute("statusFilter", status == null ? "" : status);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalMatchingApplications", totalApplications);
        model.addAttribute("hasPrevious", currentPage > 0);
        model.addAttribute("hasNext", currentPage < totalPages - 1);

        return "admin-applications";
    }

    @PostMapping("/admin/applications/{id}/status")
    public String updateApplicationStatus(
            @PathVariable Long id,
            @RequestParam String status,
            RedirectAttributes redirectAttributes) {

        if (status == null
                || !(status.equals("Pending")
                || status.equals("Under Review")
                || status.equals("Shortlisted")
                || status.equals("Rejected"))) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Invalid application status.");

            return "redirect:/admin/applications";
        }

        return applicationRepository.findById(id)
                .map(application -> {
                    application.setStatus(status);
                    applicationRepository.save(application);

                    redirectAttributes.addFlashAttribute(
                            "successMessage",
                            "Application status updated successfully.");

                    return "redirect:/admin/applications";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute(
                            "errorMessage", "Application not found.");

                    return "redirect:/admin/applications";
                });
    }
}
