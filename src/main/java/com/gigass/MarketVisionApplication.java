package com.gigass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 热搜驱动交易系统主启动类
 * 负责启动整个应用程序
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.gigass.trading", "com.gigass.ai"})
public class MarketVisionApplication {

    private static final Logger logger = LoggerFactory.getLogger(MarketVisionApplication.class);

    public static void main(String[] args) {
        logger.info("正在启动热搜驱动交易系统...");
        SpringApplication.run(MarketVisionApplication.class, args);
        logger.info("热搜驱动交易系统启动完成！");
    }
} 