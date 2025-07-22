package com.jolywood.trading.module.strategy;

import com.jolywood.trading.dto.ImpactAssessmentResult;
import com.jolywood.trading.dto.PositionSizeResult;
import com.jolywood.trading.entity.HotSearchRecord;
import com.jolywood.trading.entity.StockInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PositionSizeCalculator {
    
    private static final Logger logger = LoggerFactory.getLogger(PositionSizeCalculator.class);
    
    @Value("${trading.system.trading.max-position-size:0.1}")
    private Double maxPositionSize;
    
    @Value("${trading.system.trading.max-concurrent-trades:3}")
    private Integer maxConcurrentTrades;
    
    /**
     * 仓位规模计算 - 基于风险评估和资金管理原则计算合理仓位
     */
    public List<PositionSizeResult> calculatePositionSize(HotSearchRecord record, 
                                                         ImpactAssessmentResult assessmentResult) {
        logger.debug("执行仓位规模计算: {}", record.getTitle());
        
        try {
            // 1. 基础仓位计算
            Double basePositionSize = calculateBasePositionSize(assessmentResult);
            
            // 2. 风险调整
            Double riskAdjustedSize = applyRiskAdjustment(basePositionSize, assessmentResult);
            
            // 3. 波动率调整
            Double volatilityAdjustedSize = applyVolatilityAdjustment(riskAdjustedSize, assessmentResult);
            
            // 4. 时效性调整
            Double timeAdjustedSize = applyTimeAdjustment(volatilityAdjustedSize, assessmentResult);
            
            // 5. 为每个股票群组分配仓位
            List<PositionSizeResult> results = allocatePositionsByGroup(
                timeAdjustedSize, assessmentResult);
            
            logger.debug("仓位规模计算完成，共 {} 个仓位分配", results.size());
            return results;
            
        } catch (Exception e) {
            logger.error("仓位规模计算失败: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    /**
     * 计算基础仓位大小
     */
    private Double calculateBasePositionSize(ImpactAssessmentResult assessmentResult) {
        Double totalScore = assessmentResult.getTotalImpactScore();
        if (totalScore == null) totalScore = 0.0;
        
        // 基于总分计算基础仓位（最大不超过配置的最大仓位）
        Double baseSize = Math.min(totalScore / 500.0 * maxPositionSize, maxPositionSize);
        
        return Math.max(baseSize, 0.01); // 最小1%仓位
    }
    
    /**
     * 应用风险调整
     */
    private Double applyRiskAdjustment(Double baseSize, ImpactAssessmentResult assessmentResult) {
        Double reverseScore = assessmentResult.getReverseVerificationScore();
        if (reverseScore == null) reverseScore = 50.0;
        
        // 反向验证分数越低（市场反应越充分），仓位越小
        Double riskFactor = reverseScore / 100.0;
        
        return baseSize * riskFactor;
    }
    
    /**
     * 应用波动率调整
     */
    private Double applyVolatilityAdjustment(Double adjustedSize, ImpactAssessmentResult assessmentResult) {
        Double volatilityScore = assessmentResult.getVolatilityScore();
        if (volatilityScore == null) volatilityScore = 50.0;
        
        // 波动率越高，仓位越小
        Double volatilityFactor = Math.max(1.0 - (volatilityScore - 50.0) / 100.0, 0.5);
        
        return adjustedSize * volatilityFactor;
    }
    
    /**
     * 应用时效性调整
     */
    private Double applyTimeAdjustment(Double adjustedSize, ImpactAssessmentResult assessmentResult) {
        Double timeScore = assessmentResult.getTimeEffectivenessScore();
        if (timeScore == null) timeScore = 50.0;
        
        // 时效性越强，可以适当增加仓位
        Double timeFactor = Math.min(timeScore / 50.0, 1.5);
        
        return adjustedSize * timeFactor;
    }
    
    /**
     * 按股票群组分配仓位
     */
    private List<PositionSizeResult> allocatePositionsByGroup(Double totalPositionSize, 
                                                            ImpactAssessmentResult assessmentResult) {
        var stockGroups = assessmentResult.getRelatedStockGroups();
        if (stockGroups == null || stockGroups.isEmpty()) {
            return List.of();
        }
        
        return stockGroups.stream()
            .map(group -> {
                // 根据相关度分配仓位权重
                Double weight = group.getRelevanceScore() / 100.0;
                Double groupPositionSize = totalPositionSize * weight;
                
                // 在群组内平均分配给各股票
                Double stockPositionSize = groupPositionSize / group.getStockCount();
                
                return new PositionSizeResult(
                    group.getGroupType(),
                    group.getStocks(),
                    stockPositionSize,
                    groupPositionSize,
                    weight
                );
            })
            .collect(Collectors.toList());
    }
}