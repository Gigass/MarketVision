package com.gigass.trading.repository;

import com.gigass.trading.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志存储库
 * 用于操作日志的CRUD操作
 */
@Repository
public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {

    /**
     * 根据操作类型查询操作日志
     * @param operationType 操作类型
     * @return 操作日志列表
     */
    List<OperationLog> findByOperationType(String operationType);

    /**
     * 根据操作时间范围查询操作日志
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 操作日志列表
     */
    @Query("SELECT o FROM OperationLog o WHERE o.operationTime BETWEEN :startTime AND :endTime ORDER BY o.operationTime DESC")
    List<OperationLog> findByOperationTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据操作状态查询操作日志
     * @param operationStatus 操作状态
     * @return 操作日志列表
     */
    List<OperationLog> findByOperationStatus(String operationStatus);

    /**
     * 根据用户ID查询操作日志
     * @param userId 用户ID
     * @return 操作日志列表
     */
    List<OperationLog> findByUserId(Long userId);

    /**
     * 根据操作描述模糊查询操作日志
     * @param description 操作描述
     * @return 操作日志列表
     */
    @Query("SELECT o FROM OperationLog o WHERE o.operationDescription LIKE %:description%")
    List<OperationLog> findByOperationDescriptionContaining(String description);

    /**
     * 查询最近的操作日志
     * @param limit 限制数量
     * @return 操作日志列表
     */
    @Query(value = "SELECT * FROM operation_log ORDER BY operation_time DESC LIMIT :limit", nativeQuery = true)
    List<OperationLog> findRecentLogs(int limit);
} 