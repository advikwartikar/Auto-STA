package com.stocktrading.service;

import com.stocktrading.model.*;
import com.stocktrading.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class TradingService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    public Transaction buyStock(User user, Stock stock, Integer quantity) {
        Double totalCost = stock.getCurrentPrice() * quantity;
        
        if (user.getCredits() < totalCost) {
            throw new RuntimeException("Insufficient credits");
        }
        
        // Deduct credits
        user.setCredits(user.getCredits() - totalCost);
        userRepository.save(user);
        
        // Create transaction
        Transaction transaction = new Transaction(user, stock, "BUY", quantity, stock.getCurrentPrice());
        transactionRepository.save(transaction);
        
        // Update portfolio
        Portfolio portfolio = portfolioRepository.findByUserAndStock(user, stock)
            .orElse(new Portfolio(user, stock, 0, 0.0));
        
        Double newAverageBuyPrice = ((portfolio.getQuantity() * portfolio.getAverageBuyPrice()) + totalCost) 
                                    / (portfolio.getQuantity() + quantity);
        portfolio.setQuantity(portfolio.getQuantity() + quantity);
        portfolio.setAverageBuyPrice(newAverageBuyPrice);
        portfolio.setLastUpdated(LocalDateTime.now());
        portfolioRepository.save(portfolio);
        
        return transaction;
    }
    
    public Transaction sellStock(User user, Stock stock, Integer quantity) {
        Portfolio portfolio = portfolioRepository.findByUserAndStock(user, stock)
            .orElseThrow(() -> new RuntimeException("Stock not in portfolio"));
        
        if (portfolio.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock quantity");
        }
        
        Double totalRevenue = stock.getCurrentPrice() * quantity;
        
        // Add credits
        user.setCredits(user.getCredits() + totalRevenue);
        userRepository.save(user);
        
        // Create transaction
        Transaction transaction = new Transaction(user, stock, "SELL", quantity, stock.getCurrentPrice());
        transactionRepository.save(transaction);
        
        // Update portfolio
        portfolio.setQuantity(portfolio.getQuantity() - quantity);
        
        if (portfolio.getQuantity() == 0) {
            portfolioRepository.delete(portfolio);
        } else {
            portfolio.setLastUpdated(LocalDateTime.now());
            portfolioRepository.save(portfolio);
        }
        
        return transaction;
    }
}
