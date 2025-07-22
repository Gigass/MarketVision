package com.gigass.trading.module.feedback;

import org.springframework.stereotype.Component;

@Component
public class PostTradeAnalyzer {
    public void analyzeTradePerformance(String order) {
        // TODO: Implement trade performance analysis
        System.out.println("Analyzing trade performance for order: " + order);
    }
} 