package com.gigass.trading.module.base;

import com.gigass.trading.entity.HotSearchRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class HotsearchProcessor {
    public Object processHotsearchData(Object data) {
        // TODO: Implement actual data processing
        System.out.println("Processing hot search data...");
        return new ArrayList<HotSearchRecord>();
    }
} 