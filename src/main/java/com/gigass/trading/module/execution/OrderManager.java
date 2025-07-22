package com.gigass.trading.module.execution;

import org.springframework.stereotype.Component;

@Component
public class OrderManager {
    public Object createOrder(String stock, String action, int quantity, double price) {
        // TODO: Implement order creation logic
        System.out.println("Creating order for " + stock);
        return new Object();
    }

    public void submitOrder(Object order) {
        // TODO: Implement order submission
        System.out.println("Submitting order");
    }
} 