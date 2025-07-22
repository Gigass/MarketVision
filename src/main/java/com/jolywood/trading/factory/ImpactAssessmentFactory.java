package com.jolywood.trading.factory;

import com.jolywood.trading.module.assessment.*;
import com.jolywood.trading.dto.ImpactAssessmentResult;
import com.jolywood.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImpactAssessmentFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(ImpactAssessmentFactory.class);
    
    @Autowired
    private MarketSentimentPredictor marketSentimentPredictor;
    
    @Autowired
    private FundFlowPredictor fundFlowPredictor;
    
    @Autowired
    private StockGroupIdentifier stockGroupIdentifier;
    
    @Autowired
    private VolatilityEstimator volatilityEstimator;
    
    @Autowired
    private TimeEffectivenessAssessor timeEffectivenessAssessor;
    
    @Autowired
    private ReverseVerifier reverseVerifier;
    
    /**
     * 执行完整的影响评估流程
     */
    public ImpactAssessmentResult executeImpactAssessment(HotSearchRecord record) {
        logger.info("开始执行影响评估: {}", record.getTitle());
        
        ImpactAssessmentResult result = new ImpactAssessmentResult(record.getId(), record.getTitle());
        
        try {
            // 1. 市场情绪预测
            Double marketSentimentScore = marketSentimentPredictor.predictMarketSentiment(record);
            result.setMarketSentimentScore(marketSentimentScore);
            
            // 2. 资金流向预判
            Double fundFlowScore = fundFlowPredictor.predictFundFlow(record);
            result.setFundFlowScore(fundFlowScore);
            
            // 3. 相关股票群组识别
            var stockGroups = stockGroupIdentifier.identifyStockGroups(record);
            result.setRelatedStockGroups(stockGroups);
            
            // 4. 波动幅度估算
            Double volatilityScore = volatilityEstimator.estimateVolatility(record);
            result.setVolatilityScore(volatilityScore);
            
            // 5. 时效性评估
            Double timeEffectivenessScore = timeEffectivenessAssessor.assessTimeEffectiveness(record);
            result.setTimeEffectivenessScore(timeEffectivenessScore);
            
            // 6. 反向验证
            Double reverseVerificationScore = reverseVerifier.performReverseVerification(record);
            result.setReverseVerificationScore(reverseVerificationScore);
            
            // 计算综合影响评估分数
            Double totalImpactScore = calculateTotalImpactScore(
                marketSentimentScore, fundFlowScore, volatilityScore, 
                timeEffectivenessScore, reverseVerificationScore);
            result.setTotalImpactScore(totalImpactScore);
            
            // 判断是否具有投资价值
            boolean hasInvestmentValue = totalImpactScore >= 250.0; // 阈值可配置
            result.setHasInvestmentValue(hasInvestmentValue);
            
            if (!hasInvestmentValue) {
                result.setReason("影响评估分数不足，总分: " + totalImpactScore);
            }
            
            logger.info("影响评估完成 - 总分: {}, 具有投资价值: {}", totalImpactScore, hasInvestmentValue);
            
        } catch (Exception e) {
            logger.error("影响评估失败: {}", e.getMessage(), e);
            result.setHasInvestmentValue(false);
            result.setReason("处理异常: " + e.getMessage());
        }
        
        return result;
    }
    
    private Double calculateTotalImpactScore(Double... scores) {
        double total = 0.0;
        int count = 0;
        for (Double score : scores) {
            if (score != null) {
                total += score;
                count++;
            }
        }
        return count > 0 ? total : 0.0;
    }
}