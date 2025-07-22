package com.gigass.ai.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Document(indexName = "ai-call-logs-#{T(java.time.LocalDate).now().toString()}")
public class AiCallLog {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    
    @Field(type = FieldType.Keyword)
    private String modelType; // openai, gemini, claude
    
    @Field(type = FieldType.Keyword)
    private String modelName; // 具体模型名称
    
    @Field(type = FieldType.Text)
    private String prompt; // 用户输入
    
    @Field(type = FieldType.Text)
    private String response; // AI响应
    
    @Field(type = FieldType.Long)
    private Long responseTimeMs; // 响应时间(毫秒)
    
    @Field(type = FieldType.Integer)
    private Integer promptTokens; // 输入token数
    
    @Field(type = FieldType.Integer)
    private Integer completionTokens; // 输出token数
    
    @Field(type = FieldType.Integer)
    private Integer totalTokens; // 总token数
    
    @Field(type = FieldType.Keyword)
    private String status; // SUCCESS, FAILED
    
    @Field(type = FieldType.Text)
    private String errorMessage; // 错误信息
    
    @Field(type = FieldType.Keyword)
    private String userId; // 用户ID
    
    @Field(type = FieldType.Keyword)
    private String sessionId; // 会话ID
    
    // 构造函数和getter/setter
    public AiCallLog() {
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Long responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    // ... getter/setter方法
}