package com.stocktrading.config;

import com.stocktrading.model.Stock;
import com.stocktrading.model.User;
import com.stocktrading.repository.StockRepository;
import com.stocktrading.repository.UserRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

@Component
@Order(1)
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("STARTING DATA INITIALIZATION");
        System.out.println("========================================\n");
        
        // Create users
        if (userRepository.count() == 0) {
            System.out.println("Creating users...");
            
            try {
                // Create admin users
                User admin1 = new User("admin1", passwordEncoder.encode("admin123"), 
                                      "Admin One", "admin1@trading.com", "ADMIN");
                admin1.setCredits(100000.0);
                userRepository.save(admin1);
                
                User admin2 = new User("admin2", passwordEncoder.encode("admin123"), 
                                      "Admin Two", "admin2@trading.com", "ADMIN");
                admin2.setCredits(100000.0);
                userRepository.save(admin2);
                
                System.out.println("✓ Created 2 admin users (admin1, admin2)");
                
                // Create 30 regular users
                for (int i = 1; i <= 30; i++) {
                    User user = new User(
                        "user" + i,
                        passwordEncoder.encode("user123"),
                        "User " + i,
                        "user" + i + "@trading.com",
                        "USER"
                    );
                    user.setCredits(100000.0);
                    userRepository.save(user);
                }
                
                System.out.println("✓ Created 30 regular users (user1-user30)");
                System.out.println("✓ Total users created: " + userRepository.count());
            } catch (Exception e) {
                System.err.println("❌ ERROR creating users: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("✓ Users already exist: " + userRepository.count() + " users found");
        }
        
        // Create stocks
        if (stockRepository.count() == 0) {
            System.out.println("\nLoading stocks from CSV files...");
            int successCount = 0;
            int failCount = 0;
            
            for (int i = 1; i <= 110; i++) {
                try {
                    String csvPath = "data/stock_" + i + ".csv";
                    InputStream is = getClass().getClassLoader().getResourceAsStream(csvPath);
                    
                    if (is != null) {
                        CSVReader reader = new CSVReader(new InputStreamReader(is));
                        reader.readNext(); // Skip header
                        
                        String[] firstLine = reader.readNext();
                        if (firstLine != null && firstLine.length >= 4) {
                            double openPrice = Double.parseDouble(firstLine[0]);
                            double highPrice = Double.parseDouble(firstLine[1]);
                            double lowPrice = Double.parseDouble(firstLine[2]);
                            double closePrice = Double.parseDouble(firstLine[3]);
                            
                            // Create stock using the 3-parameter constructor
                            Stock stock = new Stock("STOCK_" + i, "Stock " + i, closePrice);
                            
                            // Set additional fields using setters
                            stock.setOpeningPrice(openPrice);
                            stock.setHighPrice(highPrice);
                            stock.setLowPrice(lowPrice);
                            stock.setVolume(0L);
                            stock.setChangePercent(0.0);
                            stock.setLastUpdated(LocalDateTime.now());
                            stock.setActive(true);
                            
                            stockRepository.save(stock);
                            successCount++;
                            
                            if (successCount % 20 == 0) {
                                System.out.println("  Loaded " + successCount + " stocks...");
                            }
                        }
                        
                        reader.close();
                    } else {
                        failCount++;
                        if (failCount <= 5) {
                            System.err.println("  ⚠ CSV file not found: " + csvPath);
                        }
                    }
                } catch (Exception e) {
                    failCount++;
                    if (failCount <= 5) {
                        System.err.println("  ❌ Error loading stock " + i + ": " + e.getMessage());
                    }
                }
            }
            
            System.out.println("\n✓ Stocks loaded successfully: " + successCount);
            if (failCount > 0) {
                System.out.println("⚠ Failed to load: " + failCount + " stocks");
            }
            System.out.println("✓ Total stocks in database: " + stockRepository.count());
        } else {
            System.out.println("✓ Stocks already exist: " + stockRepository.count() + " stocks found");
        }
        
        System.out.println("\n========================================");
        System.out.println("DATA INITIALIZATION COMPLETE");
        System.out.println("Users: " + userRepository.count());
        System.out.println("Stocks: " + stockRepository.count());
        System.out.println("========================================\n");
    }
}
