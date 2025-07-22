package com.gigass.ai.controller;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import com.gigass.ai.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Gemini测试控制器
 */
@RestController
@RequestMapping("/test")
public class GeminiTestController {

    @Autowired
    private AiService aiService;

    @PostMapping("/gemini")
    public ChatResponse testGemini(@RequestBody ChatRequest request) {
        // 强制使用gemini模型
        request.setModel("gemini");
        return aiService.chat(request);
    }

    @GetMapping("/gemini/simple")
    public ChatResponse testSimple(@RequestParam(defaultValue = "你好，请介绍一下你自己") String message) {
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage(message);
        return aiService.chat(request);
    }

    @GetMapping("/gemini/status")
    public String checkStatus() {
        boolean supported = aiService.isModelSupported("gemini");
        return "Gemini模型支持状态: " + (supported ? "可用" : "不可用");
    }
}