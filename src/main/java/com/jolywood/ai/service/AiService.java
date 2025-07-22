package com.jolywood.ai.service;

import com.jolywood.ai.dto.ChatRequest;
import com.jolywood.ai.dto.ChatResponse;

public interface AiService {
    ChatResponse chat(ChatRequest request);
    boolean isModelSupported(String model);
}