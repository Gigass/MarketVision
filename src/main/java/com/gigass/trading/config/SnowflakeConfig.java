package com.gigass.trading.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置
 */
@Configuration
public class SnowflakeConfig {

    @Value("${snowflake.worker-id:1}")
    private long workerId;

    @Value("${snowflake.datacenter-id:1}")
    private long datacenterId;

    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(workerId, datacenterId);
    }
}