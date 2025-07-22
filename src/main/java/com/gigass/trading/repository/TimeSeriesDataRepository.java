package com.gigass.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 时序数据存储库
 * 用于存储和查询时间序列数据
 */
@Repository
public interface TimeSeriesDataRepository extends JpaRepository<Object, Long> {
    
    /**
     * 根据时间范围查询数据
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间范围内的数据列表
     */
    @Query("SELECT t FROM TimeSeriesData t WHERE t.timestamp BETWEEN :startTime AND :endTime")
    List<Object> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据数据类型和时间范围查询数据
     * @param dataType 数据类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 符合条件的数据列表
     */
    @Query("SELECT t FROM TimeSeriesData t WHERE t.dataType = :dataType AND t.timestamp BETWEEN :startTime AND :endTime")
    List<Object> findByDataTypeAndTimeRange(String dataType, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取最新的N条数据
     * @param dataType 数据类型
     * @param limit 限制数量
     * @return 最新的数据列表
     */
    @Query(value = "SELECT * FROM time_series_data WHERE data_type = :dataType ORDER BY timestamp DESC LIMIT :limit", nativeQuery = true)
    List<Object> findLatestByDataType(String dataType, int limit);
} 