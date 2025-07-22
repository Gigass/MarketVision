package com.jolywood.trading.module.signal;

import com.jolywood.trading.entity.HotSearchRecord;
import com.jolywood.ai.service.AiService;
import com.jolywood.ai.dto.ChatRequest;
import com.jolywood.ai.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SentimentQuantifier {
    
    private static final Logger logger = LoggerFactory.getLogger(SentimentQuantifier.class);
    
    @Autowired
    private AiService aiService;
    
    /**
     * 情绪极性量化 - 对热搜内容进行情感分析，量化正面/负面情绪强度
     */
    public Double quantifySentiment(HotSearchRecord record) {
        logger.debug("执行情绪极性量化: {}", record.getTitle());
        
        try {
            // 构建情感分析提示词
            String prompt = buildSentimentAnalysisPrompt(record);
            
            ChatRequest request = new ChatRequest();
            request.setMessage(prompt);
            request.setModel("openai"); // 可配置
            
            ChatResponse response = aiService.chat(request);
            
            // 解析AI响应，提取情感分数
            Double sentimentScore = parseSentimentScore(response.getMessage());
            
            logger.debug("情绪极性量化完成，分数: {}", sentimentScore);
            return sentimentScore;
            
        } catch (Exception e) {
            logger.error("情绪极性量化失败: {}", e.getMessage(), e);
            return 50.0; // 中性分数
        }
    }
    
    private String buildSentimentAnalysisPrompt(HotSearchRecord record) {
        return String.format(
            "请分析以下热搜内容的情感倾向，返回0-100的分数，其中0表示极度负面，50表示中性，100表示极度正面。\n" +
            "标题：%s\n" +
            "内容：%s\n" +
            "请只返回数字分数，不要其他解释。",
            record.getTitle(),
            record.getContent()
        );
    }
    
    private Double parseSentimentScore(String aiResponse) {
        try {
            // 提取数字
            String numStr = aiResponse.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numStr);
        } catch (Exception e) {
            logger.warn("解析情感分数失败，使用默认值: {}", aiResponse);
            return 50.0;
        }
    }
    
    /**
     * 分析情绪强度
     */
    public Double analyzeEmotionIntensity(HotSearchRecord record) {
        // TODO: 实现情绪强度分析
        return 0.0;
    }
    
    /**
     * 检测情绪极性
     */
    public String detectEmotionPolarity(HotSearchRecord record) {
        // TODO: 实现情绪极性检测
        return "NEUTRAL";
    }
}