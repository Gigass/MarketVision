package com.jolywood.trading.module.strategy;

import com.jolywood.trading.dto.ImpactAssessmentResult;
import com.jolywood.trading.dto.TradingStrategyResult;
import com.jolywood.trading.dto.BacktestResult;
import com.jolywood.trading.entity.HotSearchRecord;
import com.jolywood.trading.repository.HotSearchRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BacktestValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(BacktestValidator.class);
    
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;
    
    /**
     * 回测验证 - 基于历史数据验证策略有效性
     */
    public BacktestResult validateStrategy(HotSearchRecord record, 
                                         ImpactAssessmentResult assessmentResult,
                                         TradingStrategyResult strategyResult) {
        logger.debug("执行回测验证: {}", record.getTitle());
        
        try {
            // 1. 查找历史相似案例
            List<HotSearchRecord> historicalCases = findHistoricalCases(record);
            
            // 2. 模拟历史策略执行
            var simulationResults = simulateHistoricalExecution(historicalCases, strategyResult);
            
            // 3. 计算策略表现指标
            var performanceMetrics = calculatePerformanceMetrics(simulationResults);
            
            // 4. 分析风险指标
            var riskMetrics = calculateRiskMetrics(simulationResults);
            
            // 5. 生成验证结论
            String validationConclusion = generateValidationConclusion(performanceMetrics, riskMetrics);
            
            BacktestResult result = new BacktestResult();
            result.setHistoricalCaseCount(historicalCases.size());
            result.setSimulationResults(simulationResults);
            result.setPerformanceMetrics(performanceMetrics);
            result.setRiskMetrics(riskMetrics);
            result.setValidationConclusion(validationConclusion);
            result.setSuccessRate(calculateSuccessRate(simulationResults));
            result.setAverageReturn(calculateAverageReturn(simulationResults));
            result.setMaxDrawdown(calculateMaxDrawdown(simulationResults));
            
            logger.debug("回测验证完成 - 成功率: {}%, 平均收益: {}%", 
                result.getSuccessRate() * 100, result.getAverageReturn() * 100);
            
            return result;
            
        } catch (Exception e) {
            logger.error("回测验证失败: {}", e.getMessage(), e);
            return createDefaultBacktestResult();
        }
    }
    
    /**
     * 查找历史相似案例
     */
    private List<HotSearchRecord> findHistoricalCases(HotSearchRecord record) {
        try {
            // 基于关键词查找相似历史记录
            String keyword = extractKeyword(record.getTitle());
            List<HotSearchRecord> similarRecords = hotSearchRecordRepository.findByKeyword(keyword);
            
            // 过滤出已完成处理的记录
            return similarRecords.stream()
                .filter(r -> r.getStatus() == HotSearchRecord.ProcessStatus.COMPLETED)
                .filter(r -> !r.getId().equals(record.getId())) // 排除当前记录
                .limit(20) // 最多取20个案例
                .toList();
            
        } catch (Exception e) {
            logger.warn("查找历史案例失败: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * 模拟历史策略执行
     */
    private List<BacktestResult.SimulationResult> simulateHistoricalExecution(
            List<HotSearchRecord> historicalCases, TradingStrategyResult strategyResult) {
        
        return historicalCases.stream()
            .map(historicalCase -> {
                BacktestResult.SimulationResult simulation = new BacktestResult.SimulationResult();
                simulation.setCaseId(historicalCase.getId());
                simulation.setCaseTitle(historicalCase.getTitle());
                
                // 模拟策略执行结果（简化逻辑）
                double simulatedReturn = simulateReturn(historicalCase, strategyResult);
                simulation.setSimulatedReturn(simulatedReturn);
                simulation.setSuccess(simulatedReturn > 0);
                
                // 模拟最大回撤
                simulation.setMaxDrawdown(Math.abs(simulatedReturn) * 0.3); // 简化计算
                
                // 模拟持有天数
                simulation.setHoldingDays(calculateHoldingDays(historicalCase));
                
                return simulation;
            })
            .toList();
    }
    
    /**
     * 模拟收益率
     */
    private double simulateReturn(HotSearchRecord historicalCase, TradingStrategyResult strategyResult) {
        // 简化的收益模拟逻辑
        Double heatScore = historicalCase.getHeatScore();
        Double sentimentScore = historicalCase.getSentimentScore();
        
        if (heatScore == null) heatScore = 0.0;
        if (sentimentScore == null) sentimentScore = 50.0;
        
        // 基于历史数据的简化收益计算
        double baseReturn = (heatScore / 1000.0) + ((sentimentScore - 50.0) / 500.0);
        
        // 添加随机波动
        double randomFactor = (Math.random() - 0.5) * 0.1; // ±5%随机波动
        
        return baseReturn + randomFactor;
    }
    
    /**
     * 计算持有天数
     */
    private int calculateHoldingDays(HotSearchRecord historicalCase) {
        // 简化逻辑：基于热度分数估算持有天数
        Double heatScore = historicalCase.getHeatScore();
        if (heatScore == null) heatScore = 0.0;
        
        if (heatScore >= 800) {
            return 2; // 高热度，短期持有
        } else if (heatScore >= 500) {
            return 5; // 中等热度，中期持有
        } else {
            return 10; // 低热度，长期持有
        }
    }
    
    /**
     * 计算表现指标
     */
    private BacktestResult.PerformanceMetrics calculatePerformanceMetrics(
            List<BacktestResult.SimulationResult> simulations) {
        
        BacktestResult.PerformanceMetrics metrics = new BacktestResult.PerformanceMetrics();
        
        if (simulations.isEmpty()) {
            return metrics;
        }
        
        // 计算平均收益
        double avgReturn = simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getSimulatedReturn)
            .average()
            .orElse(0.0);
        metrics.setAverageReturn(avgReturn);
        
        // 计算成功率
        long successCount = simulations.stream()
            .mapToLong(s -> s.isSuccess() ? 1 : 0)
            .sum();
        metrics.setSuccessRate((double) successCount / simulations.size());
        
        // 计算最大收益
        double maxReturn = simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getSimulatedReturn)
            .max()
            .orElse(0.0);
        metrics.setMaxReturn(maxReturn);
        
        // 计算最大亏损
        double maxLoss = simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getSimulatedReturn)
            .min()
            .orElse(0.0);
        metrics.setMaxLoss(maxLoss);
        
        return metrics;
    }
    
    /**
     * 计算风险指标
     */
    private BacktestResult.RiskMetrics calculateRiskMetrics(
            List<BacktestResult.SimulationResult> simulations) {
        
        BacktestResult.RiskMetrics metrics = new BacktestResult.RiskMetrics();
        
        if (simulations.isEmpty()) {
            return metrics;
        }
        
        // 计算收益标准差（波动率）
        double[] returns = simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getSimulatedReturn)
            .toArray();
        
        double mean = simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getSimulatedReturn)
            .average()
            .orElse(0.0);
        
        double variance = 0.0;
        for (double ret : returns) {
            variance += Math.pow(ret - mean, 2);
        }
        variance /= returns.length;
        
        metrics.setVolatility(Math.sqrt(variance));
        
        // 计算最大回撤
        double maxDrawdown = simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getMaxDrawdown)
            .max()
            .orElse(0.0);
        metrics.setMaxDrawdown(maxDrawdown);
        
        // 计算夏普比率（简化）
        double sharpeRatio = metrics.getVolatility() > 0 ? mean / metrics.getVolatility() : 0.0;
        metrics.setSharpeRatio(sharpeRatio);
        
        return metrics;
    }
    
    /**
     * 生成验证结论
     */
    private String generateValidationConclusion(BacktestResult.PerformanceMetrics performance,
                                              BacktestResult.RiskMetrics risk) {
        StringBuilder conclusion = new StringBuilder();
        
        if (performance.getSuccessRate() >= 0.7) {
            conclusion.append("策略历史表现优秀，");
        } else if (performance.getSuccessRate() >= 0.5) {
            conclusion.append("策略历史表现一般，");
        } else {
            conclusion.append("策略历史表现较差，");
        }
        
        if (performance.getAverageReturn() >= 0.1) {
            conclusion.append("平均收益率较高，");
        } else if (performance.getAverageReturn() >= 0.05) {
            conclusion.append("平均收益率适中，");
        } else {
            conclusion.append("平均收益率偏低，");
        }
        
        if (risk.getMaxDrawdown() <= 0.1) {
            conclusion.append("风险控制良好");
        } else if (risk.getMaxDrawdown() <= 0.2) {
            conclusion.append("风险控制一般");
        } else {
            conclusion.append("风险控制需要加强");
        }
        
        return conclusion.toString();
    }
    
    /**
     * 计算成功率
     */
    private Double calculateSuccessRate(List<BacktestResult.SimulationResult> simulations) {
        if (simulations.isEmpty()) return 0.0;
        
        long successCount = simulations.stream()
            .mapToLong(s -> s.isSuccess() ? 1 : 0)
            .sum();
        
        return (double) successCount / simulations.size();
    }
    
    /**
     * 计算平均收益
     */
    private Double calculateAverageReturn(List<BacktestResult.SimulationResult> simulations) {
        return simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getSimulatedReturn)
            .average()
            .orElse(0.0);
    }
    
    /**
     * 计算最大回撤
     */
    private Double calculateMaxDrawdown(List<BacktestResult.SimulationResult> simulations) {
        return simulations.stream()
            .mapToDouble(BacktestResult.SimulationResult::getMaxDrawdown)
            .max()
            .orElse(0.0);
    }
    
    /**
     * 提取关键词
     */
    private String extractKeyword(String title) {
        // 简化的关键词提取逻辑
        if (title == null || title.isEmpty()) {
            return "";
        }
        
        // 取标题的前几个字符作为关键词
        return title.length() > 10 ? title.substring(0, 10) : title;
    }
    
    /**
     * 创建默认回测结果
     */
    private BacktestResult createDefaultBacktestResult() {
        BacktestResult result = new BacktestResult();
        result.setHistoricalCaseCount(0);
        result.setSuccessRate(0.5);
        result.setAverageReturn(0.0);
        result.setMaxDrawdown(0.1);
        result.setValidationConclusion("缺乏足够历史数据，建议谨慎操作");
        
        BacktestResult.PerformanceMetrics performance = new BacktestResult.PerformanceMetrics();
        performance.setSuccessRate(0.5);
        performance.setAverageReturn(0.0);
        result.setPerformanceMetrics(performance);
        
        BacktestResult.RiskMetrics risk = new BacktestResult.RiskMetrics();
        risk.setMaxDrawdown(0.1);
        risk.setVolatility(0.15);
        result.setRiskMetrics(risk);
        
        return result;
    }
}