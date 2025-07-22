package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hotsearch_history")
public class HotsearchHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("keyword")
    private String keyword;

    @TableField("timestamp")
    private LocalDateTime timestamp;

    @TableField("heat")
    private Integer heat;

    @TableField("related_stocks")
    private String relatedStocks; // JSON格式存储关联股票
} 