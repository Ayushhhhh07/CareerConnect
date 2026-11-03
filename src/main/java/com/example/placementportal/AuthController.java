
package com.example.placementportal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("student", new Student());
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(
            @ModelAttribute Student student,
            RedirectAttributes redirectAttributes) {

        String email = student.getEmail().trim().toLowerCase();

        if (studentRepository.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute(
                    "error", "This email is already registered.");
            return "redirect:/register";
        }

        student.setEmail(email);
        student.setPassword(
                passwordEncoder.encode(student.getPassword()));

        studentRepository.save(student);

        redirectAttributes.addFlashAttribute(
                "message", "Registration successful! Please log in.");

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            Model model) {

        if (error != null) {
            model.addAttribute(
                    "error", "Invalid email or password.");
        }

        if (logout != null) {
            model.addAttribute(
                    "message", "You have been logged out.");
        }

        return "login";
    }
}
