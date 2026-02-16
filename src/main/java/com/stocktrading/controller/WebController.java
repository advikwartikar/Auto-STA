package com.stocktrading.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login?logout";
    }
    
    @PostMapping("/logout")
    public String logoutPost(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/login?logout";
    }
}
