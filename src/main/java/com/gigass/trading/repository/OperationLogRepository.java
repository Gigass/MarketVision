package com.gigass.trading.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.OperationLog;

/**
 * 操作日志存储库
 * 用于操作日志的CRUD操作
 */
public interface OperationLogRepository extends BaseMapper<OperationLog> {
    // 复杂的查询可以在对应的XML文件中实现
} 