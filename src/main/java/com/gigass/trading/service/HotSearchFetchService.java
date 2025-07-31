package com.gigass.trading.service;

import com.gigass.trading.dto.HotSearchResponse;
import com.gigass.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@Service
public class HotSearchFetchService {
    
    private static final Logger logger = LoggerFactory.getLogger(HotSearchFetchService.class);
    
    @Value("${hotsearch.crawler.base-url}")
    private String baseUrl;
    
    @Value("${hotsearch.crawler.platforms}")
    private List<String> platforms;
    
    @Value("${hotsearch.crawler.cache}")
    private Boolean cache;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    /**
     * 获取指定平台的热搜数据
     */
    public HotSearchResponse fetchHotSearchData(String platform) {
        String url = String.format("%s/%s?cache=%s", baseUrl, platform, cache);
        
        try {
            logger.info("开始获取{}平台热搜数据: {}", platform, url);
            HotSearchResponse response = restTemplate.getForObject(url, HotSearchResponse.class);
            
            if (response != null && response.getCode() == 200) {
                logger.info("成功获取{}平台热搜数据，共{}条", platform, response.getTotal());
                return response;
            } else {
                logger.warn("获取{}平台热搜数据失败，响应码: {}", platform, 
                           response != null ? response.getCode() : "null");
                return null;
            }
        } catch (Exception e) {
            logger.error("获取{}平台热搜数据异常: {}", platform, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * 获取所有平台的热搜数据
     */
    public void fetchAllPlatformsData() {
        logger.info("开始获取所有平台热搜数据，平台列表: {}", platforms);
        
        for (String platform : platforms) {
            HotSearchResponse response = fetchHotSearchData(platform);
            if (response != null) {
                // 这里可以进一步处理数据，比如保存到数据库
                processHotSearchData(platform, response);
            }
        }
    }
    
    /**
     * 处理热搜数据
     */
    private void processHotSearchData(String platform, HotSearchResponse response) {
        // TODO: 将数据转换为HotSearchRecord并保存
        logger.info("处理{}平台热搜数据完成", platform);
    }
    
    /**
     * 批量获取所有平台热搜数据并转换为HotSearchRecord
     */
    public List<HotSearchRecord> fetchAllPlatformsAsRecords() {
        logger.info("开始批量获取所有平台热搜数据，平台列表: {}", platforms);
        
        List<HotSearchRecord> allRecords = new ArrayList<>();
        
        for (String platform : platforms) {
            HotSearchResponse response = fetchHotSearchData(platform);
            if (response != null && response.getData() != null) {
                List<HotSearchRecord> records = convertToHotSearchRecords(response, platform);
                allRecords.addAll(records);
                logger.info("平台{}转换完成，获得{}条记录", platform, records.size());
            }
        }
        
        logger.info("所有平台热搜数据获取完成，共{}条记录", allRecords.size());
        return allRecords;
    }
    
    /**
     * 将HotSearchResponse转换为HotSearchRecord列表
     */
    private List<HotSearchRecord> convertToHotSearchRecords(HotSearchResponse response, String platform) {
        return response.getData().stream()
                .map(item -> {
                    HotSearchRecord record = new HotSearchRecord();
                    record.setSource(platform);
                    record.setTitle(item.getTitle());
                    record.setContent(item.getTitle()); // 使用title作为content
                    record.setHeatScore(item.getHot().intValue());
                    record.setStatus(HotSearchRecord.ProcessStatus.PENDING);
                    record.setCreatedTime(LocalDateTime.now());
                    return record;
                })
                .collect(Collectors.toList());
    }
}
