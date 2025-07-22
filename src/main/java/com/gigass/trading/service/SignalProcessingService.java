package com.gigass.trading.service;

import com.gigass.trading.factory.SignalCaptureFactory;
import com.gigass.trading.dto.SignalFilterResult;
import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.repository.HotSearchRecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignalProcessingService {
    
    private static final Logger logger = LoggerFactory.getLogger(SignalProcessingService.class);
    
    @Autowired
    private SignalCaptureFactory signalCaptureFactory;
    
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;
    
    /**
     * 处理待处理的热搜记录
     */
    @Async
    public void processSignals() {
        logger.info("开始处理信号捕捉与筛选任务");
        
        try {
            // 获取待处理的记录
            List<HotSearchRecord> pendingRecords = hotSearchRecordRepository
                .findByStatusOrderByCreatedTimeDesc(HotSearchRecord.ProcessStatus.PENDING);
            
            for (HotSearchRecord record : pendingRecords) {
                processSignalRecord(record);
            }
            
            logger.info("信号处理完成，共处理 {} 条记录", pendingRecords.size());
            
        } catch (Exception e) {
            logger.error("信号处理失败: {}", e.getMessage(), e);
        }
    }
    
    /**
     * 处理单条记录
     */
    public SignalFilterResult processSignalRecord(HotSearchRecord record) {
        logger.info("开始处理信号记录: {}", record.getTitle());
        
        try {
            // 更新状态为处理中
            record.setStatus(HotSearchRecord.ProcessStatus.PROCESSING);
            hotSearchRecordRepository.save(record);
            
            // 执行信号捕捉与筛选
            SignalFilterResult result = signalCaptureFactory.executeSignalCapture(record);
            
            // 更新记录状态和分数
            updateRecordWithResult(record, result);
            
            logger.info("信号记录处理完成: {} - 通过: {}", record.getTitle(), result.getPassed());
            
            return result;
            
        } catch (Exception e) {
            logger.error("处理信号记录失败: {}", e.getMessage(), e);
            
            // 更新为失败状态
            record.setStatus(HotSearchRecord.ProcessStatus.FAILED);
            hotSearchRecordRepository.save(record);
            
            return null;
        }
    }
    
    /**
     * 更新记录结果
     */
    private void updateRecordWithResult(HotSearchRecord record, SignalFilterResult result) {
        record.setSentimentScore(result.getSentimentScore());
        record.setPropagationSpeed(result.getPropagationSpeed());
        record.setVerificationScore(result.getVerificationScore());
        record.setTotalScore(result.getTotalScore());
        
        if (result.getPassed()) {
            record.setStatus(HotSearchRecord.ProcessStatus.FILTERED);
        } else {
            record.setStatus(HotSearchRecord.ProcessStatus.COMPLETED);
        }
        
        hotSearchRecordRepository.save(record);
    }
}