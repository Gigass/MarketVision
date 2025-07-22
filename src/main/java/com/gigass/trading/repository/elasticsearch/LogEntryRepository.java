package com.gigass.trading.repository.elasticsearch;

import com.gigass.trading.entity.LogEntry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEntryRepository extends ElasticsearchRepository<LogEntry, String> {
    
    // 按日志级别查询
    List<LogEntry> findByLevel(String level);
    
    // 按时间范围查询
    List<LogEntry> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
    
    // 按操作类型查询
    List<LogEntry> findByOperationType(String operationType);
    
    // 按日志级别和时间范围查询（分页）
    Page<LogEntry> findByLevelAndTimestampBetween(String level, LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    // 按关键字搜索消息内容
    List<LogEntry> findByMessageContaining(String keyword);
}