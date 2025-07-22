package com.gigass.trading.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务配置类
 * 启用Spring的定时任务功能
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // 定时任务的全局配置可以在这里添加
} 