package com.gigass.trading.dto;

import java.time.LocalDateTime;

public class HotSearchData {
    
    private String source; // 数据源
    private String title; // 标题
    private String content; // 内容
    private String url; // 原始链接
    private Integer heatIndex; // 热度指数
    private LocalDateTime publishTime; // 发布时间
    private LocalDateTime fetchTime; // 抓取时间
    
    // 构造函数
    public HotSearchData() {
        this.fetchTime = LocalDateTime.now();
    }
    
    public HotSearchData(String source, String title, String content) {
        this();
        this.source = source;
        this.title = title;
        this.content = content;
    }
    
    // TODO: 添加完整的getter/setter方法
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    
    public Integer getHeatIndex() { return heatIndex; }
    public void setHeatIndex(Integer heatIndex) { this.heatIndex = heatIndex; }
    
    public LocalDateTime getPublishTime() { return publishTime; }
    public void setPublishTime(LocalDateTime publishTime) { this.publishTime = publishTime; }
    
    public LocalDateTime getFetchTime() { return fetchTime; }
    public void setFetchTime(LocalDateTime fetchTime) { this.fetchTime = fetchTime; }
}