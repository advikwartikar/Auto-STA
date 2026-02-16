package com.stocktrading.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
public class Stock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "current_price", nullable = false)
    private Double currentPrice;
    
    @Column(name = "opening_price")
    private Double openingPrice;
    
    @Column(name = "high_price")
    private Double highPrice;
    
    @Column(name = "low_price")
    private Double lowPrice;
    
    @Column
    private Long volume;
    
    @Column(name = "change_percent")
    private Double changePercent;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(nullable = false)
    private Boolean active = true;
    
    public Stock() {
    }
    
    public Stock(String symbol, String name, Double currentPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.openingPrice = currentPrice;
        this.highPrice = currentPrice;
        this.lowPrice = currentPrice;
        this.volume = 0L;
        this.changePercent = 0.0;
        this.lastUpdated = LocalDateTime.now();
        this.active = true;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Double getCurrentPrice() {
        return currentPrice;
    }
    
    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public Double getOpeningPrice() {
        return openingPrice;
    }
    
    public void setOpeningPrice(Double openingPrice) {
        this.openingPrice = openingPrice;
    }
    
    public Double getHighPrice() {
        return highPrice;
    }
    
    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }
    
    public Double getLowPrice() {
        return lowPrice;
    }
    
    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }
    
    public Long getVolume() {
        return volume;
    }
    
    public void setVolume(Long volume) {
        this.volume = volume;
    }
    
    public Double getChangePercent() {
        return changePercent;
    }
    
    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }
}
