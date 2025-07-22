package com.gigass.trading.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_info")
public class StockInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "stock_code", nullable = false, unique = true)
    private String stockCode; // 股票代码
    
    @Column(name = "stock_name", nullable = false)
    private String stockName; // 股票名称
    
    @Column(name = "market")
    private String market; // 市场：SH, SZ
    
    @Column(name = "industry")
    private String industry; // 行业
    
    @Column(name = "concept", columnDefinition = "TEXT")
    private String concept; // 概念，JSON格式存储
    
    @Column(name = "current_price", precision = 10, scale = 2)
    private BigDecimal currentPrice; // 当前价格
    
    @Column(name = "market_cap", precision = 15, scale = 2)
    private BigDecimal marketCap; // 市值
    
    @Column(name = "is_active")
    private Boolean isActive = true; // 是否活跃
    
    @Column(name = "created_time")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    
    public StockInfo() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // TODO: 添加完整的getter/setter方法
}