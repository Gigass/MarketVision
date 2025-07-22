package com.gigass.trading.dto;

import java.util.List;

public class RiskControlResult {
    
    private Double stopLossRatio;
    private Double takeProfitRatio;
    private List<String> positionManagementRules;
    private List<String> riskMonitors;
    private List<String> emergencyExitConditions;
    private Double maxAcceptableLoss;
    private String riskLevel;
    
    // Getters and Setters
    public Double getStopLossRatio() { return stopLossRatio; }
    public void setStopLossRatio(Double stopLossRatio) { this.stopLossRatio = stopLossRatio; }
    
    public Double getTakeProfitRatio() { return takeProfitRatio; }
    public void setTakeProfitRatio(Double takeProfitRatio) { this.takeProfitRatio = takeProfitRatio; }
    
    public List<String> getPositionManagementRules() { return positionManagementRules; }
    public void setPositionManagementRules(List<String> positionManagementRules) { this.positionManagementRules = positionManagementRules; }
    
    public List<String> getRiskMonitors() { return riskMonitors; }
    public void setRiskMonitors(List<String> riskMonitors) { this.riskMonitors = riskMonitors; }
    
    public List<String> getEmergencyExitConditions() { return emergencyExitConditions; }
    public void setEmergencyExitConditions(List<String> emergencyExitConditions) { this.emergencyExitConditions = emergencyExitConditions; }
    
    public Double getMaxAcceptableLoss() { return maxAcceptableLoss; }
    public void setMaxAcceptableLoss(Double maxAcceptableLoss) { this.maxAcceptableLoss = maxAcceptableLoss; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
}