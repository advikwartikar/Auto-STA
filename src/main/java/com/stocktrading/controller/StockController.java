package com.stocktrading.controller;

import com.stocktrading.model.Stock;
import com.stocktrading.model.User;
import com.stocktrading.service.StockService;
import com.stocktrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stocks")
public class StockController {
    
    @Autowired
    private StockService stockService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listStocks(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("stocks", stockService.getAllActiveStocks());
        model.addAttribute("user", user);
        return "stocks";
    }
    
    @GetMapping("/{id}")
    public String stockDetails(@PathVariable Long id, Model model, Authentication auth) {
        Stock stock = stockService.getStockById(id)
            .orElseThrow(() -> new RuntimeException("Stock not found"));
        
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        model.addAttribute("stock", stock);
        model.addAttribute("user", user);
        return "stock-detail";
    }
}
