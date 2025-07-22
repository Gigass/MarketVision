package com.gigass.ai.demo;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import com.gigass.ai.service.AiService;
import com.gigass.ai.entity.AiCallLog;
import com.gigass.ai.repository.AiCallLogRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Gemini API 测试Demo
 */
@Component
@ConditionalOnProperty(name = "demo.ai.enabled", havingValue = "true", matchIfMissing = false)
@Order(1) // 优先执行，在其他Demo之前
public class GeminiTestDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(GeminiTestDemo.class);

    @Autowired
    private AiService aiService;

    @Autowired
    private AiCallLogRepository aiCallLogRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public void run(String... args) {
        System.out.println("=== Gemini API 测试开始 ===");
        
        // 测试1: 简单问答
        testSimpleChat();
        
//        // 测试4: ES日志记录和查询
//        testEsLogDemo();
        
        System.out.println("=== Gemini API 测试完成 ===");
    }

    private void testSimpleChat() {
        System.out.println("\n--- 测试1: 简单问答 ---");
        
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage("你好，请简单介绍一下你自己");
        
        try {
            ChatResponse response = aiService.chat(request);
            System.out.println("请求: " + request.getMessage());
            System.out.println("响应: " + response.getContent());
            System.out.println("使用模型: " + response.getModel());
        } catch (Exception e) {
            logger.error("简单问答测试失败", e);
            System.out.println("测试失败: " + e.getMessage());
        }
    }

    private void testStockAnalysis() {
        System.out.println("\n--- 测试2: 股票分析 ---");
        
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage("请分析一下AI概念股的投资价值，包括风险和机会");
        
        try {
            ChatResponse response = aiService.chat(request);
            System.out.println("请求: " + request.getMessage());
            System.out.println("响应: " + response.getContent());
        } catch (Exception e) {
            logger.error("股票分析测试失败", e);
            System.out.println("测试失败: " + e.getMessage());
        }
    }

    private void testHotSearchAnalysis() {
        System.out.println("\n--- 测试3: 热搜分析 ---");
        
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage("假设微博热搜出现'ChatGPT概念股大涨'，请分析这个热搜的投资信号强度，从情感倾向、传播速度、真实性等维度评分（1-10分）");
        
        try {
            ChatResponse response = aiService.chat(request);
            System.out.println("请求: " + request.getMessage());
            System.out.println("响应: " + response.getContent());
        } catch (Exception e) {
            logger.error("热搜分析测试失败", e);
            System.out.println("测试失败: " + e.getMessage());
        }
    }

    private void   testEsLogDemo() {
        System.out.println("\n--- 测试4: ES AI请求日志记录和查询 ---");
        
        // 1. 记录AI请求日志到ES
        String testPrompt = "这是一个ES日志测试请求";
        String logId = recordAiCallLog(testPrompt);
        
        // 2. 等待ES索引
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 3. 查询刚插入的日志
        queryAiCallLogs(logId);
    }

    private String recordAiCallLog(String prompt) {
        try {
            AiCallLog aiCallLog = new AiCallLog();
            String logId = UUID.randomUUID().toString();
            aiCallLog.setId(logId);
            aiCallLog.setTimestamp(LocalDateTime.now());
            aiCallLog.setModelType("gemini");
            aiCallLog.setModelName("gemini-pro");
            aiCallLog.setPrompt(prompt);
            aiCallLog.setResponse("这是测试响应内容");
            aiCallLog.setResponseTimeMs(1500L);
            aiCallLog.setPromptTokens(20);
            aiCallLog.setCompletionTokens(15);  // 修正方法名
            aiCallLog.setTotalTokens(35);
            aiCallLog.setStatus("SUCCESS");     // 使用 status 而不是 success
            
            aiCallLogRepository.save(aiCallLog);
            System.out.println("✓ AI请求日志已保存到ES，ID: " + logId);
            return logId;
        } catch (Exception e) {
            logger.error("保存AI请求日志失败", e);
            System.out.println("✗ 保存日志失败: " + e.getMessage());
            return null;
        }
    }

    private void queryAiCallLogs(String logId) {
        try {
            // 方式1: 通过Repository查询
            System.out.println("\n--- 通过Repository查询 ---");
            if (logId != null) {
                aiCallLogRepository.findById(logId).ifPresentOrElse(
                    log -> {
                        System.out.println("✓ 找到日志记录:");
                        System.out.println("  ID: " + log.getId());
                        System.out.println("  时间: " + log.getTimestamp());
                        System.out.println("  模型: " + log.getModelType());
                        System.out.println("  提示: " + log.getPrompt());
                        System.out.println("  响应时间: " + log.getResponseTimeMs() + "ms");
                    },
                    () -> System.out.println("✗ 未找到指定ID的日志记录")
                );
            }
            
            // 方式2: 查询最近的日志
            System.out.println("\n--- 查询最近的AI请求日志 ---");
            Query query = new CriteriaQuery(new Criteria())
                .setPageable(PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "timestamp")));
                
            SearchHits<AiCallLog> searchHits = elasticsearchOperations.search(query, AiCallLog.class);
            
            if (searchHits.hasSearchHits()) {
                System.out.println("✓ 找到 " + searchHits.getTotalHits() + " 条AI请求日志:");
                searchHits.getSearchHits().forEach(hit -> {
                    AiCallLog log = hit.getContent();
                    System.out.println("  - " + log.getTimestamp() + " | " + log.getModelType() + " | " + 
                        log.getPrompt().substring(0, Math.min(30, log.getPrompt().length())) + "...");
                });
            } else {
                System.out.println("✗ 未找到任何AI请求日志");
            }
            
        } catch (Exception e) {
            logger.error("查询AI请求日志失败", e);
            System.out.println("✗ 查询日志失败: " + e.getMessage());
        }
    }
}
