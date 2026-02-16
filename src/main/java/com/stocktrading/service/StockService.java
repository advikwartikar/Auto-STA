package com.stocktrading.service;

import com.stocktrading.model.Stock;
import com.stocktrading.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StockService {
    
    @Autowired
    private StockRepository stockRepository;
    
    public List<Stock> getAllActiveStocks() {
        return stockRepository.findByActiveTrue();
    }
    
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    
    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }
    
    public Optional<Stock> getStockBySymbol(String symbol) {
        return stockRepository.findBySymbol(symbol);
    }
    
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }
}
