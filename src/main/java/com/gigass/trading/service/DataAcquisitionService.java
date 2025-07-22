package com.gigass.trading.service;

import com.gigass.trading.dto.HotSearchData;
import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.module.acquisition.SocialMediaCrawlerFactory;
import com.gigass.trading.module.acquisition.SocialMediaCrawler;
import com.gigass.trading.repository.HotSearchRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataAcquisitionService {
    
    private static final Logger logger = LoggerFactory.getLogger(DataAcquisitionService.class);
    
    @Autowired
    private SocialMediaCrawlerFactory crawlerFactory;
    
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;
    
    /**
     * 定时数据采集任务
     */
    @Scheduled(fixedDelayString = "${trading.system.rss.fetch-interval:60000}")
    public void scheduledDataCollection() {
        logger.info("开始定时数据采集任务");
        
        try {
            Map<String, SocialMediaCrawler> crawlers = crawlerFactory.getAllCrawlers();
            
            for (Map.Entry<String, SocialMediaCrawler> entry : crawlers.entrySet()) {
                String platform = entry.getKey();
                SocialMediaCrawler crawler = entry.getValue();
                
                if (crawler.isAvailable()) {
                    collectDataFromPlatform(platform, crawler);
                } else {
                    logger.warn("平台 {} 当前不可用，跳过采集", platform);
                }
            }
            
        } catch (Exception e) {
            logger.error("定时数据采集任务失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 从指定平台采集数据
     */
    public void collectDataFromPlatform(String platform, SocialMediaCrawler crawler) {
        logger.info("开始从平台 {} 采集数据", platform);
        
        try {
            List<HotSearchData> dataList = crawler.fetchHotSearchData(20);
            
            if (dataList != null && !dataList.isEmpty()) {
                List<HotSearchRecord> records = dataList.stream()
                        .map(this::convertToRecord)
                        .toList();
                hotSearchRecordRepository.insertBatch(records);
            }
            
            logger.info("平台 {} 数据采集完成，共采集 {} 条数据", platform, dataList.size());
            
        } catch (Exception e) {
            logger.error("平台 {} 数据采集失败: {}", platform, e.getMessage(), e);
        }
    }
    
    /**
     * 转换数据格式
     */
    private HotSearchRecord convertToRecord(HotSearchData data) {
        HotSearchRecord record = new HotSearchRecord();
        record.setSource(data.getSource());
        record.setTitle(data.getTitle());
        record.setContent(data.getContent());
        record.setHeatScore(data.getHeatIndex());
        record.setStatus(HotSearchRecord.ProcessStatus.PENDING);
        
        return record;
    }
    
    public Object acquireData(Object crawler) {
        // TODO: Implement data acquisition logic using the provided crawler
        System.out.println("Acquiring data...");
        return new java.util.ArrayList<HotSearchRecord>();
    }

    /**
     * 手动触发数据采集
     */
    public void manualDataCollection(String platform) {
        logger.info("手动触发平台 {} 数据采集", platform);
        
        try {
            SocialMediaCrawler crawler = crawlerFactory.getCrawler(platform);
            collectDataFromPlatform(platform, crawler);
        } catch (Exception e) {
            logger.error("手动数据采集失败: {}", e.getMessage(), e);
            throw new RuntimeException("数据采集失败: " + e.getMessage());
        }
    }
}