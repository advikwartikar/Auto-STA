package com.stocktrading.controller;

import com.stocktrading.model.User;
import com.stocktrading.model.ExperimentSession;
import com.stocktrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ExperimentService experimentService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        
        // Check if user has an active or completed experiment session
        ExperimentSession session = experimentService.getCurrentSession(user);
        
        if (session != null) {
            if (session.getCompleted()) {
                // Session completed, show results
                model.addAttribute("experimentCompleted", true);
                return "dashboard";
            } else {
                // Session in progress, redirect to experiment
                return "redirect:/experiment/trade";
            }
        }
        
        // No session exists, show start experiment option
        model.addAttribute("canStartExperiment", true);
        return "dashboard";
    }
}
