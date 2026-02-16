package com.stocktrading.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "experiment_decisions")
public class ExperimentDecision {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "session_id", nullable = false)
    private ExperimentSession session;
    
    @Column(name = "stock_index", nullable = false)
    private Integer stockIndex;
    
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;
    
    @Column(nullable = false)
    private String action;
    
    @Column(nullable = false)
    private Double price;
    
    private Integer quantity;
    
    @Column(name = "capital_before")
    private Double capitalBefore;
    
    @Column(name = "capital_after")
    private Double capitalAfter;
    
    @Column(name = "shares_before")
    private Integer sharesBefore;
    
    @Column(name = "shares_after")
    private Integer sharesAfter;
    
    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
    
    public ExperimentDecision() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public ExperimentSession getSession() {
        return session;
    }
    
    public void setSession(ExperimentSession session) {
        this.session = session;
    }
    
    public Integer getStockIndex() {
        return stockIndex;
    }
    
    public void setStockIndex(Integer stockIndex) {
        this.stockIndex = stockIndex;
    }
    
    public Integer getDayNumber() {
        return dayNumber;
    }
    
    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Double getCapitalBefore() {
        return capitalBefore;
    }
    
    public void setCapitalBefore(Double capitalBefore) {
        this.capitalBefore = capitalBefore;
    }
    
    public Double getCapitalAfter() {
        return capitalAfter;
    }
    
    public void setCapitalAfter(Double capitalAfter) {
        this.capitalAfter = capitalAfter;
    }
    
    public Integer getSharesBefore() {
        return sharesBefore;
    }
    
    public void setSharesBefore(Integer sharesBefore) {
        this.sharesBefore = sharesBefore;
    }
    
    public Integer getSharesAfter() {
        return sharesAfter;
    }
    
    public void setSharesAfter(Integer sharesAfter) {
        this.sharesAfter = sharesAfter;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
