package com.jolywood.trading.module.acquisition;

import com.jolywood.trading.dto.HotSearchData;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UniversalRssCrawler implements SocialMediaCrawler {
    
    private static final Logger logger = LoggerFactory.getLogger(UniversalRssCrawler.class);
    
    private final WebClient webClient;
    private String dataSourceUrl;
    private String platformName;
    
    public UniversalRssCrawler() {
        this.webClient = WebClient.builder().build();
    }
    
    public UniversalRssCrawler(String dataSourceUrl, String platformName) {
        this();
        this.dataSourceUrl = dataSourceUrl;
        this.platformName = platformName;
    }
    
    @Override
    public List<HotSearchData> fetchHotSearchData(int limit) {
        logger.info("开始获取{}热搜数据，限制数量: {}", platformName, limit);
        
        List<HotSearchData> result = new ArrayList<>();
        
        try {
            // 发送HTTP请求获取RSS数据
            String rssContent = webClient.get()
                .uri(dataSourceUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
            
            if (rssContent == null || rssContent.isEmpty()) {
                logger.warn("RSS内容为空: {}", dataSourceUrl);
                return result;
            }
            
            // 解析RSS XML数据
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(new java.io.StringReader(rssContent)));
            
            List<SyndEntry> entries = feed.getEntries();
            int count = Math.min(entries.size(), limit);
            
            // 转换为HotSearchData对象
            for (int i = 0; i < count; i++) {
                SyndEntry entry = entries.get(i);
                HotSearchData data = convertToHotSearchData(entry);
                result.add(data);
            }
            
            logger.info("成功获取{}热搜数据 {} 条", platformName, result.size());
            
        } catch (Exception e) {
            logger.error("获取{}热搜数据失败: {}", platformName, e.getMessage(), e);
        }
        
        return result;
    }
    
    private HotSearchData convertToHotSearchData(SyndEntry entry) {
        HotSearchData data = new HotSearchData();
        data.setSource(platformName);
        data.setTitle(entry.getTitle());
        data.setContent(entry.getDescription() != null ? entry.getDescription().getValue() : "");
        data.setUrl(entry.getLink());
        
        // 转换发布时间
        Date publishedDate = entry.getPublishedDate();
        if (publishedDate != null) {
            data.setPublishTime(publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime());
        }
        
        // TODO: 从RSS中提取热度指数（如果有的话）
        data.setHeatIndex(0);
        
        return data;
    }
    
    @Override
    public String getPlatformName() {
        return platformName;
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // 简单的可用性检查：发送HEAD请求
            webClient.head()
                .uri(dataSourceUrl)
                .retrieve()
                .toBodilessEntity()
                .block();
            return true;
        } catch (Exception e) {
            logger.warn("数据源不可用: {} - {}", dataSourceUrl, e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getDataSourceUrl() {
        return dataSourceUrl;
    }
    
    public void setDataSourceUrl(String dataSourceUrl) {
        this.dataSourceUrl = dataSourceUrl;
    }
    
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}