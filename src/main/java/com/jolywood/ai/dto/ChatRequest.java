package com.jolywood.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChatRequest {
    @NotBlank(message = "消息内容不能为空")
    private String message;
    
    @NotNull(message = "AI模型不能为空")
    private String model;
    
    private Double temperature = 0.7;

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }
}