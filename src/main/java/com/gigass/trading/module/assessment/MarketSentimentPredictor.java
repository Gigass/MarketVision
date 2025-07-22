package com.gigass.trading.module.assessment;

import com.gigass.trading.entity.HotSearchRecord;
import com.gigass.trading.repository.HotSearchRecordRepository;
import com.gigass.ai.service.AiService;
import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MarketSentimentPredictor {
    
    private static final Logger logger = LoggerFactory.getLogger(MarketSentimentPredictor.class);
    
    @Autowired
    private AiService aiService;
    
    @Autowired
    private HotSearchRecordRepository hotSearchRecordRepository;
    
    /**
     * 市场情绪预测 - 基于历史数据预测市场对此类信息的情绪反应
     */
    public Double predictMarketSentiment(HotSearchRecord record) {
        logger.debug("执行市场情绪预测: {}", record.getTitle());
        
        try {
            // 1. 分析历史相似事件的市场反应
            Double historicalSentimentScore = analyzeHistoricalSentiment(record);
            
            // 2. 使用AI预测当前市场情绪
            Double aiPredictionScore = predictWithAI(record);
            
            // 3. 分析市场环境因素
            Double marketEnvironmentScore = analyzeMarketEnvironment(record);
            
            // 综合计算市场情绪预测分数
            Double sentimentScore = (historicalSentimentScore + aiPredictionScore + marketEnvironmentScore) / 3.0;
            
            logger.debug("市场情绪预测完成，分数: {}", sentimentScore);
            return sentimentScore;
            
        } catch (Exception e) {
            logger.error("市场情绪预测失败: {}", e.getMessage(), e);
            return 50.0; // 中性分数
        }
    }
    
    /**
     * 分析历史相似事件的市场反应
     */
    private Double analyzeHistoricalSentiment(HotSearchRecord record) {
        try {
            // 查找历史相似记录
            List<HotSearchRecord> similarRecords = hotSearchRecordRepository.findByKeyword(
                extractKeyword(record.getTitle()));
            
            if (similarRecords.isEmpty()) {
                return 50.0; // 默认中性
            }
            
            // 计算历史记录的平均情绪分数
            double avgSentiment = similarRecords.stream()
                .filter(r -> r.getSentimentScore() != null)
                .mapToDouble(HotSearchRecord::getSentimentScore)
                .average()
                .orElse(50.0);
            
            return avgSentiment;
            
        } catch (Exception e) {
            logger.warn("历史情绪分析失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 使用AI预测市场情绪
     */
    private Double predictWithAI(HotSearchRecord record) {
        try {
            String prompt = buildMarketSentimentPrompt(record);
            
            ChatRequest request = new ChatRequest();
            request.setMessage(prompt);
            request.setModel("openai");
            
            ChatResponse response = aiService.chat(request);
            
            return parsePredictionScore(response.getMessage());
            
        } catch (Exception e) {
            logger.warn("AI市场情绪预测失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 分析市场环境因素
     */
    private Double analyzeMarketEnvironment(HotSearchRecord record) {
        // TODO: 实现市场环境分析
        // 1. 当前市场趋势
        // 2. 宏观经济环境
        // 3. 政策环境
        // 4. 行业周期
        
        return 60.0; // 默认分数
    }
    
    private String buildMarketSentimentPrompt(HotSearchRecord record) {
        return String.format(
            "作为股票市场分析师，请预测以下热搜事件对股票市场情绪的影响，返回0-100的分数，" +
            "其中0表示极度负面影响，50表示中性，100表示极度正面影响。\n" +
            "热搜标题：%s\n" +
            "热搜内容：%s\n" +
            "来源：%s\n" +
            "请只返回数字分数，不要其他解释。",
            record.getTitle(),
            record.getContent(),
            record.getSource()
        );
    }
    
    private Double parsePredictionScore(String aiResponse) {
        try {
            String numStr = aiResponse.replaceAll("[^0-9.]", "");
            return Double.parseDouble(numStr);
        } catch (Exception e) {
            logger.warn("解析AI预测分数失败: {}", aiResponse);
            return 50.0;
        }
    }
    
    private String extractKeyword(String title) {
        return title.length() > 10 ? title.substring(0, 10) : title;
    }
}