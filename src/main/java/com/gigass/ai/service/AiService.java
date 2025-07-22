package com.gigass.ai.service;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;

public interface AiService {
    ChatResponse chat(ChatRequest request);
    boolean isModelSupported(String model);
}