package com.gigass.trading.dto;

import java.util.List;

public class ImpactAssessmentResult {
    
    private Long hotSearchId;
    private String title;
    private Double marketSentimentScore;
    private Double fundFlowScore;
    private List<StockGroupResult> relatedStockGroups;
    private Double volatilityScore;
    private Double timeEffectivenessScore;
    private Double reverseVerificationScore;
    private Double totalImpactScore;
    private Boolean hasInvestmentValue;
    private String reason;
    
    public ImpactAssessmentResult() {}
    
    public ImpactAssessmentResult(Long hotSearchId, String title) {
        this.hotSearchId = hotSearchId;
        this.title = title;
    }
    
    // Getters and Setters
    public Long getHotSearchId() { return hotSearchId; }
    public void setHotSearchId(Long hotSearchId) { this.hotSearchId = hotSearchId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Double getMarketSentimentScore() { return marketSentimentScore; }
    public void setMarketSentimentScore(Double marketSentimentScore) { this.marketSentimentScore = marketSentimentScore; }
    
    public Double getFundFlowScore() { return fundFlowScore; }
    public void setFundFlowScore(Double fundFlowScore) { this.fundFlowScore = fundFlowScore; }
    
    public List<StockGroupResult> getRelatedStockGroups() { return relatedStockGroups; }
    public void setRelatedStockGroups(List<StockGroupResult> relatedStockGroups) { this.relatedStockGroups = relatedStockGroups; }
    
    public Double getVolatilityScore() { return volatilityScore; }
    public void setVolatilityScore(Double volatilityScore) { this.volatilityScore = volatilityScore; }
    
    public Double getTimeEffectivenessScore() { return timeEffectivenessScore; }
    public void setTimeEffectivenessScore(Double timeEffectivenessScore) { this.timeEffectivenessScore = timeEffectivenessScore; }
    
    public Double getReverseVerificationScore() { return reverseVerificationScore; }
    public void setReverseVerificationScore(Double reverseVerificationScore) { this.reverseVerificationScore = reverseVerificationScore; }
    
    public Double getTotalImpactScore() { return totalImpactScore; }
    public void setTotalImpactScore(Double totalImpactScore) { this.totalImpactScore = totalImpactScore; }
    
    public Boolean getHasInvestmentValue() { return hasInvestmentValue; }
    public void setHasInvestmentValue(Boolean hasInvestmentValue) { this.hasInvestmentValue = hasInvestmentValue; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}