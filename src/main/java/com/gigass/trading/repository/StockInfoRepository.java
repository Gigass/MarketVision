package com.gigass.trading.repository;

import com.gigass.trading.entity.StockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoRepository extends JpaRepository<StockInfo, Long> {
    
    /**
     * 根据股票代码查询
     */
    StockInfo findByStockCode(String stockCode);
    
    /**
     * 根据关键词查询相关股票
     */
    @Query("SELECT s FROM StockInfo s WHERE s.stockName LIKE %:keyword% OR s.industry LIKE %:keyword% OR s.concept LIKE %:keyword%")
    List<StockInfo> findByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据行业查询
     */
    List<StockInfo> findByIndustry(String industry);
    
    /**
     * 根据概念查询
     */
    @Query("SELECT s FROM StockInfo s WHERE s.concept LIKE %:concept%")
    List<StockInfo> findByConcept(@Param("concept") String concept);
    
    /**
     * 查询活跃股票
     */
    List<StockInfo> findByIsActiveTrue();
}