package com.gigass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages = {"com.gigass.trading", "com.gigass.ai"})
@MapperScan("com.gigass.trading.mapper")
@EnableElasticsearchRepositories(basePackages = "com.gigass.trading.repository.elasticsearch")
public class MarketVisionApplication {

    private static final Logger logger = LoggerFactory.getLogger(MarketVisionApplication.class);

    public static void main(String[] args) {
        logger.info("正在启动热搜驱动交易系统...");
        SpringApplication.run(MarketVisionApplication.class, args);
        logger.info("热搜驱动交易系统启动完成！");
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
} 