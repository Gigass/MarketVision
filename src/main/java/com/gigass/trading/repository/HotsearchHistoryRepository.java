package com.gigass.trading.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.HotsearchHistory;

/**
 * 热搜历史数据存储库
 * 用于存储和查询历史热搜数据
 */
public interface HotsearchHistoryRepository extends BaseMapper<HotsearchHistory> {
    // 复杂的查询可以在对应的XML文件中实现
} 