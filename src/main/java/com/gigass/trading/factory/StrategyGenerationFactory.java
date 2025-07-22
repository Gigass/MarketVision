package com.gigass.trading.factory;

import com.gigass.trading.module.strategy.*;
import com.gigass.trading.dto.ImpactAssessmentResult;
import com.gigass.trading.dto.TradingStrategyResult;
import com.gigass.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyGenerationFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(StrategyGenerationFactory.class);
    
    @Autowired
    private PositionSizeCalculator positionSizeCalculator;
    
    @Autowired
    private EntryTimingOptimizer entryTimingOptimizer;
    
    @Autowired
    private RiskControlPlanner riskControlPlanner;
    
    @Autowired
    private ExitStrategyDesigner exitStrategyDesigner;
    
    @Autowired
    private PortfolioBalancer portfolioBalancer;
    
    @Autowired
    private BacktestValidator backtestValidator;
    
    /**
     * 执行完整的策略生成流程
     */
    public TradingStrategyResult generateTradingStrategy(HotSearchRecord record, 
                                                        ImpactAssessmentResult assessmentResult) {
        logger.info("开始生成交易策略: {}", record.getTitle());
        
        TradingStrategyResult result = new TradingStrategyResult(record.getId(), record.getTitle());
        
        try {
            // 检查是否具有投资价值
            if (!assessmentResult.getHasInvestmentValue()) {
                result.setRecommendation("不建议交易");
                result.setReason("影响评估未通过: " + assessmentResult.getReason());
                return result;
            }
            
            // 1. 仓位规模计算
            var positionSizes = positionSizeCalculator.calculatePositionSize(record, assessmentResult);
            result.setPositionSizes(positionSizes);
            
            // 2. 入场时机优化
            var entryTiming = entryTimingOptimizer.optimizeEntryTiming(record, assessmentResult);
            result.setEntryTiming(entryTiming);
            
            // 3. 风险控制规划
            var riskControls = riskControlPlanner.planRiskControl(record, assessmentResult);
            result.setRiskControls(riskControls);
            
            // 4. 退出策略设计
            var exitStrategy = exitStrategyDesigner.designExitStrategy(record, assessmentResult);
            result.setExitStrategy(exitStrategy);
            
            // 5. 组合平衡调整
            var portfolioAdjustment = portfolioBalancer.balancePortfolio(record, assessmentResult);
            result.setPortfolioAdjustment(portfolioAdjustment);
            
            // 6. 回测验证
            var backtestResult = backtestValidator.validateStrategy(record, assessmentResult, result);
            result.setBacktestResult(backtestResult);
            
            // 生成最终建议
            generateFinalRecommendation(result, assessmentResult);
            
            logger.info("交易策略生成完成: {} - 建议: {}", record.getTitle(), result.getRecommendation());
            
        } catch (Exception e) {
            logger.error("交易策略生成失败: {}", e.getMessage(), e);
            result.setRecommendation("策略生成失败");
            result.setReason("处理异常: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * 生成最终交易建议
     */
    private void generateFinalRecommendation(TradingStrategyResult result, 
                                           ImpactAssessmentResult assessmentResult) {
        try {
            Double totalScore = assessmentResult.getTotalImpactScore();
            var backtestResult = result.getBacktestResult();
            
            if (totalScore >= 350.0 && backtestResult != null && backtestResult.getSuccessRate() >= 0.7) {
                result.setRecommendation("强烈建议");
                result.setConfidenceLevel(0.9);
            } else if (totalScore >= 300.0 && backtestResult != null && backtestResult.getSuccessRate() >= 0.6) {
                result.setRecommendation("建议交易");
                result.setConfidenceLevel(0.7);
            } else if (totalScore >= 250.0) {
                result.setRecommendation("谨慎交易");
                result.setConfidenceLevel(0.5);
            } else {
                result.setRecommendation("不建议交易");
                result.setConfidenceLevel(0.3);
            }
            
        } catch (Exception e) {
            logger.warn("生成最终建议失败: {}", e.getMessage());
            result.setRecommendation("建议人工审核");
            result.setConfidenceLevel(0.0);
        }
    }
}