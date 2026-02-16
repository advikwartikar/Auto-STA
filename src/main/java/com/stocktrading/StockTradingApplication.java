package com.stocktrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockTradingApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(StockTradingApplication.class, args);
        System.out.println("\n=========================================");
        System.out.println("Stock Trading Simulation Started!");
        System.out.println("Access at: http://localhost:8080");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("=========================================\n");
    }
}
