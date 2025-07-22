package com.jolywood.ai.controller;

import com.jolywood.ai.dto.ChatRequest;
import com.jolywood.ai.dto.ChatResponse;
import com.jolywood.ai.enums.AiModelEnum;
import com.jolywood.ai.service.AiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {
    
    @Autowired
    private AiService aiService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest request) {
        if (!aiService.isModelSupported(request.getModel())) {
            return ResponseEntity.badRequest()
                .body(new ChatResponse("不支持的AI模型: " + request.getModel()));
        }
        
        ChatResponse response = aiService.chat(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getSupportedModels() {
        List<String> models = Arrays.stream(AiModelEnum.values())
            .filter(model -> aiService.isModelSupported(model.getCode()))
            .map(AiModelEnum::getCode)
            .collect(Collectors.toList());
        return ResponseEntity.ok(models);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Spring AI Service is running");
    }
}