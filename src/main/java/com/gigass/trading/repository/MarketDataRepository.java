package com.gigass.trading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * 市场数据存储库
 * 用于存储和查询市场相关数据
 */
@Repository
public interface MarketDataRepository extends JpaRepository<Object, Long> {
    
    /**
     * 根据股票代码查询市场数据
     * @param stockCode 股票代码
     * @return 市场数据列表
     */
    List<Object> findByStockCode(String stockCode);
    
    /**
     * 根据股票代码和日期范围查询市场数据
     * @param stockCode 股票代码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 符合条件的市场数据列表
     */
    @Query("SELECT m FROM MarketData m WHERE m.stockCode = :stockCode AND m.date BETWEEN :startDate AND :endDate")
    List<Object> findByStockCodeAndDateRange(String stockCode, LocalDate startDate, LocalDate endDate);
    
    /**
     * 查询指定日期的所有市场数据
     * @param date 日期
     * @return 指定日期的市场数据列表
     */
    List<Object> findByDate(LocalDate date);
    
    /**
     * 查询特定行业的市场数据
     * @param industry 行业
     * @param date 日期
     * @return 特定行业在指定日期的市场数据
     */
    @Query("SELECT m FROM MarketData m WHERE m.industry = :industry AND m.date = :date")
    List<Object> findByIndustryAndDate(String industry, LocalDate date);
} 