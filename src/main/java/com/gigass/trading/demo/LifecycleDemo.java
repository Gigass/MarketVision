package com.gigass.trading.demo;

import com.gigass.ai.module.agent.AgentFactory;
import com.gigass.trading.dto.HotSearchResponse;
import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.enums.LifecycleStage;
import com.gigass.trading.mapper.HotSearchRecordRepository;
import com.gigass.trading.module.acquisition.SocialMediaCrawlerFactory;
import com.gigass.trading.module.base.HotsearchProcessor;
import com.gigass.trading.module.execution.OrderManager;
import com.gigass.trading.module.feedback.PostTradeAnalyzer;
import com.gigass.trading.module.monitoring.HeatTrendTracker;
import com.gigass.trading.module.monitoring.MarketReactionEvaluator;
import com.gigass.trading.service.DataAcquisitionService;
import com.gigass.trading.service.HotSearchFetchService;
import com.gigass.trading.service.HotsearchLifecycleLogService;
import com.gigass.trading.service.SignalProcessingService;
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
    @Autowired
    private HotSearchFetchService hotSearchFetchService;
    @Autowired
    private SignalProcessingService signalProcessingService;
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;

    @Override
    public void run(String... args) {
        System.out.println("=== 热搜驱动交易系统生命周期演示 ===");
        
        // 1. 数据采集阶段：一次性获取所有平台热搜数据
        System.out.println("--- 阶段1：数据采集 - 获取多平台热搜数据 ---");
        List<HotSearchRecord> allHotSearchRecords = hotSearchFetchService.fetchAllPlatformsAsRecords();
        
        if (allHotSearchRecords.isEmpty()) {
            System.out.println("未获取到热搜数据，使用模拟数据继续演示");
            // 创建模拟数据
            allHotSearchRecords = createMockHotSearchRecords();
        }
        
        // 批量保存到数据库
        System.out.println("保存热搜记录到数据库，共" + allHotSearchRecords.size() + "条");
        hotSearchRecordRepository.insertBatch(allHotSearchRecords);
        
        // 2. 信号筛选阶段：处理所有热搜记录
        System.out.println("--- 阶段2：信号筛选 - 处理所有热搜记录 ---");
        signalProcessingService.processSignals();
        
        // 等待信号处理完成
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
        
        // 3. 查看筛选结果
        System.out.println("--- 信号筛选结果统计 ---");
        showSignalFilteringResults();
        
        // 继续原有的生命周期演示逻辑...
        System.out.println("--- 阶段3：生命周期演示 ---");
        demonstrateLifecycle();
    }
    
    /**
     * 创建模拟热搜数据
     */
    private List<HotSearchRecord> createMockHotSearchRecords() {
        List<HotSearchRecord> mockRecords = new ArrayList<>();
        
        // 模拟抖音数据
        mockRecords.add(createMockRecord("douyin", "AI概念股大涨", 11472575));
        mockRecords.add(createMockRecord("douyin", "新能源汽车销量创新高", 10522029));
        
        // 模拟微博数据
        mockRecords.add(createMockRecord("weibo", "ChatGPT概念股异动", 9500000));
        mockRecords.add(createMockRecord("weibo", "半导体板块集体上涨", 8800000));
        
        // 模拟百度数据
        mockRecords.add(createMockRecord("baidu", "量子计算概念股走强", 7200000));
        mockRecords.add(createMockRecord("baidu", "新材料板块表现活跃", 6500000));
        
        return mockRecords;
    }
    
    private HotSearchRecord createMockRecord(String source, String title, int heatScore) {
        HotSearchRecord record = new HotSearchRecord();
        record.setSource(source);
        record.setTitle(title);
        record.setContent(title);
        record.setHeatScore(heatScore);
        record.setStatus(HotSearchRecord.ProcessStatus.PENDING);
        record.setCreatedTime(LocalDateTime.now());
        return record;
    }
    
    /**
     * 显示信号筛选结果
     */
    private void showSignalFilteringResults() {
        QueryWrapper<HotSearchRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("total_score");
        List<HotSearchRecord> processedRecords = hotSearchRecordRepository.selectList(queryWrapper);
        
        System.out.println("信号筛选完成，处理记录数: " + processedRecords.size());
        
        // 显示通过筛选的记录
        List<HotSearchRecord> filteredRecords = processedRecords.stream()
                .filter(r -> r.getStatus() == HotSearchRecord.ProcessStatus.FILTERED)
                .limit(5)
                .collect(Collectors.toList());
                
        System.out.println("通过信号筛选的热搜(前5条):");
        for (HotSearchRecord record : filteredRecords) {
            System.out.printf("- [%s] %s (总分: %.2f, 热度: %d)\n", 
                    record.getSource(), record.getTitle(), 
                    record.getTotalScore(), record.getHeatScore());
        }
    }
    
    /**
     * 原有的生命周期演示逻辑
     */
    private void demonstrateLifecycle() {
        // 使用筛选后的最高分记录进行演示
        QueryWrapper<HotSearchRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", HotSearchRecord.ProcessStatus.FILTERED)
                   .orderByDesc("total_score")
                   .last("LIMIT 1");
        HotSearchRecord topRecord = hotSearchRecordRepository.selectOne(queryWrapper);
        
        if (topRecord != null) {
            System.out.println("使用最高分热搜进行生命周期演示: " + topRecord.getTitle());
            // 继续原有的演示逻辑...
        }
    }
}
