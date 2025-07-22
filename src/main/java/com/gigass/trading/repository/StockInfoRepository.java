package com.gigass.trading.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.StockInfo;

public interface StockInfoRepository extends BaseMapper<StockInfo> {
    // 复杂的查询可以在对应的XML文件中实现
}