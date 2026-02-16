package com.stocktrading.service;

import com.stocktrading.model.Portfolio;
import com.stocktrading.model.User;
import com.stocktrading.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PortfolioService {
    
    @Autowired
    private PortfolioRepository portfolioRepository;
    
    public List<Portfolio> getUserPortfolio(User user) {
        return portfolioRepository.findByUser(user);
    }
    
    public Double getTotalPortfolioValue(User user) {
        List<Portfolio> portfolios = getUserPortfolio(user);
        return portfolios.stream()
            .mapToDouble(Portfolio::getCurrentValue)
            .sum();
    }
    
    public Double getTotalProfitLoss(User user) {
        List<Portfolio> portfolios = getUserPortfolio(user);
        return portfolios.stream()
            .mapToDouble(Portfolio::getProfitLoss)
            .sum();
    }
    
    public Double getTotalInvested(User user) {
        List<Portfolio> portfolios = getUserPortfolio(user);
        return portfolios.stream()
            .mapToDouble(Portfolio::getInvestedAmount)
            .sum();
    }
}
