package com.gigass.trading.factory;

import com.gigass.trading.dto.SignalFilterResult;
import com.gigass.trading.entity.HotSearchRecord;
import org.springframework.stereotype.Component;

@Component
public class SignalCaptureFactory {

    public SignalFilterResult executeSignalCapture(HotSearchRecord record) {
        // TODO: Implement actual signal capture logic
        System.out.println("Executing signal capture for: " + record.getKeyword());
        SignalFilterResult result = new SignalFilterResult();
        result.setPassed(true);
        result.setSentimentScore(0.8);
        result.setPropagationSpeed(0.9);
        result.setVerificationScore(0.7);
        result.setTotalScore(0.85);
        return result;
    }
} 