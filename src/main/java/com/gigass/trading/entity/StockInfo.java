package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("stock_info")
public class StockInfo {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("stock_code")
    private String stockCode; // 股票代码
    
    @TableField("stock_name")
    private String stockName; // 股票名称
    
    @TableField("market")
    private String market; // 市场：SH, SZ
    
    @TableField("industry")
    private String industry; // 行业
    
    @TableField("concept")
    private String concept; // 概念，JSON格式存储
    
    @TableField("current_price")
    private BigDecimal currentPrice; // 当前价格
    
    @TableField("market_cap")
    private BigDecimal marketCap; // 市值
    
    @TableField("is_active")
    private Boolean isActive = true; // 是否活跃
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    @TableField("updated_time")
    private LocalDateTime updatedTime;
    
    public StockInfo() {
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    // TODO: 添加完整的getter/setter方法
}