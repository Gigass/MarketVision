package com.gigass.trading.dto;

import com.gigass.trading.entity.StockInfo;
import java.util.List;

public class PositionSizeResult {
    
    private String groupType;
    private List<StockInfo> stocks;
    private Double stockPositionSize;
    private Double groupPositionSize;
    private Double weight;
    
    public PositionSizeResult() {}
    
    public PositionSizeResult(String groupType, List<StockInfo> stocks, 
                             Double stockPositionSize, Double groupPositionSize, Double weight) {
        this.groupType = groupType;
        this.stocks = stocks;
        this.stockPositionSize = stockPositionSize;
        this.groupPositionSize = groupPositionSize;
        this.weight = weight;
    }
    
    // Getters and Setters
    public String getGroupType() { return groupType; }
    public void setGroupType(String groupType) { this.groupType = groupType; }
    
    public List<StockInfo> getStocks() { return stocks; }
    public void setStocks(List<StockInfo> stocks) { this.stocks = stocks; }
    
    public Double getStockPositionSize() { return stockPositionSize; }
    public void setStockPositionSize(Double stockPositionSize) { this.stockPositionSize = stockPositionSize; }
    
    public Double getGroupPositionSize() { return groupPositionSize; }
    public void setGroupPositionSize(Double groupPositionSize) { this.groupPositionSize = groupPositionSize; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
}