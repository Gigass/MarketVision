package com.gigass.ai.service.impl;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import com.gigass.ai.entity.AiCallLog;
import com.gigass.ai.enums.AiModelEnum;
import com.gigass.ai.repository.AiCallLogRepository;
import com.gigass.ai.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AiServiceImpl implements AiService {
    
    private static final Logger logger = LoggerFactory.getLogger(AiServiceImpl.class);
    
    @Autowired(required = false)
    private OpenAiChatModel openAiChatModel;
    
    // 移除这两个依赖，因为我们不使用官方的 Vertex AI 和 Anthropic
    // @Autowired(required = false)
    // private VertexAiGeminiChatModel geminiChatModel;
    
    // @Autowired(required = false)
    // private AnthropicChatModel anthropicChatModel;

    @Autowired
    private AiCallLogRepository aiCallLogRepository;

    @Override
    public ChatResponse chat(ChatRequest request) {
        long startTime = System.currentTimeMillis();
        AiCallLog callLog = new AiCallLog();
        callLog.setId(UUID.randomUUID().toString());
        callLog.setTimestamp(LocalDateTime.now());
        callLog.setModelType(request.getModel());
        callLog.setPrompt(request.getMessage());
        
        try {
            AiModelEnum modelEnum = AiModelEnum.fromCode(request.getModel());
            ChatModel chatModel = getChatModel(modelEnum);
            
            if (chatModel == null) {
                long responseTime = System.currentTimeMillis() - startTime;
                callLog.setStatus("FAILED");
                callLog.setErrorMessage("模型 " + request.getModel() + " 未配置或不可用");
                callLog.setResponseTimeMs(responseTime);
                callLog.setModelName(getModelName(modelEnum));
                aiCallLogRepository.save(callLog);
                
                return new ChatResponse("模型 " + request.getModel() + " 未配置或不可用");
            }
            
            Prompt prompt = new Prompt(request.getMessage());
            String response = chatModel.call(prompt).getResult().getOutput().getContent();
            
            long responseTime = System.currentTimeMillis() - startTime;
            callLog.setResponse(response);
            callLog.setResponseTimeMs(responseTime);
            callLog.setStatus("SUCCESS");
            callLog.setModelName(getModelName(modelEnum));
            callLog.setPromptTokens(estimateTokens(request.getMessage()));
            callLog.setCompletionTokens(estimateTokens(response));
            callLog.setTotalTokens(callLog.getPromptTokens() + callLog.getCompletionTokens());
            
            aiCallLogRepository.save(callLog);
            logger.info("AI响应成功，模型: {}, 消息长度: {}", request.getModel(), response.length());
            return new ChatResponse(response, request.getModel());
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            callLog.setStatus("FAILED");
            callLog.setErrorMessage(e.getMessage());
            callLog.setResponseTimeMs(responseTime);
            callLog.setModelName(getModelName(AiModelEnum.fromCode(request.getModel())));
            
            aiCallLogRepository.save(callLog);
            logger.error("AI调用失败: {}", e.getMessage(), e);
            return new ChatResponse("AI调用失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isModelSupported(String model) {
        long startTime = System.currentTimeMillis();
        AiCallLog callLog = new AiCallLog();
        callLog.setId(UUID.randomUUID().toString());
        callLog.setTimestamp(LocalDateTime.now());
        callLog.setModelType(model);
        callLog.setPrompt("模型支持检查");
        
        try {
            AiModelEnum modelEnum = AiModelEnum.fromCode(model);
            boolean supported = getChatModel(modelEnum) != null;
            
            long responseTime = System.currentTimeMillis() - startTime;
            callLog.setResponse("模型支持: " + supported);
            callLog.setResponseTimeMs(responseTime);
            callLog.setStatus("SUCCESS");
            callLog.setModelName(getModelName(modelEnum));
            
            aiCallLogRepository.save(callLog);
            return supported;
        } catch (IllegalArgumentException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            callLog.setStatus("FAILED");
            callLog.setErrorMessage("不支持的模型: " + model);
            callLog.setResponseTimeMs(responseTime);
            callLog.setResponse("false");
            
            aiCallLogRepository.save(callLog);
            return false;
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            callLog.setStatus("FAILED");
            callLog.setErrorMessage(e.getMessage());
            callLog.setResponseTimeMs(responseTime);
            
            aiCallLogRepository.save(callLog);
            logger.error("检查模型支持失败: {}", e.getMessage(), e);
            return false;
        }
    }
    
    private ChatModel getChatModel(AiModelEnum modelEnum) {
        return switch (modelEnum) {
            case OPENAI, GEMINI -> openAiChatModel; // 统一使用 OpenAI 客户端
            case CLAUDE -> null; // 暂时不支持
        };
    }

    @Override
    public String chat(AiModelEnum model, String message) {
        long startTime = System.currentTimeMillis();
        AiCallLog callLog = new AiCallLog();
        callLog.setModelType(model.getCode());
        callLog.setPrompt(message);
        
        try {
            String response = switch (model) {
                case OPENAI -> openAiChatModel.call(message);
                case GEMINI -> openAiChatModel.call(message); // 使用同一个client
                case CLAUDE -> openAiChatModel.call(message);
            };
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            // 记录成功调用
            callLog.setResponse(response);
            callLog.setResponseTimeMs(responseTime);
            callLog.setStatus("SUCCESS");
            callLog.setModelName(getModelName(model));
            
            aiCallLogRepository.save(callLog);
            logger.info("AI调用成功 - 模型: {}, 耗时: {}ms", model.getCode(), responseTime);
            
            return response;
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            
            // 记录失败调用
            callLog.setStatus("FAILED");
            callLog.setErrorMessage(e.getMessage());
            callLog.setResponseTimeMs(responseTime);
            callLog.setModelName(getModelName(model));
            
            aiCallLogRepository.save(callLog);
            logger.error("AI调用失败 - 模型: {}, 错误: {}", model.getCode(), e.getMessage());
            
            throw new RuntimeException("AI调用失败: " + e.getMessage(), e);
        }
    }

    private String getModelName(AiModelEnum model) {
        return switch (model) {
            case OPENAI -> "gpt-3.5-turbo";
            case GEMINI -> "[满血伪流]gemini-2.5-pro-search";
            case CLAUDE -> "claude-3-sonnet";
        };
    }

    // 添加 token 估算方法
    private Integer estimateTokens(String text) {
        if (text == null) return 0;
        // 简单估算：中文按字符数，英文按单词数*1.3
        int chineseChars = text.replaceAll("[^\\u4e00-\\u9fa5]", "").length();
        int englishWords = text.replaceAll("[\\u4e00-\\u9fa5]", "").split("\\s+").length;
        return chineseChars + (int)(englishWords * 1.3);
    }
}
