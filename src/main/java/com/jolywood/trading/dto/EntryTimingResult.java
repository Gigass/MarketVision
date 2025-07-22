package com.jolywood.trading.dto;

import java.time.LocalDateTime;

public class EntryTimingResult {
    
    private LocalDateTime optimalEntryTime;
    private String entryStrategy;
    private PriceRange priceRange;
    private Double marketTimingScore;
    private String recommendation;
    private String urgencyLevel;
    
    // Getters and Setters
    public LocalDateTime getOptimalEntryTime() { return optimalEntryTime; }
    public void setOptimalEntryTime(LocalDateTime optimalEntryTime) { this.optimalEntryTime = optimalEntryTime; }
    
    public String getEntryStrategy() { return entryStrategy; }
    public void setEntryStrategy(String entryStrategy) { this.entryStrategy = entryStrategy; }
    
    public PriceRange getPriceRange() { return priceRange; }
    public void setPriceRange(PriceRange priceRange) { this.priceRange = priceRange; }
    
    public Double getMarketTimingScore() { return marketTimingScore; }
    public void setMarketTimingScore(Double marketTimingScore) { this.marketTimingScore = marketTimingScore; }
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public String getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    
    public static class PriceRange {
        private Double lowerBound;
        private Double upperBound;
        
        public PriceRange() {}
        
        public PriceRange(Double lowerBound, Double upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
        
        public Double getLowerBound() { return lowerBound; }
        public void setLowerBound(Double lowerBound) { this.lowerBound = lowerBound; }
        
        public Double getUpperBound() { return upperBound; }
        public void setUpperBound(Double upperBound) { this.upperBound = upperBound; }
    }
}