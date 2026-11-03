
package com.example.placementportal;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordResetHelper {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        System.out.println(
                encoder.encode("Student@12345")
        );
    }
}
