package com.stocktrading.config;

import com.stocktrading.model.ExperimentStock;
import com.stocktrading.repository.ExperimentStockRepository;
import com.stocktrading.service.VolatilityAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Order(2) // Run after DataLoader
public class ExperimentDataLoader implements CommandLineRunner {
    
    @Autowired
    private VolatilityAnalyzer volatilityAnalyzer;
    
    @Autowired
    private ExperimentStockRepository experimentStockRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (experimentStockRepository.count() == 0) {
            System.out.println("========================================");
            System.out.println("INITIALIZING EXPERIMENT MODE");
            System.out.println("========================================");
            System.out.println("Analyzing 110 stocks for volatility...");
            
            List<VolatilityAnalyzer.StockVolatility> top10 = volatilityAnalyzer.analyzeTop10Stocks();
            
            System.out.println("\nTop 10 Most Volatile Stocks Selected:");
            System.out.println("----------------------------------------");
            
            for (int i = 0; i < top10.size(); i++) {
                VolatilityAnalyzer.StockVolatility sv = top10.get(i);
                
                System.out.printf("Stock %d: %s (Avg Volatility: %.4f)\n", 
                    i + 1, sv.symbol, sv.avgVolatility);
                
                // For each stock, we'll use the first volatile window for the experiment
                // (In a real scenario, you might want to use all 10 windows sequentially)
                if (!sv.windows.isEmpty()) {
                    VolatilityAnalyzer.VolatileWindow window = sv.windows.get(0);
                    
                    ExperimentStock expStock = new ExperimentStock();
                    expStock.setSequenceOrder(i);
                    expStock.setStockSymbol(sv.symbol);
                    expStock.setSegmentStartDay(window.startDay);
                    expStock.setSegmentEndDay(window.endDay);
                    expStock.setCsvFilePath("data/" + sv.symbol.toLowerCase() + ".csv");
                    
                    experimentStockRepository.save(expStock);
                    
                    System.out.printf("  → Days %d-%d (Volatility: %.4f)\n", 
                        window.startDay, window.endDay, window.volatility);
                }
            }
            
            System.out.println("----------------------------------------");
            System.out.println("✓ Experiment mode initialized with 10 stocks");
            System.out.println("✓ Each stock has a 10-day high-volatility segment");
            System.out.println("✓ Total experiment: 10 stocks × 10 days = 100 decisions");
            System.out.println("========================================\n");
        }
    }
}
