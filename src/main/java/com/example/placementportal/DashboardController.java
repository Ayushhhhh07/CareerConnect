
package com.example.placementportal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(
            Authentication authentication,
            Model model) {

        model.addAttribute(
                "studentEmail",
                authentication.getName()
        );

        return "dashboard";
    }
}
