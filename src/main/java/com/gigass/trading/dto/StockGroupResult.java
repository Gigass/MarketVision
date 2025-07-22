package com.gigass.trading.dto;

import com.gigass.trading.entity.StockInfo;
import java.util.List;

public class StockGroupResult {
    
    private String groupType; // 群组类型：直接相关、概念相关、行业联动、供应链相关
    private List<StockInfo> stocks;
    private Double relevanceScore; // 相关度分数
    private Integer stockCount;
    private String description;
    
    public StockGroupResult() {}
    
    public StockGroupResult(String groupType, List<StockInfo> stocks, Double relevanceScore) {
        this.groupType = groupType;
        this.stocks = stocks;
        this.relevanceScore = relevanceScore;
        this.stockCount = stocks != null ? stocks.size() : 0;
    }
    
    // Getters and Setters
    public String getGroupType() { return groupType; }
    public void setGroupType(String groupType) { this.groupType = groupType; }
    
    public List<StockInfo> getStocks() { return stocks; }
    public void setStocks(List<StockInfo> stocks) { 
        this.stocks = stocks; 
        this.stockCount = stocks != null ? stocks.size() : 0;
    }
    
    public Double getRelevanceScore() { return relevanceScore; }
    public void setRelevanceScore(Double relevanceScore) { this.relevanceScore = relevanceScore; }
    
    public Integer getStockCount() { return stockCount; }
    public void setStockCount(Integer stockCount) { this.stockCount = stockCount; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}