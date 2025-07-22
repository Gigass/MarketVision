package com.gigass.trading.module.acquisition;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;

@Component
public class SocialMediaCrawlerFactory {
    
    private final Map<String, SocialMediaCrawler> crawlers = new ConcurrentHashMap<>();
    
    @Value("${trading.system.rss.sources}")
    private List<RssSourceConfig> rssSources;
    
    public SocialMediaCrawlerFactory() {
        // 初始化将在@PostConstruct中完成
    }
    
    @jakarta.annotation.PostConstruct
    public void initializeCrawlers() {
        if (rssSources != null) {
            for (RssSourceConfig source : rssSources) {
                if (source.isEnabled()) {
                    UniversalRssCrawler crawler = new UniversalRssCrawler(
                        source.getUrl(), source.getName());
                    crawlers.put(source.getName().toLowerCase(), crawler);
                }
            }
        }
    }
    
    public SocialMediaCrawler getCrawler(String platform) {
        SocialMediaCrawler crawler = crawlers.get(platform.toLowerCase());
        if (crawler == null) {
            throw new IllegalArgumentException("不支持的平台: " + platform);
        }
        return crawler;
    }
    
    public void registerCrawler(String platform, SocialMediaCrawler crawler) {
        crawlers.put(platform.toLowerCase(), crawler);
    }
    
    public Map<String, SocialMediaCrawler> getAllCrawlers() {
        return new ConcurrentHashMap<>(crawlers);
    }
    
    // RSS源配置内部类
    public static class RssSourceConfig {
        private String name;
        private String url;
        private boolean enabled = true;
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }
}