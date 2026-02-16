package com.stocktrading.service;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VolatilityAnalyzer {
    
    public static class StockVolatility {
        public String symbol;
        public double avgVolatility;
        public List<VolatileWindow> windows;
        
        public StockVolatility(String symbol) {
            this.symbol = symbol;
            this.windows = new ArrayList<>();
        }
    }
    
    public static class VolatileWindow {
        public int startDay;
        public int endDay;
        public double volatility;
        public List<Double> prices;
        
        public VolatileWindow(int start, int end, double vol, List<Double> prices) {
            this.startDay = start;
            this.endDay = end;
            this.volatility = vol;
            this.prices = prices;
        }
    }
    
    public List<StockVolatility> analyzeTop10Stocks() {
        List<StockVolatility> allStocks = new ArrayList<>();
        
        // Analyze all 110 stocks
        for (int i = 1; i <= 110; i++) {
            try {
                StockVolatility sv = analyzeStock("data/stock_" + i + ".csv", "STOCK_" + i);
                if (sv != null && !sv.windows.isEmpty()) {
                    allStocks.add(sv);
                }
            } catch (Exception e) {
                System.err.println("Error analyzing stock " + i + ": " + e.getMessage());
            }
        }
        
        // Sort by average volatility and select top 10
        return allStocks.stream()
            .sorted(Comparator.comparingDouble(s -> -s.avgVolatility))
            .limit(10)
            .collect(Collectors.toList());
    }
    
    private StockVolatility analyzeStock(String csvPath, String symbol) throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream(csvPath);
        if (is == null) {
            return null;
        }
        
        List<Double> closePrices = new ArrayList<>();
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            reader.readNext(); // Skip header
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                if (line.length >= 4) {
                    closePrices.add(Double.parseDouble(line[3])); // Close price
                }
            }
        }
        
        if (closePrices.size() < 10) {
            return null;
        }
        
        StockVolatility sv = new StockVolatility(symbol);
        
        // Calculate volatility for all possible 10-day windows
        List<VolatileWindow> allWindows = new ArrayList<>();
        
        for (int start = 0; start <= closePrices.size() - 10; start++) {
            int end = start + 9; // 10 days inclusive
            List<Double> windowPrices = closePrices.subList(start, end + 1);
            double volatility = calculateVolatility(windowPrices);
            
            allWindows.add(new VolatileWindow(start, end, volatility, windowPrices));
        }
        
        // Sort by volatility and select top 10 non-overlapping windows
        allWindows.sort(Comparator.comparingDouble(w -> -w.volatility));
        
        List<VolatileWindow> selectedWindows = new ArrayList<>();
        for (VolatileWindow window : allWindows) {
            if (selectedWindows.size() >= 10) break;
            
            // Check if this window overlaps with any selected window
            boolean overlaps = selectedWindows.stream()
                .anyMatch(w -> windowsOverlap(w, window));
            
            if (!overlaps) {
                selectedWindows.add(window);
            }
        }
        
        // Sort selected windows by start day
        selectedWindows.sort(Comparator.comparingInt(w -> w.startDay));
        sv.windows = selectedWindows;
        
        // Calculate average volatility
        sv.avgVolatility = selectedWindows.stream()
            .mapToDouble(w -> w.volatility)
            .average()
            .orElse(0.0);
        
        return sv;
    }
    
    private boolean windowsOverlap(VolatileWindow w1, VolatileWindow w2) {
        // Check if windows overlap or are too close (require 5-day gap)
        int gap = 5;
        return !(w1.endDay + gap < w2.startDay || w2.endDay + gap < w1.startDay);
    }
    
    private double calculateVolatility(List<Double> prices) {
        if (prices.size() < 2) return 0.0;
        
        // Calculate daily returns
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < prices.size(); i++) {
            double ret = (prices.get(i) - prices.get(i - 1)) / prices.get(i - 1);
            returns.add(ret);
        }
        
        // Calculate standard deviation of returns
        double mean = returns.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = returns.stream()
            .mapToDouble(r -> Math.pow(r - mean, 2))
            .average()
            .orElse(0.0);
        
        return Math.sqrt(variance);
    }
}
