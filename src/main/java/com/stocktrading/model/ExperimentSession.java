package com.stocktrading.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "experiment_sessions")
public class ExperimentSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private Boolean completed = false;
    
    @Column(name = "current_stock_index")
    private Integer currentStockIndex = 0;
    
    @Column(name = "current_day")
    private Integer currentDay = 0;
    
    @Column(name = "current_capital")
    private Double currentCapital = 100000.0;
    
    @Column(name = "current_shares")
    private Integer currentShares = 0;
    
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<ExperimentDecision> decisions = new ArrayList<>();
    
    public ExperimentSession() {
    }
    
    public ExperimentSession(User user) {
        this.user = user;
        this.startTime = LocalDateTime.now();
        this.currentStockIndex = 0;
        this.currentDay = 0;
        this.currentCapital = 100000.0;
        this.currentShares = 0;
        this.completed = false;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    public Integer getCurrentStockIndex() {
        return currentStockIndex;
    }
    
    public void setCurrentStockIndex(Integer currentStockIndex) {
        this.currentStockIndex = currentStockIndex;
    }
    
    public Integer getCurrentDay() {
        return currentDay;
    }
    
    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }
    
    public Double getCurrentCapital() {
        return currentCapital;
    }
    
    public void setCurrentCapital(Double currentCapital) {
        this.currentCapital = currentCapital;
    }
    
    public Integer getCurrentShares() {
        return currentShares;
    }
    
    public void setCurrentShares(Integer currentShares) {
        this.currentShares = currentShares;
    }
    
    public List<ExperimentDecision> getDecisions() {
        return decisions;
    }
    
    public void setDecisions(List<ExperimentDecision> decisions) {
        this.decisions = decisions;
    }
}
