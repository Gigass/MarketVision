package com.gigass.trading.demo;

import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.module.acquisition.SocialMediaCrawlerFactory;
import com.gigass.trading.module.base.HotsearchProcessor;
import com.gigass.trading.module.correlation.StockMatcher;
import com.gigass.trading.module.correlation.RiskAssessmentService;
import com.gigass.trading.module.execution.OrderManager;
import com.gigass.trading.module.monitoring.HeatTrendTracker;
import com.gigass.trading.module.monitoring.MarketReactionEvaluator;
import com.gigass.trading.module.feedback.PostTradeAnalyzer;
import com.gigass.trading.service.DataAcquisitionService;
import com.gigass.trading.module.signal.HeatBurstDetector;
import com.gigass.trading.module.scoring.MultiDimensionalScorer;
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
    private HeatBurstDetector heatBurstDetector;
    @Autowired
    private MultiDimensionalScorer multiDimensionalScorer;
    @Autowired
    private StockMatcher stockMatcher;
    @Autowired
    private RiskAssessmentService riskAssessmentService;
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private HeatTrendTracker heatTrendTracker;
    @Autowired
    private MarketReactionEvaluator marketReactionEvaluator;
    @Autowired
    private PostTradeAnalyzer postTradeAnalyzer;

    @Override
    public void run(String... args) {
        System.out.println("=== 热搜驱动交易系统生命周期演示 ===");
        // 1. 热搜采集
        Object weiboCrawler = crawlerFactory.createCrawler("weibo");
        Object weiboData = dataAcquisitionService.acquireData(weiboCrawler);
        Object processedData = hotsearchProcessor.processHotsearchData(weiboData);
        List<HotSearchRecord> hotSearchRecords = processedData instanceof List ? (List<HotSearchRecord>) processedData : Collections.emptyList();
        System.out.println("采集到热搜条数: " + hotSearchRecords.size());

        // 2. 信号筛选
        for (HotSearchRecord record : hotSearchRecords) {
            int heatScore = heatBurstDetector.detectHeatBurst(record);
            int totalScore = multiDimensionalScorer.calculateTotalScore(Collections.singletonMap("heat", heatScore));
            if (totalScore < 50) continue; // 简化：只处理高分热搜

            // 3. 关联分析
            List<Object> relatedStocks = stockMatcher.matchRelatedStocks(record.getKeyword());
            if (relatedStocks == null || relatedStocks.isEmpty()) continue;

            // 4. 风险评估
            Object bestStock = relatedStocks.get(0); // 简化：取第一个
            int riskScore = riskAssessmentService.calculateRiskScore(bestStock.toString(), record.getKeyword());
            if (riskScore > 80) continue; // 风险过高跳过

            // 5. 交易决策与下单
            Object order = orderManager.createOrder(bestStock.toString(), "BUY", 100, 10.0);
            orderManager.submitOrder(order);
            System.out.println("已下单: " + bestStock);

            // 6. 实时监控
            heatTrendTracker.trackHeatTrend(record.getKeyword());
            marketReactionEvaluator.evaluateMarketReaction(record.getKeyword(), bestStock.toString());

            // 7. 反馈优化
            postTradeAnalyzer.analyzeTradePerformance(order.toString());
        }
        System.out.println("=== 生命周期演示结束 ===");
    }
} 