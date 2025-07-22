package com.gigass.trading.dto;

public class SignalFilterResult {
    private boolean passed;
    private double sentimentScore;
    private double propagationSpeed;
    private double verificationScore;
    private double totalScore;

    public boolean getPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public double getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public double getPropagationSpeed() {
        return propagationSpeed;
    }

    public void setPropagationSpeed(double propagationSpeed) {
        this.propagationSpeed = propagationSpeed;
    }

    public double getVerificationScore() {
        return verificationScore;
    }

    public void setVerificationScore(double verificationScore) {
        this.verificationScore = verificationScore;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }
}