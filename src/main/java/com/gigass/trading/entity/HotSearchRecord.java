package com.gigass.trading.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hot_search_records")
public class HotSearchRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String source; // 数据源：douyin, weibo等
    
    @Column(nullable = false, length = 500)
    private String title; // 热搜标题
    
    @Column(columnDefinition = "TEXT")
    private String content; // 热搜内容
    
    @Column(name = "heat_score")
    private Integer heatScore; // 热度分数
    
    @Column(name = "sentiment_score")
    private Double sentimentScore; // 情感分数
    
    @Column(name = "propagation_speed")
    private Double propagationSpeed; // 传播速度
    
    @Column(name = "verification_score")
    private Double verificationScore; // 验证分数
    
    @Column(name = "total_score")
    private Double totalScore; // 总分
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    @Enumerated(EnumType.STRING)
    private ProcessStatus status; // 处理状态
    
    // 构造函数、getter、setter
    public HotSearchRecord() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
        this.status = ProcessStatus.PENDING;
    }

    public String getKeyword() {
        return this.title;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHeatScore(Integer heatScore) {
        this.heatScore = heatScore;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setSentimentScore(Double sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public void setPropagationSpeed(Double propagationSpeed) {
        this.propagationSpeed = propagationSpeed;
    }

    public void setVerificationScore(Double verificationScore) {
        this.verificationScore = verificationScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    // TODO: 添加完整的getter/setter方法
    
    public enum ProcessStatus {
        PENDING, PROCESSING, FILTERED, ANALYZED, COMPLETED, FAILED
    }
}