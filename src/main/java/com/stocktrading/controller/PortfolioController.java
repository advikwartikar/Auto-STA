package com.stocktrading.controller;

import com.stocktrading.model.User;
import com.stocktrading.repository.TransactionRepository;
import com.stocktrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortfolioController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private PortfolioService portfolioService;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @GetMapping("/portfolio")
    public String portfolio(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        model.addAttribute("portfolios", portfolioService.getUserPortfolio(user));
        model.addAttribute("totalValue", portfolioService.getTotalPortfolioValue(user));
        model.addAttribute("totalProfitLoss", portfolioService.getTotalProfitLoss(user));
        model.addAttribute("totalInvested", portfolioService.getTotalInvested(user));
        
        return "portfolio";
    }
    
    @GetMapping("/transactions")
    public String transactions(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("user", user);
        model.addAttribute("transactions", transactionRepository.findByUserOrderByTransactionDateDesc(user));
        
        return "transactions";
    }
}
