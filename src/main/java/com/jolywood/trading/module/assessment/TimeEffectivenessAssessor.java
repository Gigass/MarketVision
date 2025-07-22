package com.jolywood.trading.module.assessment;

import com.jolywood.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class TimeEffectivenessAssessor {
    
    private static final Logger logger = LoggerFactory.getLogger(TimeEffectivenessAssessor.class);
    
    /**
     * 时效性评估 - 分析热搜影响的可能持续时长
     */
    public Double assessTimeEffectiveness(HotSearchRecord record) {
        logger.debug("执行时效性评估: {}", record.getTitle());
        
        try {
            // 1. 分析事件类型的时效性
            Double eventTypeEffectiveness = analyzeEventTypeEffectiveness(record);
            
            // 2. 分析传播速度对时效性的影响
            Double propagationEffectiveness = analyzePropagationEffectiveness(record);
            
            // 3. 分析市场关注度持续性
            Double attentionSustainability = analyzeAttentionSustainability(record);
            
            // 4. 分析时间衰减因子
            Double timeDecayFactor = analyzeTimeDecayFactor(record);
            
            // 综合计算时效性评估分数
            Double timeEffectivenessScore = (eventTypeEffectiveness + propagationEffectiveness + 
                attentionSustainability + timeDecayFactor) / 4.0;
            
            logger.debug("时效性评估完成，分数: {}", timeEffectivenessScore);
            return timeEffectivenessScore;
            
        } catch (Exception e) {
            logger.error("时效性评估失败: {}", e.getMessage(), e);
            return 0.0;
        }
    }
    
    /**
     * 分析事件类型的时效性
     */
    private Double analyzeEventTypeEffectiveness(HotSearchRecord record) {
        try {
            String title = record.getTitle().toLowerCase();
            
            // 根据事件类型评估时效性
            if (title.contains("突发") || title.contains("紧急")) {
                return 90.0; // 突发事件时效性强
            } else if (title.contains("业绩") || title.contains("财报")) {
                return 70.0; // 业绩类事件中等时效性
            } else if (title.contains("政策") || title.contains("法规")) {
                return 80.0; // 政策类事件时效性较强
            } else if (title.contains("传言") || title.contains("消息")) {
                return 60.0; // 传言类事件时效性一般
            } else if (title.contains("长期") || title.contains("战略")) {
                return 40.0; // 长期战略类事件时效性较弱
            }
            
            return 50.0; // 默认时效性
            
        } catch (Exception e) {
            logger.warn("事件类型时效性分析失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 分析传播速度对时效性的影响
     */
    private Double analyzePropagationEffectiveness(HotSearchRecord record) {
        try {
            Double propagationSpeed = record.getPropagationSpeed();
            if (propagationSpeed == null) {
                return 50.0;
            }
            
            // 传播速度越快，时效性越强
            return Math.min(propagationSpeed * 1.2, 100.0);
            
        } catch (Exception e) {
            logger.warn("传播速度时效性分析失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 分析市场关注度持续性
     */
    private Double analyzeAttentionSustainability(HotSearchRecord record) {
        try {
            // 基于热度分数和来源分析关注度持续性
            Integer heatScore = record.getHeatScore();
            if (heatScore == null) heatScore = 0;
            
            String source = record.getSource();
            double sourceWeight = getSourceWeight(source);
            
            // 热度越高，权威来源，关注度持续性越强
            return Math.min((heatScore / 10.0) * sourceWeight, 100.0);
            
        } catch (Exception e) {
            logger.warn("关注度持续性分析失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 分析时间衰减因子
     */
    private Double analyzeTimeDecayFactor(HotSearchRecord record) {
        try {
            LocalDateTime now = LocalDateTime.now();
            long hoursSinceCreation = ChronoUnit.HOURS.between(record.getCreatedTime(), now);
            
            // 时间衰减函数：随时间递减
            if (hoursSinceCreation <= 1) {
                return 100.0; // 1小时内，时效性最强
            } else if (hoursSinceCreation <= 6) {
                return 80.0; // 6小时内，时效性较强
            } else if (hoursSinceCreation <= 24) {
                return 60.0; // 24小时内，时效性中等
            } else if (hoursSinceCreation <= 72) {
                return 40.0; // 3天内，时效性较弱
            } else {
                return 20.0; // 3天后，时效性很弱
            }
            
        } catch (Exception e) {
            logger.warn("时间衰减因子分析失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 获取来源权重
     */
    private double getSourceWeight(String source) {
        if (source == null) return 1.0;
        
        switch (source.toLowerCase()) {
            case "weibo":
                return 1.2; // 微博传播快，权重高
            case "douyin":
                return 1.1; // 抖音影响力大
            case "zhihu":
                return 1.0; // 知乎相对理性
            default:
                return 1.0;
        }
    }
}