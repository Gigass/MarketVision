package com.jolywood.trading.dto;

import java.util.List;

public class BacktestResult {
    
    private Integer historicalCaseCount;
    private List<SimulationResult> simulationResults;
    private PerformanceMetrics performanceMetrics;
    private RiskMetrics riskMetrics;
    private String validationConclusion;
    private Double successRate;
    private Double averageReturn;
    private Double maxDrawdown;
    
    // Getters and Setters
    public Integer getHistoricalCaseCount() { return historicalCaseCount; }
    public void setHistoricalCaseCount(Integer historicalCaseCount) { this.historicalCaseCount = historicalCaseCount; }
    
    public List<SimulationResult> getSimulationResults() { return simulationResults; }
    public void setSimulationResults(List<SimulationResult> simulationResults) { this.simulationResults = simulationResults; }
    
    public PerformanceMetrics getPerformanceMetrics() { return performanceMetrics; }
    public void setPerformanceMetrics(PerformanceMetrics performanceMetrics) { this.performanceMetrics = performanceMetrics; }
    
    public RiskMetrics getRiskMetrics() { return riskMetrics; }
    public void setRiskMetrics(RiskMetrics riskMetrics) { this.riskMetrics = riskMetrics; }
    
    public String getValidationConclusion() { return validationConclusion; }
    public void setValidationConclusion(String validationConclusion) { this.validationConclusion = validationConclusion; }
    
    public Double getSuccessRate() { return successRate; }
    public void setSuccessRate(Double successRate) { this.successRate = successRate; }
    
    public Double getAverageReturn() { return averageReturn; }
    public void setAverageReturn(Double averageReturn) { this.averageReturn = averageReturn; }
    
    public Double getMaxDrawdown() { return maxDrawdown; }
    public void setMaxDrawdown(Double maxDrawdown) { this.maxDrawdown = maxDrawdown; }
    
    public static class SimulationResult {
        private Long caseId;
        private String caseTitle;
        private Double simulatedReturn;
        private Boolean success;
        private Double maxDrawdown;
        private Integer holdingDays;
        
        public Long getCaseId() { return caseId; }
        public void setCaseId(Long caseId) { this.caseId = caseId; }
        
        public String getCaseTitle() { return caseTitle; }
        public void setCaseTitle(String caseTitle) { this.caseTitle = caseTitle; }
        
        public Double getSimulatedReturn() { return simulatedReturn; }
        public void setSimulatedReturn(Double simulatedReturn) { this.simulatedReturn = simulatedReturn; }
        
        public Boolean isSuccess() { return success; }
        public void setSuccess(Boolean success) { this.success = success; }
        
        public Double getMaxDrawdown() { return maxDrawdown; }
        public void setMaxDrawdown(Double maxDrawdown) { this.maxDrawdown = maxDrawdown; }
        
        public Integer getHoldingDays() { return holdingDays; }
        public void setHoldingDays(Integer holdingDays) { this.holdingDays = holdingDays; }
    }
    
    public static class PerformanceMetrics {
        private Double successRate;
        private Double averageReturn;
        private Double maxReturn;
        private Double maxLoss;
        
        public Double getSuccessRate() { return successRate; }
        public void setSuccessRate(Double successRate) { this.successRate = successRate; }
        
        public Double getAverageReturn() { return averageReturn; }
        public void setAverageReturn(Double averageReturn) { this.averageReturn = averageReturn; }
        
        public Double getMaxReturn() { return maxReturn; }
        public void setMaxReturn(Double maxReturn) { this.maxReturn = maxReturn; }
        
        public Double getMaxLoss() { return maxLoss; }
        public void setMaxLoss(Double maxLoss) { this.maxLoss = maxLoss; }
    }
    
    public static class RiskMetrics {
        private Double volatility;
        private Double maxDrawdown;
        private Double sharpeRatio;
        
        public Double getVolatility() { return volatility; }
        public void setVolatility(Double volatility) { this.volatility = volatility; }
        
        public Double getMaxDrawdown() { return maxDrawdown; }
        public void setMaxDrawdown(Double maxDrawdown) { this.maxDrawdown = maxDrawdown; }
        
        public Double getSharpeRatio() { return sharpeRatio; }
        public void setSharpeRatio(Double sharpeRatio) { this.sharpeRatio = sharpeRatio; }
    }
}