package com.stocktrading.controller;

import com.stocktrading.model.*;
import com.stocktrading.service.*;
import com.stocktrading.repository.ExperimentStockRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Controller
@RequestMapping("/experiment")
public class ExperimentController {
    
    @Autowired
    private ExperimentService experimentService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ExperimentStockRepository experimentStockRepository;
    
    @GetMapping("/start")
    public String startExperiment(Authentication auth, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            ExperimentSession session = experimentService.startExperiment(user);
            return "redirect:/experiment/trade";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/dashboard";
        }
    }
    
    @GetMapping("/trade")
    public String tradingInterface(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        ExperimentSession session = experimentService.getCurrentSession(user);
        
        if (session == null) {
            return "redirect:/experiment/start";
        }
        
        if (session.getCompleted()) {
            return "redirect:/experiment/summary";
        }
        
        if (experimentService.isSessionExpired(session)) {
            session.setCompleted(true);
            return "redirect:/experiment/summary";
        }
        
        Map<String, Object> state = experimentService.getCurrentState(session);
        ExperimentStock currentStock = experimentService.getCurrentStock(session);
        
        if (currentStock == null) {
            redirectAttributes.addFlashAttribute("error", "Experiment configuration error");
            return "redirect:/dashboard";
        }
        
        // Load current day's data
        try {
            List<Map<String, Object>> stockData = loadStockData(currentStock, session.getCurrentDay());
            model.addAttribute("stockData", stockData);
            model.addAttribute("currentDayData", stockData.isEmpty() ? null : stockData.get(stockData.size() - 1));
        } catch (Exception e) {
            model.addAttribute("stockData", new ArrayList<>());
            model.addAttribute("currentDayData", null);
        }
        
        model.addAttribute("state", state);
        model.addAttribute("session", session);
        model.addAttribute("stock", currentStock);
        model.addAttribute("user", user);
        
        return "experiment-trade";
    }
    
    @PostMapping("/decide")
    public String makeDecision(@RequestParam String action,
                              Authentication auth,
                              RedirectAttributes redirectAttributes) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        ExperimentSession session = experimentService.getCurrentSession(user);
        
        if (session == null || session.getCompleted()) {
            return "redirect:/experiment/summary";
        }
        
        try {
            ExperimentStock currentStock = experimentService.getCurrentStock(session);
            
            // Get current price
            List<Map<String, Object>> stockData = loadStockData(currentStock, session.getCurrentDay());
            if (stockData.isEmpty()) {
                throw new RuntimeException("No stock data available");
            }
            
            Double currentPrice = (Double) stockData.get(stockData.size() - 1).get("close");
            
            // Check if this is the last day of current stock
            boolean isLastDayOfStock = session.getCurrentDay() == 9;
            boolean isLastStock = session.getCurrentStockIndex() == 9;
            
            // Make decision
            experimentService.makeDecision(session, action.toUpperCase(), currentPrice);
            
            // Check if episode completed
            if (isLastDayOfStock) {
                if (isLastStock) {
                    // Experiment complete
                    redirectAttributes.addFlashAttribute("success", "Experiment completed!");
                    return "redirect:/experiment/summary";
                } else {
                    // Episode complete, show summary
                    redirectAttributes.addFlashAttribute("completedStock", session.getCurrentStockIndex() - 1);
                    return "redirect:/experiment/episode-summary";
                }
            }
            
            redirectAttributes.addFlashAttribute("success", "Decision recorded: " + action);
            return "redirect:/experiment/trade";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/experiment/trade";
        }
    }
    
    @GetMapping("/episode-summary")
    public String episodeSummary(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        ExperimentSession session = experimentService.getCurrentSession(user);
        
        if (session == null) {
            return "redirect:/experiment/start";
        }
        
        // Get summary of previous stock (current index - 1)
        Integer completedStockIndex = session.getCurrentStockIndex() - 1;
        Map<String, Object> summary = experimentService.getEpisodeSummary(session, completedStockIndex);
        
        ExperimentStock completedStock = experimentStockRepository.findBySequenceOrder(completedStockIndex)
            .orElse(null);
        
        model.addAttribute("summary", summary);
        model.addAttribute("stock", completedStock);
        model.addAttribute("session", session);
        model.addAttribute("user", user);
        model.addAttribute("nextStockIndex", session.getCurrentStockIndex() + 1);
        
        return "experiment-episode-summary";
    }
    
    @GetMapping("/summary")
    public String finalSummary(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        ExperimentSession session = experimentService.getCurrentSession(user);
        
        if (session == null) {
            return "redirect:/dashboard";
        }
        
        Map<String, Object> summary = experimentService.getSessionSummary(session);
        
        model.addAttribute("summary", summary);
        model.addAttribute("session", session);
        model.addAttribute("user", user);
        
        return "experiment-summary";
    }
    
    private List<Map<String, Object>> loadStockData(ExperimentStock stock, Integer currentDay) throws Exception {
        List<Map<String, Object>> data = new ArrayList<>();
        
        String csvPath = "data/" + stock.getStockSymbol().toLowerCase() + ".csv";
        InputStream is = getClass().getClassLoader().getResourceAsStream(csvPath);
        
        if (is == null) {
            return data;
        }
        
        try (CSVReader reader = new CSVReader(new InputStreamReader(is))) {
            reader.readNext(); // Skip header
            
            List<String[]> allRows = new ArrayList<>();
            String[] line;
            while ((line = reader.readNext()) != null) {
                allRows.add(line);
            }
            
            // Extract only the segment data up to current day
            int startDay = stock.getSegmentStartDay();
            int endDay = Math.min(startDay + currentDay, stock.getSegmentEndDay());
            
            for (int i = startDay; i <= endDay && i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                if (row.length >= 7) {
                    Map<String, Object> dayData = new HashMap<>();
                    dayData.put("day", i - startDay);
                    dayData.put("open", Double.parseDouble(row[0]));
                    dayData.put("high", Double.parseDouble(row[1]));
                    dayData.put("low", Double.parseDouble(row[2]));
                    dayData.put("close", Double.parseDouble(row[3]));
                    dayData.put("volume", Long.parseLong(row[4]));
                    dayData.put("sma", Double.parseDouble(row[5]));
                    dayData.put("rsi", Double.parseDouble(row[6]));
                    data.add(dayData);
                }
            }
        }
        
        return data;
    }
}
