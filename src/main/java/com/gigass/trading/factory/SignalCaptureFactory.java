package com.gigass.trading.factory;

import com.gigass.trading.module.signal.*;
import com.gigass.trading.dto.SignalFilterResult;
import com.gigass.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ArrayList;

@Component
public class SignalCaptureFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(SignalCaptureFactory.class);
    
    @Autowired
    private HeatBurstDetector heatBurstDetector;
    
    @Autowired
    private StockCorrelationAnalyzer stockCorrelationAnalyzer;
    
    @Autowired
    private SentimentQuantifier sentimentQuantifier;
    
    @Autowired
    private AuthenticityVerifier authenticityVerifier;
    
    @Autowired
    private PropagationSpeedMeter propagationSpeedMeter;
    
    @Autowired
    private HistoricalPatternMatcher historicalPatternMatcher;
    
    /**
     * 执行完整的信号捕捉与筛选流程
     */
    public SignalFilterResult executeSignalCapture(HotSearchRecord record) {
        logger.info("开始执行信号捕捉与筛选: {}", record.getTitle());
        
        SignalFilterResult result = new SignalFilterResult(record.getId(), record.getTitle());
        
        try {
            // 1. 热度爆发检测
            Double heatBurstScore = heatBurstDetector.detectHeatBurst(record);
            result.setHeatScore(heatBurstScore);
            
            // 2. 股票关联度分析
            Double correlationScore = stockCorrelationAnalyzer.analyzeCorrelation(record);
            
            // 3. 情绪极性量化
            Double sentimentScore = sentimentQuantifier.quantifySentiment(record);
            result.setSentimentScore(sentimentScore);
            
            // 4. 真实性验证
            Double authenticityScore = authenticityVerifier.verifyAuthenticity(record);
            result.setVerificationScore(authenticityScore);
            
            // 5. 传播速度测量
            Double propagationScore = propagationSpeedMeter.measureSpeed(record);
            result.setPropagationSpeed(propagationScore);
            
            // 6. 相似历史模式识别
            Double patternScore = historicalPatternMatcher.matchPattern(record);
            
            // 计算综合分数
            Double totalScore = calculateTotalScore(heatBurstScore, correlationScore, 
                sentimentScore, authenticityScore, propagationScore, patternScore);
            result.setTotalScore(totalScore);
            
            // 判断是否通过筛选
            boolean passed = totalScore >= 300.0; // 阈值可配置
            result.setPassed(passed);
            
            if (!passed) {
                result.setReason("综合评分不足，总分: " + totalScore);
            }
            
            logger.info("信号捕捉完成 - 总分: {}, 通过: {}", totalScore, passed);
            
        } catch (Exception e) {
            logger.error("信号捕捉失败: {}", e.getMessage(), e);
            result.setPassed(false);
            result.setReason("处理异常: " + e.getMessage());
        }
        
        return result;
    }
    
    private Double calculateTotalScore(Double... scores) {
        double total = 0.0;
        for (Double score : scores) {
            if (score != null) {
                total += score;
            }
        }
        return total;
    }
}