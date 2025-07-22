package com.jolywood.trading.dto;

import java.util.List;

public class StockCorrelationResult {
    
    private Long hotSearchId;
    private String stockCode;
    private String stockName;
    private Double correlationScore;
    private List<String> matchedConcepts;
    private String industry;
    private Double historicalPerformance;
    private Double riskScore;
    private Boolean isLeaderStock;
    
    public StockCorrelationResult() {}
    
    public StockCorrelationResult(Long hotSearchId, String stockCode, String stockName) {
        this.hotSearchId = hotSearchId;
        this.stockCode = stockCode;
        this.stockName = stockName;
    }
    
    // TODO: 添加完整的getter/setter方法
    
    public Long getHotSearchId() { return hotSearchId; }
    public void setHotSearchId(Long hotSearchId) { this.hotSearchId = hotSearchId; }
    
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    
    public String getStockName() { return stockName; }
    public void setStockName(String stockName) { this.stockName = stockName; }
    
    public Double getCorrelationScore() { return correlationScore; }
    public void setCorrelationScore(Double correlationScore) { this.correlationScore = correlationScore; }
    
    public List<String> getMatchedConcepts() { return matchedConcepts; }
    public void setMatchedConcepts(List<String> matchedConcepts) { this.matchedConcepts = matchedConcepts; }
    
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    
    public Double getHistoricalPerformance() { return historicalPerformance; }
    public void setHistoricalPerformance(Double historicalPerformance) { this.historicalPerformance = historicalPerformance; }
    
    public Double getRiskScore() { return riskScore; }
    public void setRiskScore(Double riskScore) { this.riskScore = riskScore; }
    
    public Boolean getIsLeaderStock() { return isLeaderStock; }
    public void setIsLeaderStock(Boolean isLeaderStock) { this.isLeaderStock = isLeaderStock; }
}