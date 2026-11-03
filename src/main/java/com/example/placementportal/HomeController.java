
package com.example.placementportal;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final InternshipRepository internshipRepository;
    private final JobApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;

    public HomeController(
            InternshipRepository internshipRepository,
            JobApplicationRepository applicationRepository,
            StudentRepository studentRepository) {

        this.internshipRepository = internshipRepository;
        this.applicationRepository = applicationRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/")
    public String home(Model model) {

        List<Internship> internships = internshipRepository.findAll();

        model.addAttribute("internships", internships);

        return "index";
    }

    // NEW: Internship Details Page
    @GetMapping("/internship-details")
    public String internshipDetails(
            @RequestParam("title") String title,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Internship> internship = internshipRepository.findAll()
                .stream()
                .filter(item -> item.getTitle().equals(title))
                .findFirst();

        if (internship.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Internship not found.");

            return "redirect:/";
        }

        model.addAttribute("internship", internship.get());

        return "internship-details";
    }

    @GetMapping("/apply")
    public String showApplicationForm(
            @RequestParam("title") String title,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        Optional<Internship> internship = internshipRepository.findAll()
                .stream()
                .filter(item -> item.getTitle().equals(title))
                .findFirst();

        if (internship.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Internship not found.");

            return "redirect:/";
        }

        model.addAttribute("application", new JobApplication());
        model.addAttribute("internshipTitle", internship.get().getTitle());
        model.addAttribute("studentEmail", authentication.getName());

        return "apply";
    }

    @PostMapping("/apply")
    public String submitApplication(
            @ModelAttribute("application") JobApplication formApplication,
            @RequestParam("internshipTitle") String title,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Optional<Internship> internship = internshipRepository.findAll()
                .stream()
                .filter(item -> item.getTitle().equals(title))
                .findFirst();

        if (internship.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage", "Invalid internship selected.");

            return "redirect:/";
        }

        String email = authentication.getName().trim().toLowerCase();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Logged-in student not found."));

        JobApplication application = new JobApplication();

        application.setApplicantName(student.getFullName());
        application.setEmail(student.getEmail());
        application.setPhone(formApplication.getPhone());
        application.setInternshipTitle(internship.get().getTitle());
        application.setStatus("Pending");
        application.setStudent(student);

        applicationRepository.save(application);

        redirectAttributes.addFlashAttribute(
                "message", "Application submitted successfully!");

        return "redirect:/success";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }
}
