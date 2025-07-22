package com.gigass.trading.enums;

/**
 * 生命周期阶段状态
 */
public enum StageStatus {
    STARTED("开始"),
    PROCESSING("处理中"),
    SUCCESS("成功"),
    FAILED("失败"),
    SKIPPED("跳过");

    private final String description;

    StageStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}