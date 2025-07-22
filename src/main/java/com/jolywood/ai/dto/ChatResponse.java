package com.jolywood.ai.dto;

public class ChatResponse {
    private String content;
    private String model;
    private boolean success;
    private String error;

    public ChatResponse(String content, String model) {
        this.content = content;
        this.model = model;
        this.success = true;
    }

    public ChatResponse(String error) {
        this.error = error;
        this.success = false;
    }

    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}