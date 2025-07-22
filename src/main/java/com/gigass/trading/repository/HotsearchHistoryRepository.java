package com.gigass.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 热搜历史数据存储库
 * 用于存储和查询历史热搜数据
 */
@Repository
public interface HotsearchHistoryRepository extends JpaRepository<Object, Long> {
    
    /**
     * 根据关键词查询热搜历史
     * @param keyword 关键词
     * @return 包含关键词的热搜历史列表
     */
    List<Object> findByKeywordContaining(String keyword);
    
    /**
     * 根据时间范围查询热搜历史
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 时间范围内的热搜历史列表
     */
    @Query("SELECT h FROM HotsearchHistory h WHERE h.timestamp BETWEEN :startTime AND :endTime")
    List<Object> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查询热度超过阈值的热搜历史
     * @param threshold 热度阈值
     * @return 热度超过阈值的热搜历史列表
     */
    @Query("SELECT h FROM HotsearchHistory h WHERE h.heat > :threshold")
    List<Object> findByHeatAbove(int threshold);
    
    /**
     * 查询与特定股票相关的热搜历史
     * @param stockCode 股票代码
     * @return 与特定股票相关的热搜历史列表
     */
    @Query("SELECT h FROM HotsearchHistory h WHERE h.relatedStocks LIKE %:stockCode%")
    List<Object> findByRelatedStock(String stockCode);
} 