package com.gigass.trading.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gigass.trading.entity.HotsearchLifecycleLog;
import com.gigass.trading.enums.LifecycleStage;
import com.gigass.trading.enums.StageStatus;
import com.gigass.trading.mapper.HotsearchLifecycleLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class HotsearchLifecycleLogService extends ServiceImpl<HotsearchLifecycleLogMapper, HotsearchLifecycleLog> {

    private static final Logger logger = LoggerFactory.getLogger(HotsearchLifecycleLogService.class);

    @Autowired
    private Snowflake snowflake;

    /**
     * 记录生命周期开始日志
     */
    public Long logStageStart(Long hotSearchId, LifecycleStage stage, String agentName, 
                             String operationDetail, Object inputData) {
        return logStageStart(hotSearchId, stage, agentName, operationDetail, inputData, null);
    }

    /**
     * 记录生命周期开始日志（带父级ID）
     */
    public Long logStageStart(Long hotSearchId, LifecycleStage stage, String agentName, 
                             String operationDetail, Object inputData, Long parentLogId) {
        long startTime = System.currentTimeMillis();
        Long logId = snowflake.nextId();
        String traceId = getOrCreateTraceId();

        HotsearchLifecycleLog log = new HotsearchLifecycleLog();
        log.setId(logId);
        log.setHotSearchId(hotSearchId);
        log.setLifecycleStage(stage);
        log.setStageStatus(StageStatus.STARTED);
        log.setAgentName(agentName);
        log.setOperationDetail(operationDetail);
        log.setInputData(inputData != null ? JSONUtil.toJsonStr(inputData) : null);
        log.setTraceId(traceId);
        log.setParentLogId(parentLogId);

        save(log);
        
        // 存储开始时间到MDC，用于计算处理时间
        MDC.put("stage_start_time_" + logId, String.valueOf(startTime));
        
        logger.info("热搜生命周期阶段开始: hotSearchId={}, stage={}, logId={}, traceId={}", 
                   hotSearchId, stage, logId, traceId);
        
        return logId;
    }

    /**
     * 记录生命周期成功日志
     */
    public void logStageSuccess(Long logId, String operationDetail, Object outputData, 
                               Double scoreBefore, Double scoreAfter, Double confidenceLevel) {
        updateStageLog(logId, StageStatus.SUCCESS, operationDetail, outputData, 
                      scoreBefore, scoreAfter, confidenceLevel, null, null);
    }

    /**
     * 记录生命周期失败日志
     */
    public void logStageFailure(Long logId, String operationDetail, String errorMessage, 
                               String errorStack) {
        updateStageLog(logId, StageStatus.FAILED, operationDetail, null, 
                      null, null, null, errorMessage, errorStack);
    }

    /**
     * 记录生命周期处理中日志
     */
    public void logStageProcessing(Long logId, String operationDetail, Object currentData) {
        updateStageLog(logId, StageStatus.PROCESSING, operationDetail, currentData, 
                      null, null, null, null, null);
    }

    /**
     * 更新阶段日志
     */
    private void updateStageLog(Long logId, StageStatus status, String operationDetail, 
                               Object outputData, Double scoreBefore, Double scoreAfter, 
                               Double confidenceLevel, String errorMessage, String errorStack) {
        try {
            HotsearchLifecycleLog log = getById(logId);
            if (log == null) {
                logger.warn("未找到生命周期日志记录: logId={}", logId);
                return;
            }

            // 计算处理时间
            String startTimeStr = MDC.get("stage_start_time_" + logId);
            if (startTimeStr != null) {
                long startTime = Long.parseLong(startTimeStr);
                long processingTime = System.currentTimeMillis() - startTime;
                log.setProcessingTimeMs(processingTime);
                MDC.remove("stage_start_time_" + logId);
            }

            log.setStageStatus(status);
            log.setOperationDetail(operationDetail);
            log.setOutputData(outputData != null ? JSONUtil.toJsonStr(outputData) : null);
            log.setScoreBefore(scoreBefore);
            log.setScoreAfter(scoreAfter);
            log.setConfidenceLevel(confidenceLevel);
            log.setErrorMessage(errorMessage);
            log.setErrorStack(errorStack);

            updateById(log);

            logger.info("热搜生命周期阶段更新: logId={}, status={}, processingTime={}ms", 
                       logId, status, log.getProcessingTimeMs());

        } catch (Exception e) {
            logger.error("更新生命周期日志失败: logId=" + logId, e);
        }
    }

    /**
     * 记录详细的决策信息
     */
    public void logDecisionDetails(Long logId, Map<String, Object> decisionFactors, 
                                  Map<String, Object> riskAssessment, 
                                  Map<String, Object> marketConditions,
                                  Map<String, Object> relatedStocks) {
        try {
            HotsearchLifecycleLog log = getById(logId);
            if (log != null) {
                log.setDecisionFactors(decisionFactors != null ? JSONUtil.toJsonStr(decisionFactors) : null);
                log.setRiskAssessment(riskAssessment != null ? JSONUtil.toJsonStr(riskAssessment) : null);
                log.setMarketConditions(marketConditions != null ? JSONUtil.toJsonStr(marketConditions) : null);
                log.setRelatedStocks(relatedStocks != null ? JSONUtil.toJsonStr(relatedStocks) : null);
                updateById(log);
            }
        } catch (Exception e) {
            logger.error("记录决策详情失败: logId=" + logId, e);
        }
    }

    /**
     * 获取或创建链路追踪ID
     */
    private String getOrCreateTraceId() {
        String traceId = MDC.get("traceId");
        if (traceId == null) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            MDC.put("traceId", traceId);
        }
        return traceId;
    }
}