package com.jolywood.trading.dto;

public class SignalFilterResult {
    
    private Long hotSearchId;
    private String title;
    private Double heatScore;
    private Double sentimentScore;
    private Double propagationSpeed;
    private Double verificationScore;
    private Double totalScore;
    private Boolean passed;
    private String reason;
    
    public SignalFilterResult() {}
    
    public SignalFilterResult(Long hotSearchId, String title) {
        this.hotSearchId = hotSearchId;
        this.title = title;
    }
    
    // TODO: 添加完整的getter/setter方法
    
    public Long getHotSearchId() { return hotSearchId; }
    public void setHotSearchId(Long hotSearchId) { this.hotSearchId = hotSearchId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public Double getHeatScore() { return heatScore; }
    public void setHeatScore(Double heatScore) { this.heatScore = heatScore; }
    
    public Double getSentimentScore() { return sentimentScore; }
    public void setSentimentScore(Double sentimentScore) { this.sentimentScore = sentimentScore; }
    
    public Double getPropagationSpeed() { return propagationSpeed; }
    public void setPropagationSpeed(Double propagationSpeed) { this.propagationSpeed = propagationSpeed; }
    
    public Double getVerificationScore() { return verificationScore; }
    public void setVerificationScore(Double verificationScore) { this.verificationScore = verificationScore; }
    
    public Double getTotalScore() { return totalScore; }
    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }
    
    public Boolean getPassed() { return passed; }
    public void setPassed(Boolean passed) { this.passed = passed; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}