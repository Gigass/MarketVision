package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("time_series_data")
public class TimeSeriesData {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("data_type")
    private String dataType;

    @TableField("timestamp")
    private LocalDateTime timestamp;

    @TableField("value")
    private String value; // 假设是JSON格式的数据
} 