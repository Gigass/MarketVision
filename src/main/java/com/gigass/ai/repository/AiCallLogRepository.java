package com.gigass.ai.repository;

import com.gigass.ai.entity.AiCallLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiCallLogRepository extends ElasticsearchRepository<AiCallLog, String> {
}