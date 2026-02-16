package com.stocktrading.controller;

import com.stocktrading.model.User;
import com.stocktrading.repository.TransactionRepository;
import com.stocktrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PortfolioService portfolioService;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @GetMapping("/dashboard")
    public String adminDashboard(Model model, Authentication auth) {
        User admin = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("admin", admin);
        model.addAttribute("totalUsers", userService.getAllRegularUsers().size());
        model.addAttribute("activeUsers", userService.getAllRegularUsers().stream()
            .filter(u -> u.getActive()).count());
        model.addAttribute("totalTransactions", transactionRepository.count());
        
        return "admin/dashboard";
    }
    
    @GetMapping("/users")
    public String listUsers(Model model, Authentication auth) {
        User admin = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("admin", admin);
        model.addAttribute("users", userService.getAllRegularUsers());
        
        return "admin/users";
    }
    
    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable Long id, Model model, Authentication auth) {
        User admin = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        User user = userService.getUserById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("admin", admin);
        model.addAttribute("viewUser", user);
        model.addAttribute("portfolios", portfolioService.getUserPortfolio(user));
        model.addAttribute("transactions", transactionRepository.findByUserOrderByTransactionDateDesc(user));
        model.addAttribute("totalValue", portfolioService.getTotalPortfolioValue(user));
        model.addAttribute("totalProfitLoss", portfolioService.getTotalProfitLoss(user));
        model.addAttribute("totalInvested", portfolioService.getTotalInvested(user));
        
        return "admin/user-detail";
    }
    
    @PostMapping("/user/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            if ("ADMIN".equals(user.getRole())) {
                redirectAttributes.addFlashAttribute("error", "Cannot delete admin users");
                return "redirect:/admin/users";
            }
            
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User '" + user.getUsername() + "' deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @PostMapping("/user/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            user.setActive(!user.getActive());
            userService.updateUser(user);
            
            String status = user.getActive() ? "activated" : "deactivated";
            redirectAttributes.addFlashAttribute("success", "User '" + user.getUsername() + "' " + status + " successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating user status: " + e.getMessage());
        }
        
        return "redirect:/admin/users";
    }
    
    @GetMapping("/experiments")
    public String viewExperiments(Model model, Authentication auth) {
        User admin = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("admin", admin);
        // This will be implemented to show all experiment results
        
        return "admin/experiments";
    }
}
