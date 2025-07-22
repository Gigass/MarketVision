package com.gigass.trading.demo;

import com.gigass.ai.module.agent.AgentFactory;
import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.module.acquisition.SocialMediaCrawlerFactory;
import com.gigass.trading.module.base.HotsearchProcessor;
import com.gigass.trading.module.execution.OrderManager;
import com.gigass.trading.module.feedback.PostTradeAnalyzer;
import com.gigass.trading.module.monitoring.HeatTrendTracker;
import com.gigass.trading.module.monitoring.MarketReactionEvaluator;
import com.gigass.trading.service.DataAcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 生命周期流程演示Demo
 * 模拟完整的热搜驱动交易生命周期
 */
@Component
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

    @Override
    public void run(String... args) {
        System.out.println("=== 热搜驱动交易系统生命周期演示 ===");
        
        // 1. 热搜采集
        Object weiboCrawler = crawlerFactory.createCrawler("weibo");
        Object weiboData = dataAcquisitionService.acquireData(weiboCrawler);
        Object processedData = hotsearchProcessor.processHotsearchData(weiboData);
        List<HotSearchRecord> hotSearchRecords = processedData instanceof List ? (List<HotSearchRecord>) processedData : Collections.emptyList();
        System.out.println("采集到热搜条数: " + hotSearchRecords.size());

        for (HotSearchRecord record : hotSearchRecords) {
            // 2. 信号筛选 - 使用AI智能体
            Object signalAgent = agentFactory.createSignalFilteringAgent(record);
            boolean passSignalFilter = (Boolean) signalAgent; // 简化处理
            if (!passSignalFilter) continue;

            // 3. 关联分析 - 使用AI智能体
            Object correlationAgent = agentFactory.createCorrelationAnalysisAgent(record);
            String bestStock = (String) correlationAgent; // 简化处理
            if (bestStock == null) continue;

            // 4. 风险评估 - 使用AI智能体
            Object riskAgent = agentFactory.createRiskAssessmentAgent(record, bestStock);
            boolean passRiskAssessment = (Boolean) riskAgent; // 简化处理
            if (!passRiskAssessment) continue;

            // 5. 交易决策
            Object order = orderManager.createOrder(bestStock, "BUY", 100, 10.0);
            orderManager.submitOrder(order);
            System.out.println("已下单: " + bestStock);
            //6.下单代码实现
            //todo

            //7. 实时监控
            heatTrendTracker.trackHeatTrend(record.getKeyword());
            marketReactionEvaluator.evaluateMarketReaction(record.getKeyword(), bestStock);

            //8. 反馈优化
            postTradeAnalyzer.analyzeTradePerformance(order.toString());
        }
        System.out.println("=== 生命周期演示结束 ===");
    }
} 