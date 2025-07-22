package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@TableName("market_data")
public class MarketData {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("stock_code")
    private String stockCode;

    @TableField("date")
    private LocalDate date;

    @TableField("industry")
    private String industry;

    @TableField("open_price")
    private BigDecimal openPrice;

    @TableField("close_price")
    private BigDecimal closePrice;

    @TableField("high_price")
    private BigDecimal highPrice;

    @TableField("low_price")
    private BigDecimal lowPrice;

    @TableField("volume")
    private Long volume;
} 