package com.gigass.trading.scheduler;

import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.module.acquisition.SocialMediaCrawlerFactory;
import com.gigass.trading.module.base.HotsearchProcessor;
import com.gigass.trading.repository.HotSearchRecordRepository;
import com.gigass.trading.service.DataAcquisitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 热搜数据采集定时任务
 * 定期从各个平台爬取热搜数据
 */
@Component
public class HotSearchCrawlTask {
    
    private static final Logger logger = LoggerFactory.getLogger(HotSearchCrawlTask.class);
    
    @Autowired
    private SocialMediaCrawlerFactory crawlerFactory;
    
    @Autowired
    private HotsearchProcessor hotsearchProcessor;
    
    @Autowired
    private DataAcquisitionService dataAcquisitionService;
    
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;
    
    /**
     * 每10分钟执行一次热搜数据采集
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void crawlHotSearchData() {
        logger.info("开始执行热搜数据采集任务，时间: {}", LocalDateTime.now());
        
        try {
            // 获取微博热搜数据
            Object weiboCrawler = crawlerFactory.createCrawler("weibo");
            Object weiboData = dataAcquisitionService.acquireData(weiboCrawler);
            List<HotSearchRecord> weiboRecords = processHotSearchData(weiboData, "weibo");
            
            // 获取百度热搜数据
            Object baiduCrawler = crawlerFactory.createCrawler("baidu");
            Object baiduData = dataAcquisitionService.acquireData(baiduCrawler);
            List<HotSearchRecord> baiduRecords = processHotSearchData(baiduData, "baidu");
            
            // 获取知乎热搜数据
            Object zhihuCrawler = crawlerFactory.createCrawler("zhihu");
            Object zhihuData = dataAcquisitionService.acquireData(zhihuCrawler);
            List<HotSearchRecord> zhihuRecords = processHotSearchData(zhihuData, "zhihu");
            
            // 保存所有热搜记录
            saveHotSearchRecords(weiboRecords);
            saveHotSearchRecords(baiduRecords);
            saveHotSearchRecords(zhihuRecords);
            
            logger.info("热搜数据采集任务执行完成，微博: {}条，百度: {}条，知乎: {}条", 
                    weiboRecords.size(), baiduRecords.size(), zhihuRecords.size());
        } catch (Exception e) {
            logger.error("热搜数据采集任务执行失败", e);
        }
    }
    
    /**
     * 处理热搜数据
     * @param rawData 原始数据
     * @param platform 平台名称
     * @return 热搜记录列表
     */
    @SuppressWarnings("unchecked")
    private List<HotSearchRecord> processHotSearchData(Object rawData, String platform) {
        // 使用热搜处理器处理数据
        Object processedData = hotsearchProcessor.processHotsearchData(rawData);
        
        // 这里假设处理后的数据可以转换为HotSearchRecord列表
        // 实际实现中需要根据具体的数据结构进行转换
        return (List<HotSearchRecord>) processedData;
    }
    
    /**
     * 保存热搜记录
     * @param records 热搜记录列表
     */
    private void saveHotSearchRecords(List<HotSearchRecord> records) {
        if (records != null && !records.isEmpty()) {
//            hotSearchRecordRepository.saveAll(records);
        }
    }
} 