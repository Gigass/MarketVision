package com.gigass.trading.module.signal;

import com.gigass.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PropagationSpeedMeter {
    
    private static final Logger logger = LoggerFactory.getLogger(PropagationSpeedMeter.class);
    
    /**
     * 传播速度测量 - 计算热搜话题的传播速率和加速度
     */
    public Double measureSpeed(HotSearchRecord record) {
        logger.debug("执行传播速度测量: {}", record.getTitle());
        
        try {
            // TODO: 实现传播速度测量算法
            // 1. 计算时间跨度
            // 2. 分析热度增长曲线
            // 3. 计算传播速率
            // 4. 评估加速度
            
            Double propagationRate = calculatePropagationRate(record);
            Double acceleration = calculateAcceleration(record);
            Double viralityIndex = calculateViralityIndex(record);
            
            // 综合计算传播速度分数
            Double speedScore = (propagationRate + acceleration + viralityIndex) / 3.0;
            
            logger.debug("传播速度测量完成，分数: {}", speedScore);
            return speedScore;
            
        } catch (Exception e) {
            logger.error("传播速度测量失败: {}", e.getMessage(), e);
            return 0.0;
        }
    }
    
    /**
     * 计算传播速率
     */
    private Double calculatePropagationRate(HotSearchRecord record) {
        try {
            // 简单的传播速率计算
            LocalDateTime now = LocalDateTime.now();
            long minutesSinceCreation = ChronoUnit.MINUTES.between(record.getCreatedTime(), now);
            
            if (minutesSinceCreation == 0) minutesSinceCreation = 1;
            
            Integer heatScore = record.getHeatScore();
            if (heatScore == null) heatScore = 0;
            
            // 热度/时间 = 传播速率
            double rate = (double) heatScore / minutesSinceCreation;
            return Math.min(rate * 10, 100.0); // 归一化到0-100
            
        } catch (Exception e) {
            logger.warn("传播速率计算失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 计算加速度
     */
    private Double calculateAcceleration(HotSearchRecord record) {
        // TODO: 实现加速度计算
        // 需要历史数据点来计算二阶导数
        return 60.0; // 默认分数
    }
    
    /**
     * 计算病毒式传播指数
     */
    private Double calculateViralityIndex(HotSearchRecord record) {
        // TODO: 实现病毒式传播指数计算
        return 70.0; // 默认分数
    }
}