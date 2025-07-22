package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gigass.trading.enums.LifecycleStage;
import com.gigass.trading.enums.StageStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("hotsearch_lifecycle_log")
public class HotsearchLifecycleLog {

    @TableId
    private Long id; // 雪花算法生成的ID

    @TableField("hot_search_id")
    private Long hotSearchId;

    @TableField("lifecycle_stage")
    private LifecycleStage lifecycleStage;

    @TableField("stage_status")
    private StageStatus stageStatus;

    @TableField("operation_detail")
    private String operationDetail;

    @TableField("input_data")
    private String inputData; // JSON格式

    @TableField("output_data")
    private String outputData; // JSON格式

    @TableField("processing_time_ms")
    private Long processingTimeMs;

    @TableField("error_message")
    private String errorMessage;

    @TableField("error_stack")
    private String errorStack;

    @TableField("agent_name")
    private String agentName;

    @TableField("score_before")
    private Double scoreBefore;

    @TableField("score_after")
    private Double scoreAfter;

    @TableField("related_stocks")
    private String relatedStocks; // JSON格式

    @TableField("market_conditions")
    private String marketConditions; // JSON格式

    @TableField("decision_factors")
    private String decisionFactors; // JSON格式

    @TableField("risk_assessment")
    private String riskAssessment; // JSON格式

    @TableField("confidence_level")
    private Double confidenceLevel;

    @TableField("created_time")
    private LocalDateTime createdTime;

    @TableField("created_by")
    private String createdBy;

    @TableField("trace_id")
    private String traceId;

    @TableField("parent_log_id")
    private Long parentLogId;

    public HotsearchLifecycleLog() {
        this.createdTime = LocalDateTime.now();
        this.createdBy = "SYSTEM";
    }
}