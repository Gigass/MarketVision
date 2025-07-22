package com.gigass.trading.controller;

import com.gigass.trading.service.SignalProcessingService;
import com.gigass.trading.dto.SignalFilterResult;
import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.mapper.HotSearchRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/signal")
@CrossOrigin(origins = "*")
public class SignalController {
    
    @Autowired
    private SignalProcessingService signalProcessingService;
    
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;
    
    /**
     * 手动触发信号处理
     */
    @PostMapping("/process")
    public ResponseEntity<String> processSignals() {
        try {
            signalProcessingService.processSignals();
            return ResponseEntity.ok("信号处理任务已启动");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("启动失败: " + e.getMessage());
        }
    }
    
    /**
     * 处理指定记录
     */
    @PostMapping("/process/{recordId}")
    public ResponseEntity<SignalFilterResult> processRecord(@PathVariable Long recordId) {
        try {
            HotSearchRecord record = hotSearchRecordRepository.selectById(recordId);
            if (record == null) {
                return ResponseEntity.notFound().build();
            }
            
            SignalFilterResult result = signalProcessingService.processSignalRecord(record);
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 获取处理状态
     */
    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Signal Processing Service is running");
    }
}