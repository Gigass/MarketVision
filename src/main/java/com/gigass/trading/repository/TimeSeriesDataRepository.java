package com.gigass.trading.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.TimeSeriesData;

/**
 * 时序数据存储库
 * 用于存储和查询时间序列数据
 */
public interface TimeSeriesDataRepository extends BaseMapper<TimeSeriesData> {
    // 复杂的查询可以在对应的XML文件中实现
} 