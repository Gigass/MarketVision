package com.gigass.ai.service;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import com.gigass.ai.enums.AiModelEnum;

public interface AiService {
    ChatResponse chat(ChatRequest request);
    boolean isModelSupported(String model);
    
    // 添加新的方法签名
    String chat(AiModelEnum model, String message);
}
