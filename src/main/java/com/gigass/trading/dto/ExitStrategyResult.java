package com.gigass.trading.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ExitStrategyResult {
    
    private List<BatchExitStep> batchExitPlan;
    private List<String> exitTriggers;
    private LocalDateTime maxHoldingPeriod;
    private ProfitProtectionStrategy profitProtectionStrategy;
    private List<String> emergencyExitPlan;
    private String exitStrategy;
    
    // Getters and Setters
    public List<BatchExitStep> getBatchExitPlan() { return batchExitPlan; }
    public void setBatchExitPlan(List<BatchExitStep> batchExitPlan) { this.batchExitPlan = batchExitPlan; }
    
    public List<String> getExitTriggers() { return exitTriggers; }
    public void setExitTriggers(List<String> exitTriggers) { this.exitTriggers = exitTriggers; }
    
    public LocalDateTime getMaxHoldingPeriod() { return maxHoldingPeriod; }
    public void setMaxHoldingPeriod(LocalDateTime maxHoldingPeriod) { this.maxHoldingPeriod = maxHoldingPeriod; }
    
    public ProfitProtectionStrategy getProfitProtectionStrategy() { return profitProtectionStrategy; }
    public void setProfitProtectionStrategy(ProfitProtectionStrategy profitProtectionStrategy) { this.profitProtectionStrategy = profitProtectionStrategy; }
    
    public List<String> getEmergencyExitPlan() { return emergencyExitPlan; }
    public void setEmergencyExitPlan(List<String> emergencyExitPlan) { this.emergencyExitPlan = emergencyExitPlan; }
    
    public String getExitStrategy() { return exitStrategy; }
    public void setExitStrategy(String exitStrategy) { this.exitStrategy = exitStrategy; }
    
    public static class BatchExitStep {
        private Integer step;
        private Double ratio;
        private String condition;
        
        public BatchExitStep() {}
        
        public BatchExitStep(Integer step, Double ratio, String condition) {
            this.step = step;
            this.ratio = ratio;
            this.condition = condition;
        }
        
        public Integer getStep() { return step; }
        public void setStep(Integer step) { this.step = step; }
        
        public Double getRatio() { return ratio; }
        public void setRatio(Double ratio) { this.ratio = ratio; }
        
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
    }
    
    public static class ProfitProtectionStrategy {
        private Double trailingStopRatio;
        private Boolean partialProfitTaking;
        private String description;
        
        public Double getTrailingStopRatio() { return trailingStopRatio; }
        public void setTrailingStopRatio(Double trailingStopRatio) { this.trailingStopRatio = trailingStopRatio; }
        
        public Boolean getPartialProfitTaking() { return partialProfitTaking; }
        public void setPartialProfitTaking(Boolean partialProfitTaking) { this.partialProfitTaking = partialProfitTaking; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}