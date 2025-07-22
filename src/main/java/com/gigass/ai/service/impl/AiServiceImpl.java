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
        try {
            AiModelEnum modelEnum = AiModelEnum.fromCode(request.getModel());
            ChatModel chatModel = getChatModel(modelEnum);
            
            if (chatModel == null) {
                return new ChatResponse("模型 " + request.getModel() + " 未配置或不可用");
            }
            
            Prompt prompt = new Prompt(request.getMessage());
            String response = chatModel.call(prompt).getResult().getOutput().getContent();
            
            logger.info("AI响应成功，模型: {}, 消息长度: {}", request.getModel(), response.length());
            return new ChatResponse(response, request.getModel());
            
        } catch (Exception e) {
            logger.error("AI调用失败: {}", e.getMessage(), e);
            return new ChatResponse("AI调用失败: " + e.getMessage());
        }
    }

    @Override
    public boolean isModelSupported(String model) {
        try {
            AiModelEnum modelEnum = AiModelEnum.fromCode(model);
            return getChatModel(modelEnum) != null;
        } catch (IllegalArgumentException e) {
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
}
