package com.gigass.ai.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.gigass.ai.repository")
public class ElasticsearchConfig {
    // 使用application.yml中的配置即可
}