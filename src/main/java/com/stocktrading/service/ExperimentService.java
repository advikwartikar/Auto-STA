package com.stocktrading.service;

import com.stocktrading.model.*;
import com.stocktrading.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class ExperimentService {
    
    @Autowired
    private ExperimentSessionRepository sessionRepository;
    
    @Autowired
    private ExperimentDecisionRepository decisionRepository;
    
    @Autowired
    private ExperimentStockRepository experimentStockRepository;
    
    private static final Double INITIAL_CAPITAL = 100000.0;
    private static final Integer SHARES_PER_TRADE = 10;
    private static final Integer TOTAL_STOCKS = 10;
    private static final Integer DAYS_PER_STOCK = 10;
    private static final long TIME_LIMIT_MINUTES = 150; // 2.5 hours
    
    public ExperimentSession startExperiment(User user) {
        // Check if user already has an incomplete session
        Optional<ExperimentSession> existing = sessionRepository.findByUserAndCompletedFalse(user);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        // Create new session
        ExperimentSession session = new ExperimentSession(user);
        return sessionRepository.save(session);
    }
    
    public ExperimentSession getCurrentSession(User user) {
        return sessionRepository.findByUserAndCompletedFalse(user)
            .orElse(null);
    }
    
    public boolean isSessionExpired(ExperimentSession session) {
        if (session.getStartTime() == null) return false;
        Duration duration = Duration.between(session.getStartTime(), LocalDateTime.now());
        return duration.toMinutes() >= TIME_LIMIT_MINUTES;
    }
    
    public ExperimentStock getCurrentStock(ExperimentSession session) {
        return experimentStockRepository.findBySequenceOrder(session.getCurrentStockIndex())
            .orElse(null);
    }
    
    public Map<String, Object> getCurrentState(ExperimentSession session) {
        Map<String, Object> state = new HashMap<>();
        
        ExperimentStock currentStock = getCurrentStock(session);
        
        state.put("sessionId", session.getId());
        state.put("stockIndex", session.getCurrentStockIndex());
        state.put("dayNumber", session.getCurrentDay());
        state.put("totalStocks", TOTAL_STOCKS);
        state.put("daysPerStock", DAYS_PER_STOCK);
        state.put("currentCapital", session.getCurrentCapital());
        state.put("currentShares", session.getCurrentShares());
        state.put("completed", session.getCompleted());
        state.put("stockSymbol", currentStock != null ? currentStock.getStockSymbol() : null);
        
        // Calculate progress percentage
        int totalDecisions = TOTAL_STOCKS * DAYS_PER_STOCK;
        int currentDecision = (session.getCurrentStockIndex() * DAYS_PER_STOCK) + session.getCurrentDay();
        double progress = (currentDecision * 100.0) / totalDecisions;
        state.put("progressPercent", progress);
        
        // Time remaining
        if (session.getStartTime() != null) {
            Duration elapsed = Duration.between(session.getStartTime(), LocalDateTime.now());
            long remainingMinutes = TIME_LIMIT_MINUTES - elapsed.toMinutes();
            state.put("timeRemainingMinutes", Math.max(0, remainingMinutes));
        }
        
        return state;
    }
    
    public ExperimentDecision makeDecision(ExperimentSession session, String action, Double currentPrice) {
        // Check if session expired
        if (isSessionExpired(session)) {
            session.setCompleted(true);
            session.setEndTime(LocalDateTime.now());
            sessionRepository.save(session);
            throw new RuntimeException("Session time limit exceeded");
        }
        
        // Create decision record
        ExperimentDecision decision = new ExperimentDecision();
        decision.setSession(session);
        decision.setStockIndex(session.getCurrentStockIndex());
        decision.setDayNumber(session.getCurrentDay());
        decision.setAction(action);
        decision.setPrice(currentPrice);
        decision.setCapitalBefore(session.getCurrentCapital());
        decision.setSharesBefore(session.getCurrentShares());
        
        // Process action
        Integer newShares = session.getCurrentShares();
        Double newCapital = session.getCurrentCapital();
        
        switch (action.toUpperCase()) {
            case "BUY":
                Double cost = currentPrice * SHARES_PER_TRADE;
                if (newCapital >= cost) {
                    newCapital -= cost;
                    newShares += SHARES_PER_TRADE;
                    decision.setQuantity(SHARES_PER_TRADE);
                } else {
                    throw new RuntimeException("Insufficient capital to buy");
                }
                break;
                
            case "SELL":
                if (newShares >= SHARES_PER_TRADE) {
                    Double revenue = currentPrice * SHARES_PER_TRADE;
                    newCapital += revenue;
                    newShares -= SHARES_PER_TRADE;
                    decision.setQuantity(SHARES_PER_TRADE);
                } else {
                    throw new RuntimeException("Insufficient shares to sell");
                }
                break;
                
            case "HOLD":
                decision.setQuantity(0);
                break;
                
            default:
                throw new RuntimeException("Invalid action: " + action);
        }
        
        decision.setCapitalAfter(newCapital);
        decision.setSharesAfter(newShares);
        
        // Update session
        session.setCurrentCapital(newCapital);
        session.setCurrentShares(newShares);
        
        // Advance to next day
        session.setCurrentDay(session.getCurrentDay() + 1);
        
        // Check if stock episode completed
        if (session.getCurrentDay() >= DAYS_PER_STOCK) {
            // Liquidate remaining shares at current price
            if (session.getCurrentShares() > 0) {
                Double liquidationValue = session.getCurrentShares() * currentPrice;
                session.setCurrentCapital(session.getCurrentCapital() + liquidationValue);
                session.setCurrentShares(0);
            }
            
            // Move to next stock
            session.setCurrentStockIndex(session.getCurrentStockIndex() + 1);
            session.setCurrentDay(0);
            
            // Check if all stocks completed
            if (session.getCurrentStockIndex() >= TOTAL_STOCKS) {
                session.setCompleted(true);
                session.setEndTime(LocalDateTime.now());
            } else {
                // Reset for next stock
                session.setCurrentCapital(INITIAL_CAPITAL);
                session.setCurrentShares(0);
            }
        }
        
        sessionRepository.save(session);
        decisionRepository.save(decision);
        
        return decision;
    }
    
    public Map<String, Object> getEpisodeSummary(ExperimentSession session, Integer stockIndex) {
        Map<String, Object> summary = new HashMap<>();
        
        List<ExperimentDecision> decisions = decisionRepository.findBySession(session)
            .stream()
            .filter(d -> d.getStockIndex().equals(stockIndex))
            .sorted(Comparator.comparing(ExperimentDecision::getDayNumber))
            .toList();
        
        if (decisions.isEmpty()) {
            return summary;
        }
        
        ExperimentDecision first = decisions.get(0);
        ExperimentDecision last = decisions.get(decisions.size() - 1);
        
        summary.put("stockIndex", stockIndex);
        summary.put("initialCapital", INITIAL_CAPITAL);
        summary.put("finalCapital", last.getCapitalAfter());
        summary.put("returnAmount", last.getCapitalAfter() - INITIAL_CAPITAL);
        summary.put("returnPercent", ((last.getCapitalAfter() - INITIAL_CAPITAL) / INITIAL_CAPITAL) * 100);
        summary.put("totalDecisions", decisions.size());
        
        long buyCount = decisions.stream().filter(d -> "BUY".equals(d.getAction())).count();
        long sellCount = decisions.stream().filter(d -> "SELL".equals(d.getAction())).count();
        long holdCount = decisions.stream().filter(d -> "HOLD".equals(d.getAction())).count();
        
        summary.put("buyCount", buyCount);
        summary.put("sellCount", sellCount);
        summary.put("holdCount", holdCount);
        summary.put("decisions", decisions);
        
        return summary;
    }
    
    public Map<String, Object> getSessionSummary(ExperimentSession session) {
        Map<String, Object> summary = new HashMap<>();
        
        summary.put("sessionId", session.getId());
        summary.put("userId", session.getUser().getId());
        summary.put("username", session.getUser().getUsername());
        summary.put("startTime", session.getStartTime());
        summary.put("endTime", session.getEndTime());
        summary.put("completed", session.getCompleted());
        
        // Get all decisions
        List<ExperimentDecision> allDecisions = decisionRepository
            .findBySessionOrderByStockIndexAscDayNumberAsc(session);
        
        summary.put("totalDecisions", allDecisions.size());
        
        // Calculate statistics
        long totalBuys = allDecisions.stream().filter(d -> "BUY".equals(d.getAction())).count();
        long totalSells = allDecisions.stream().filter(d -> "SELL".equals(d.getAction())).count();
        long totalHolds = allDecisions.stream().filter(d -> "HOLD".equals(d.getAction())).count();
        
        summary.put("totalBuys", totalBuys);
        summary.put("totalSells", totalSells);
        summary.put("totalHolds", totalHolds);
        
        // Calculate per-stock summaries
        List<Map<String, Object>> stockSummaries = new ArrayList<>();
        for (int i = 0; i < session.getCurrentStockIndex(); i++) {
            stockSummaries.add(getEpisodeSummary(session, i));
        }
        summary.put("stockSummaries", stockSummaries);
        
        // Calculate average return
        double avgReturn = stockSummaries.stream()
            .mapToDouble(s -> (Double) s.getOrDefault("returnPercent", 0.0))
            .average()
            .orElse(0.0);
        summary.put("averageReturn", avgReturn);
        
        return summary;
    }
}
