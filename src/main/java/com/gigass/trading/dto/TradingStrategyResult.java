package com.gigass.trading.dto;

import java.util.List;

public class TradingStrategyResult {
    
    private Long recordId;
    private String title;
    private String recommendation;
    private String reason;
    private Double confidenceLevel;
    
    private List<PositionSizeResult> positionSizes;
    private EntryTimingResult entryTiming;
    private RiskControlResult riskControls;
    private ExitStrategyResult exitStrategy;
    private PortfolioAdjustmentResult portfolioAdjustment;
    private BacktestResult backtestResult;
    
    public TradingStrategyResult() {}
    
    public TradingStrategyResult(Long recordId, String title) {
        this.recordId = recordId;
        this.title = title;
    }
    
    // Getters and Setters
    public Long getRecordId() { return recordId; }
    public void setRecordId(Long recordId) { this.recordId = recordId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public Double getConfidenceLevel() { return confidenceLevel; }
    public void setConfidenceLevel(Double confidenceLevel) { this.confidenceLevel = confidenceLevel; }
    
    public List<PositionSizeResult> getPositionSizes() { return positionSizes; }
    public void setPositionSizes(List<PositionSizeResult> positionSizes) { this.positionSizes = positionSizes; }
    
    public EntryTimingResult getEntryTiming() { return entryTiming; }
    public void setEntryTiming(EntryTimingResult entryTiming) { this.entryTiming = entryTiming; }
    
    public RiskControlResult getRiskControls() { return riskControls; }
    public void setRiskControls(RiskControlResult riskControls) { this.riskControls = riskControls; }
    
    public ExitStrategyResult getExitStrategy() { return exitStrategy; }
    public void setExitStrategy(ExitStrategyResult exitStrategy) { this.exitStrategy = exitStrategy; }
    
    public PortfolioAdjustmentResult getPortfolioAdjustment() { return portfolioAdjustment; }
    public void setPortfolioAdjustment(PortfolioAdjustmentResult portfolioAdjustment) { this.portfolioAdjustment = portfolioAdjustment; }
    
    public BacktestResult getBacktestResult() { return backtestResult; }
    public void setBacktestResult(BacktestResult backtestResult) { this.backtestResult = backtestResult; }
}