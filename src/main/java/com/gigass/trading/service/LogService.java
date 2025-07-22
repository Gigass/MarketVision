package com.gigass.trading.service;

import com.gigass.trading.entity.LogEntry;
import com.gigass.trading.repository.elasticsearch.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LogService {
    
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);
    
    @Autowired
    private LogEntryRepository logEntryRepository;
    
    /**
     * 记录操作日志到 Elasticsearch
     */
    public void logOperation(String operationType, String description, String params, 
                           String ip, String url, String httpMethod, String status) {
        try {
            LogEntry logEntry = new LogEntry("INFO", "OperationLog", description, Thread.currentThread().getName());
            logEntry.setOperationType(operationType);
            logEntry.setOperationParams(params);
            logEntry.setOperationIp(ip);
            logEntry.setOperationUrl(url);
            logEntry.setHttpMethod(httpMethod);
            logEntry.setOperationStatus(status);
            
            logEntryRepository.save(logEntry);
            logger.debug("操作日志已记录到 Elasticsearch: {}", operationType);
        } catch (Exception e) {
            logger.error("记录操作日志到 Elasticsearch 失败", e);
        }
    }
    
    /**
     * 记录普通日志到 Elasticsearch
     */
    public void logMessage(String level, String logger, String message, String thread, String exception) {
        try {
            LogEntry logEntry = new LogEntry(level, logger, message, thread);
            if (exception != null) {
                logEntry.setException(exception);
            }
            
            logEntryRepository.save(logEntry);
        } catch (Exception e) {
            // 避免日志记录失败导致的循环错误
            System.err.println("记录日志到 Elasticsearch 失败: " + e.getMessage());
        }
    }
}