package com.gigass.trading.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.MarketData;

/**
 * 市场数据存储库
 * 用于存储和查询市场相关数据
 */
public interface MarketDataRepository extends BaseMapper<MarketData> {
    // 复杂的查询可以在对应的XML文件中实现
} 