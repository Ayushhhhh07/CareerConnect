
package com.example.placementportal;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentDetailsService implements UserDetailsService {

    private final StudentRepository studentRepository;

    public StudentDetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        String email = username.trim().toLowerCase();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Account not found for email: " + email));

        String role = student.getRole();

        if (!"STUDENT".equals(role) && !"ADMIN".equals(role)) {
            throw new UsernameNotFoundException(
                    "Account has an invalid role.");
        }

        return User.withUsername(student.getEmail())
                .password(student.getPassword())
                .roles(role)
                .build();
    }
}
