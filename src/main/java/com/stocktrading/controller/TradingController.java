package com.stocktrading.controller;

import com.stocktrading.model.Stock;
import com.stocktrading.model.User;
import com.stocktrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/trade")
public class TradingController {
    
    @Autowired
    private TradingService tradingService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private StockService stockService;
    
    @PostMapping("/buy")
    public String buyStock(@RequestParam Long stockId, 
                          @RequestParam Integer quantity,
                          Authentication auth,
                          RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Stock stock = stockService.getStockById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
            
            tradingService.buyStock(user, stock, quantity);
            
            redirectAttributes.addFlashAttribute("success", 
                "Successfully purchased " + quantity + " shares of " + stock.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/portfolio";
    }
    
    @PostMapping("/sell")
    public String sellStock(@RequestParam Long stockId,
                           @RequestParam Integer quantity,
                           Authentication auth,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Stock stock = stockService.getStockById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
            
            tradingService.sellStock(user, stock, quantity);
            
            redirectAttributes.addFlashAttribute("success",
                "Successfully sold " + quantity + " shares of " + stock.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/portfolio";
    }
}
