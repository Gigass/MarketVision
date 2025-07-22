package com.gigass.trading.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PortfolioAdjustmentResult {
    
    private PortfolioStatus currentPortfolioStatus;
    private Double industryConcentrationRisk;
    private Double correlationRisk;
    private List<String> adjustmentRecommendations;
    private OptimalWeights optimalWeights;
    private String overallRiskLevel;
    
    // Getters and Setters
    public PortfolioStatus getCurrentPortfolioStatus() { return currentPortfolioStatus; }
    public void setCurrentPortfolioStatus(PortfolioStatus currentPortfolioStatus) { this.currentPortfolioStatus = currentPortfolioStatus; }
    
    public Double getIndustryConcentrationRisk() { return industryConcentrationRisk; }
    public void setIndustryConcentrationRisk(Double industryConcentrationRisk) { this.industryConcentrationRisk = industryConcentrationRisk; }
    
    public Double getCorrelationRisk() { return correlationRisk; }
    public void setCorrelationRisk(Double correlationRisk) { this.correlationRisk = correlationRisk; }
    
    public List<String> getAdjustmentRecommendations() { return adjustmentRecommendations; }
    public void setAdjustmentRecommendations(List<String> adjustmentRecommendations) { this.adjustmentRecommendations = adjustmentRecommendations; }
    
    public OptimalWeights getOptimalWeights() { return optimalWeights; }
    public void setOptimalWeights(OptimalWeights optimalWeights) { this.optimalWeights = optimalWeights; }
    
    public String getOverallRiskLevel() { return overallRiskLevel; }
    public void setOverallRiskLevel(String overallRiskLevel) { this.overallRiskLevel = overallRiskLevel; }
    
    public static class PortfolioStatus {
        private Integer totalPositions;
        private Double cashRatio;
        private Double largestPositionRatio;
        private Integer industryCount;
        
        public Integer getTotalPositions() { return totalPositions; }
        public void setTotalPositions(Integer totalPositions) { this.totalPositions = totalPositions; }
        
        public Double getCashRatio() { return cashRatio; }
        public void setCashRatio(Double cashRatio) { this.cashRatio = cashRatio; }
        
        public Double getLargestPositionRatio() { return largestPositionRatio; }
        public void setLargestPositionRatio(Double largestPositionRatio) { this.largestPositionRatio = largestPositionRatio; }
        
        public Integer getIndustryCount() { return industryCount; }
        public void setIndustryCount(Integer industryCount) { this.industryCount = industryCount; }
    }
    
    public static class OptimalWeights {
        private Map<String, Double> groupWeights = new HashMap<>();
        private Double cashWeight;
        
        public void addGroupWeight(String groupType, Double weight) {
            groupWeights.put(groupType, weight);
        }
        
        public Map<String, Double> getGroupWeights() { return groupWeights; }
        public void setGroupWeights(Map<String, Double> groupWeights) { this.groupWeights = groupWeights; }
        
        public Double getCashWeight() { return cashWeight; }
        public void setCashWeight(Double cashWeight) { this.cashWeight = cashWeight; }
    }
}