package com.gigass.trading.enums;

/**
 * 热搜生命周期阶段
 */
public enum LifecycleStage {
    DISCOVERED("发现阶段"),
    ANALYZING("分析阶段"),
    SCORED("评分阶段"),
    STOCK_MATCHED("股票匹配阶段"),
    DECISION_MADE("决策制定阶段"),
    TRADING("交易执行阶段"),
    MONITORING("监控阶段"),
    CLOSED("结束阶段");

    private final String description;

    LifecycleStage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}