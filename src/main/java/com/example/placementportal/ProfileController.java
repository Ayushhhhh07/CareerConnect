
package com.example.placementportal;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final StudentRepository studentRepository;

    public ProfileController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping("/profile")
    public String viewProfile(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName()
                .trim()
                .toLowerCase();

        Optional<Student> studentOptional =
                studentRepository.findByEmail(email);

        if (studentOptional.isEmpty()) {
            return "redirect:/login?error";
        }

        model.addAttribute("student", studentOptional.get());

        return "profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(
            Authentication authentication,
            Model model) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName()
                .trim()
                .toLowerCase();

        Optional<Student> studentOptional =
                studentRepository.findByEmail(email);

        if (studentOptional.isEmpty()) {
            return "redirect:/login?error";
        }

        model.addAttribute("student", studentOptional.get());

        return "edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(
            Authentication authentication,
            @ModelAttribute("student") Student formStudent,
            RedirectAttributes redirectAttributes) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = authentication.getName()
                .trim()
                .toLowerCase();

        Optional<Student> studentOptional =
                studentRepository.findByEmail(email);

        if (studentOptional.isEmpty()) {
            return "redirect:/login?error";
        }

        Student existingStudent = studentOptional.get();

        String fullName = formStudent.getFullName();

        if (fullName == null
                || fullName.trim().isEmpty()
                || fullName.trim().length() > 100) {

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Name is required and must not exceed 100 characters.");

            return "redirect:/profile/edit";
        }

        existingStudent.setFullName(fullName.trim());

        studentRepository.save(existingStudent);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Your profile has been updated successfully.");

        return "redirect:/profile";
    }
}
