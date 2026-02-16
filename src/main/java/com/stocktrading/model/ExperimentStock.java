package com.stocktrading.model;

import jakarta.persistence.*;

@Entity
@Table(name = "experiment_stocks")
public class ExperimentStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;
    
    @Column(name = "stock_symbol", nullable = false)
    private String stockSymbol;
    
    @Column(name = "segment_start_day", nullable = false)
    private Integer segmentStartDay;
    
    @Column(name = "segment_end_day", nullable = false)
    private Integer segmentEndDay;
    
    @Column(name = "csv_file_path")
    private String csvFilePath;
    
    public ExperimentStock() {
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getSequenceOrder() {
        return sequenceOrder;
    }
    
    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }
    
    public String getStockSymbol() {
        return stockSymbol;
    }
    
    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }
    
    public Integer getSegmentStartDay() {
        return segmentStartDay;
    }
    
    public void setSegmentStartDay(Integer segmentStartDay) {
        this.segmentStartDay = segmentStartDay;
    }
    
    public Integer getSegmentEndDay() {
        return segmentEndDay;
    }
    
    public void setSegmentEndDay(Integer segmentEndDay) {
        this.segmentEndDay = segmentEndDay;
    }
    
    public String getCsvFilePath() {
        return csvFilePath;
    }
    
    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
}
