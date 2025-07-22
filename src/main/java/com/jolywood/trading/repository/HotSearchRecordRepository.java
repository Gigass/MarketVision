package com.jolywood.trading.repository;

import com.jolywood.trading.entity.HotSearchRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HotSearchRecordRepository extends JpaRepository<HotSearchRecord, Long> {
    
    /**
     * 根据状态查询记录
     */
    List<HotSearchRecord> findByStatusOrderByCreatedTimeDesc(HotSearchRecord.ProcessStatus status);
    
    /**
     * 根据来源查询记录
     */
    List<HotSearchRecord> findBySourceOrderByCreatedTimeDesc(String source);
    
    /**
     * 根据关键词模糊查询
     */
    @Query("SELECT h FROM HotSearchRecord h WHERE h.title LIKE %:keyword% OR h.content LIKE %:keyword%")
    List<HotSearchRecord> findByKeyword(@Param("keyword") String keyword);
    
    /**
     * 查询指定时间范围内的记录
     */
    @Query("SELECT h FROM HotSearchRecord h WHERE h.createdTime BETWEEN :startTime AND :endTime ORDER BY h.createdTime DESC")
    List<HotSearchRecord> findByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查询高分记录
     */
    @Query("SELECT h FROM HotSearchRecord h WHERE h.totalScore >= :minScore ORDER BY h.totalScore DESC")
    List<HotSearchRecord> findHighScoreRecords(@Param("minScore") Double minScore);
    
    /**
     * 查询待处理记录数量
     */
    long countByStatus(HotSearchRecord.ProcessStatus status);
    
    /**
     * 根据热度分数范围查询
     */
    @Query("SELECT h FROM HotSearchRecord h WHERE h.heatScore BETWEEN :minHeat AND :maxHeat")
    List<HotSearchRecord> findByHeatScoreRange(@Param("minHeat") Integer minHeat, 
                                              @Param("maxHeat") Integer maxHeat);
}