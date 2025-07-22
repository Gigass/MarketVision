package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

@TableName("hot_search_records")
public class HotSearchRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("source")
    private String source; // 数据源：douyin, weibo等
    
    @TableField("title")
    private String title; // 热搜标题
    
    @TableField("content")
    private String content; // 热搜内容
    
    @TableField("heat_score")
    private Integer heatScore; // 热度分数
    
    @TableField("sentiment_score")
    private Double sentimentScore; // 情感分数
    
    @TableField("propagation_speed")
    private Double propagationSpeed; // 传播速度
    
    @TableField("verification_score")
    private Double verificationScore; // 验证分数
    
    @TableField("total_score")
    private Double totalScore; // 总分
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    @TableField("status")
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