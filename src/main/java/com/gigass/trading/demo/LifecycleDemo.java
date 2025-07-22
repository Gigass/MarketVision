package com.gigass.trading.demo;

import com.gigass.ai.module.agent.AgentFactory;
import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.enums.LifecycleStage;
import com.gigass.trading.module.acquisition.SocialMediaCrawlerFactory;
import com.gigass.trading.module.base.HotsearchProcessor;
import com.gigass.trading.module.execution.OrderManager;
import com.gigass.trading.module.feedback.PostTradeAnalyzer;
import com.gigass.trading.module.monitoring.HeatTrendTracker;
import com.gigass.trading.module.monitoring.MarketReactionEvaluator;
import com.gigass.trading.service.DataAcquisitionService;
import com.gigass.trading.service.HotsearchLifecycleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 生命周期流程演示Demo
 * 模拟完整的热搜驱动交易生命周期
 */
@Component
@ConditionalOnProperty(name = "demo.lifecycle.enabled", havingValue = "true", matchIfMissing = false)
@Order(2)
public class LifecycleDemo implements CommandLineRunner {

    @Autowired
    private SocialMediaCrawlerFactory crawlerFactory;
    @Autowired
    private DataAcquisitionService dataAcquisitionService;
    @Autowired
    private HotsearchProcessor hotsearchProcessor;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private HeatTrendTracker heatTrendTracker;
    @Autowired
    private MarketReactionEvaluator marketReactionEvaluator;
    @Autowired
    private PostTradeAnalyzer postTradeAnalyzer;
    @Autowired
    private AgentFactory agentFactory;
    @Autowired
    private HotsearchLifecycleLogService lifecycleLogService;

    @Override
    public void run(String... args) {
        System.out.println("=== 热搜驱动交易系统生命周期演示 ===");
        
        // 模拟热搜ID
        Long hotSearchId = 1001L;
        
        // 1. 热搜发现阶段
        Long discoveryLogId = lifecycleLogService.logStageStart(
            hotSearchId, 
            LifecycleStage.DISCOVERED,
            "WeiboAgent", 
            "从微博平台发现新热搜话题", 
            Map.of("keyword", "AI概念股", "heat", 8500, "source", "weibo")
        );
        
        // 模拟处理延时
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        lifecycleLogService.logStageSuccess(
            discoveryLogId,
            "成功发现热搜话题，初步评估热度较高",
            Map.of("validated", true, "initialScore", 85),
            null, 85.0, 0.9
        );

        // 2. 分析阶段
        Long analysisLogId = lifecycleLogService.logStageStart(
            hotSearchId,
            LifecycleStage.ANALYZING,
            "AnalysisAgent",
            "对热搜内容进行深度分析",
            Map.of("title", "AI概念股大涨", "content", "多只AI概念股涨停..."),
            discoveryLogId
        );

        lifecycleLogService.logStageProcessing(
            analysisLogId,
            "正在进行情感分析和传播速度计算",
            Map.of("sentiment", "positive", "propagationSpeed", 1.2)
        );

        try { Thread.sleep(200); } catch (InterruptedException e) {}

        lifecycleLogService.logStageSuccess(
            analysisLogId,
            "分析完成，情感偏向积极，传播速度较快",
            Map.of("sentimentScore", 0.8, "propagationScore", 0.75),
            85.0, 92.5, 0.85
        );

        // 3. 股票匹配阶段
        Long matchingLogId = lifecycleLogService.logStageStart(
            hotSearchId,
            LifecycleStage.STOCK_MATCHED,
            "StockMatchingAgent",
            "匹配相关股票",
            Map.of("keywords", List.of("AI", "人工智能", "概念股"))
        );

        Map<String, Object> relatedStocks = Map.of(
            "stocks", List.of(
                Map.of("code", "000001", "name", "平安银行", "relevance", 0.8),
                Map.of("code", "300750", "name", "宁德时代", "relevance", 0.9)
            )
        );

        lifecycleLogService.logStageSuccess(
            matchingLogId,
            "成功匹配到2只相关股票",
            relatedStocks,
            92.5, 95.0, 0.88
        );

        // 4. 决策阶段
        Long decisionLogId = lifecycleLogService.logStageStart(
            hotSearchId,
            LifecycleStage.DECISION_MADE,
            "DecisionAgent",
            "制定交易决策",
            Map.of("totalScore", 95.0, "threshold", 90.0)
        );

        // 记录详细决策信息
        lifecycleLogService.logDecisionDetails(
            decisionLogId,
            Map.of("scoreWeight", 0.4, "marketTrend", 0.3, "riskTolerance", 0.3),
            Map.of("maxLoss", 0.05, "riskLevel", "MEDIUM", "stopLoss", 0.03),
            Map.of("marketStatus", "BULL", "volatility", 0.15, "volume", "HIGH"),
            relatedStocks
        );

        lifecycleLogService.logStageSuccess(
            decisionLogId,
            "决策完成，建议买入宁德时代",
            Map.of("action", "BUY", "stockCode", "300750", "quantity", 100),
            95.0, 95.0, 0.92
        );

        System.out.println("热搜生命周期日志记录完成！");
        System.out.println("发现阶段日志ID: " + discoveryLogId);
        System.out.println("分析阶段日志ID: " + analysisLogId);
        System.out.println("匹配阶段日志ID: " + matchingLogId);
        System.out.println("决策阶段日志ID: " + decisionLogId);
    }
} 