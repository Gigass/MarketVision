package com.gigass.ai.service.impl;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import com.gigass.ai.enums.AiModelEnum;
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
    
    @Autowired(required = false)
    private VertexAiGeminiChatModel geminiChatModel;
    
    @Autowired(required = false)
    private AnthropicChatModel anthropicChatModel;

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
            case OPENAI -> openAiChatModel;
            case GEMINI -> geminiChatModel;
            case CLAUDE -> anthropicChatModel;
        };
    }
}