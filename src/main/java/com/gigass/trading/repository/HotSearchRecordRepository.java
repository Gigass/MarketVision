package com.gigass.trading.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.HotSearchRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HotSearchRecordRepository extends BaseMapper<HotSearchRecord> {
    void insertBatch(@Param("records") List<HotSearchRecord> records);
    // 复杂的查询可以在对应的XML文件中实现
}