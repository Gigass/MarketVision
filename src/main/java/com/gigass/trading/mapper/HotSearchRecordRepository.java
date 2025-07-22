package com.gigass.trading.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gigass.trading.entity.HotSearchRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HotSearchRecordRepository extends BaseMapper<HotSearchRecord> {
    
    /**
     * 批量插入热搜记录
     */
    int insertBatch(List<HotSearchRecord> records);
    
    // 复杂的查询可以在对应的XML文件中实现
}