package com.gigass.trading.module.strategy;

import com.gigass.trading.dto.ImpactAssessmentResult;
import com.gigass.trading.dto.ExitStrategyResult;
import com.gigass.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ExitStrategyDesigner {
    
    private static final Logger logger = LoggerFactory.getLogger(ExitStrategyDesigner.class);
    
    /**
     * 退出策略设计 - 制定分批退出计划和触发条件
     */
    public ExitStrategyResult designExitStrategy(HotSearchRecord record, 
                                               ImpactAssessmentResult assessmentResult) {
        logger.debug("执行退出策略设计: {}", record.getTitle());
        
        try {
            // 1. 设计分批退出计划
            var exitPlan = designBatchExitPlan(assessmentResult);
            
            // 2. 定义退出触发条件
            var exitTriggers = defineExitTriggers(assessmentResult);
            
            // 3. 计算最大持有期限
            LocalDateTime maxHoldingPeriod = calculateMaxHoldingPeriod(record, assessmentResult);
            
            // 4. 设计利润保护策略
            var profitProtectionStrategy = designProfitProtectionStrategy(assessmentResult);
            
            // 5. 制定紧急退出预案
            var emergencyExitPlan = designEmergencyExitPlan(assessmentResult);
            
            ExitStrategyResult result = new ExitStrategyResult();
            result.setBatchExitPlan(exitPlan);
            result.setExitTriggers(exitTriggers);
            result.setMaxHoldingPeriod(maxHoldingPeriod);
            result.setProfitProtectionStrategy(profitProtectionStrategy);
            result.setEmergencyExitPlan(emergencyExitPlan);
            result.setExitStrategy(determineMainExitStrategy(assessmentResult));
            
            logger.debug("退出策略设计完成: {}", result.getExitStrategy());
            return result;
            
        } catch (Exception e) {
            logger.error("退出策略设计失败: {}", e.getMessage(), e);
            return createDefaultExitStrategy();
        }
    }
    
    /**
     * 设计分批退出计划
     */
    private List<ExitStrategyResult.BatchExitStep> designBatchExitPlan(ImpactAssessmentResult assessmentResult) {
        Double volatilityScore = assessmentResult.getVolatilityScore();
        Double timeScore = assessmentResult.getTimeEffectivenessScore();
        
        if (volatilityScore == null) volatilityScore = 50.0;
        if (timeScore == null) timeScore = 50.0;
        
        List<ExitStrategyResult.BatchExitStep> plan = List.of();
        
        if (timeScore >= 80.0) {
            // 高时效性：快速退出策略
            plan.add(new ExitStrategyResult.BatchExitStep(1, 0.4, "达到10%收益时"));
            plan.add(new ExitStrategyResult.BatchExitStep(2, 0.3, "达到15%收益时"));
            plan.add(new ExitStrategyResult.BatchExitStep(3, 0.3, "热度开始下降时"));
        } else if (timeScore >= 60.0) {
            // 中等时效性：分批退出策略
            plan.add(new ExitStrategyResult.BatchExitStep(1, 0.3, "达到8%收益时"));
            plan.add(new ExitStrategyResult.BatchExitStep(2, 0.4, "达到12%收益时"));
            plan.add(new ExitStrategyResult.BatchExitStep(3, 0.3, "持有3天后"));
        } else {
            // 低时效性：长期持有策略
            plan.add(new ExitStrategyResult.BatchExitStep(1, 0.2, "达到5%收益时"));
            plan.add(new ExitStrategyResult.BatchExitStep(2, 0.3, "达到10%收益时"));
            plan.add(new ExitStrategyResult.BatchExitStep(3, 0.5, "持有1周后"));
        }
        
        return plan;
    }
    
    /**
     * 定义退出触发条件
     */
    private List<String> defineExitTriggers(ImpactAssessmentResult assessmentResult) {
        Double reverseScore = assessmentResult.getReverseVerificationScore();
        if (reverseScore == null) reverseScore = 50.0;
        
        List<String> triggers = List.of(
            "达到预设止盈目标",
            "触及止损线",
            "热搜热度下降超过50%",
            "相关股票成交量萎缩至平均水平以下",
            "出现重大负面消息"
        );
        
        if (reverseScore < 30.0) {
            // 市场反应已经很充分，增加更严格的退出条件
            triggers.add("任何技术指标转弱信号");
            triggers.add("大盘出现调整信号");
        }
        
        return triggers;
    }
    
    /**
     * 计算最大持有期限
     */
    private LocalDateTime calculateMaxHoldingPeriod(HotSearchRecord record, 
                                                  ImpactAssessmentResult assessmentResult) {
        Double timeScore = assessmentResult.getTimeEffectivenessScore();
        if (timeScore == null) timeScore = 50.0;
        
        LocalDateTime now = LocalDateTime.now();
        
        if (timeScore >= 80.0) {
            return now.plusDays(3); // 高时效性：最多持有3天
        } else if (timeScore >= 60.0) {
            return now.plusDays(7); // 中等时效性：最多持有1周
        } else {
            return now.plusDays(14); // 低时效性：最多持有2周
        }
    }
    
    /**
     * 设计利润保护策略
     */
    private ExitStrategyResult.ProfitProtectionStrategy designProfitProtectionStrategy(
            ImpactAssessmentResult assessmentResult) {
        
        Double volatilityScore = assessmentResult.getVolatilityScore();
        if (volatilityScore == null) volatilityScore = 50.0;
        
        ExitStrategyResult.ProfitProtectionStrategy strategy = 
            new ExitStrategyResult.ProfitProtectionStrategy();
        
        if (volatilityScore >= 80.0) {
            // 高波动：紧密跟踪止盈
            strategy.setTrailingStopRatio(0.03); // 3%跟踪止损
            strategy.setPartialProfitTaking(true);
            strategy.setDescription("高波动环境下采用3%跟踪止损，分批获利了结");
        } else if (volatilityScore >= 60.0) {
            // 中等波动：适中跟踪止盈
            strategy.setTrailingStopRatio(0.05); // 5%跟踪止损
            strategy.setPartialProfitTaking(true);
            strategy.setDescription("中等波动环境下采用5%跟踪止损");
        } else {
            // 低波动：宽松跟踪止盈
            strategy.setTrailingStopRatio(0.08); // 8%跟踪止损
            strategy.setPartialProfitTaking(false);
            strategy.setDescription("低波动环境下采用8%跟踪止损，整体退出");
        }
        
        return strategy;
    }
    
    /**
     * 制定紧急退出预案
     */
    private List<String> designEmergencyExitPlan(ImpactAssessmentResult assessmentResult) {
        return List.of(
            "市场恐慌情绪蔓延时立即清仓",
            "相关热搜被官方辟谣时立即清仓",
            "监管政策突然收紧时立即清仓",
            "系统性风险出现时立即清仓",
            "单日跌幅超过止损线时立即清仓"
        );
    }
    
    /**
     * 确定主要退出策略
     */
    private String determineMainExitStrategy(ImpactAssessmentResult assessmentResult) {
        Double timeScore = assessmentResult.getTimeEffectivenessScore();
        Double volatilityScore = assessmentResult.getVolatilityScore();
        
        if (timeScore == null) timeScore = 50.0;
        if (volatilityScore == null) volatilityScore = 50.0;
        
        if (timeScore >= 80.0 && volatilityScore >= 80.0) {
            return "快进快出策略";
        } else if (timeScore >= 60.0) {
            return "分批退出策略";
        } else {
            return "长期持有策略";
        }
    }
    
    /**
     * 创建默认退出策略
     */
    private ExitStrategyResult createDefaultExitStrategy() {
        ExitStrategyResult result = new ExitStrategyResult();
        result.setExitStrategy("保守退出策略");
        result.setMaxHoldingPeriod(LocalDateTime.now().plusDays(7));
        
        List<ExitStrategyResult.BatchExitStep> defaultPlan = List.of(
            new ExitStrategyResult.BatchExitStep(1, 0.5, "达到5%收益时"),
            new ExitStrategyResult.BatchExitStep(2, 0.5, "达到10%收益时")
        );
        result.setBatchExitPlan(defaultPlan);
        
        result.setExitTriggers(List.of("达到止盈止损目标", "持有期限到达"));
        
        return result;
    }
}