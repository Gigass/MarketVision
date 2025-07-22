package com.gigass.trading.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Document(indexName = "market-vision-logs")
public class LogEntry {
    
    @Id
    private String id;
    
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    
    @Field(type = FieldType.Keyword)
    private String level;
    
    @Field(type = FieldType.Keyword)
    private String logger;
    
    @Field(type = FieldType.Text)
    private String message;
    
    @Field(type = FieldType.Keyword)
    private String thread;
    
    @Field(type = FieldType.Keyword)
    private String application;
    
    @Field(type = FieldType.Text)
    private String exception;
    
    @Field(type = FieldType.Keyword)
    private String operationType;
    
    @Field(type = FieldType.Keyword)
    private String operationStatus;
    
    @Field(type = FieldType.Text)
    private String operationParams;
    
    @Field(type = FieldType.Keyword)
    private String operationIp;
    
    @Field(type = FieldType.Keyword)
    private String operationUrl;
    
    @Field(type = FieldType.Keyword)
    private String httpMethod;

    // 构造函数
    public LogEntry() {}
    
    public LogEntry(String level, String logger, String message, String thread) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.logger = logger;
        this.message = message;
        this.thread = thread;
        this.application = "market-vision";
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    
    public String getLogger() { return logger; }
    public void setLogger(String logger) { this.logger = logger; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getThread() { return thread; }
    public void setThread(String thread) { this.thread = thread; }
    
    public String getApplication() { return application; }
    public void setApplication(String application) { this.application = application; }
    
    public String getException() { return exception; }
    public void setException(String exception) { this.exception = exception; }
    
    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }
    
    public String getOperationStatus() { return operationStatus; }
    public void setOperationStatus(String operationStatus) { this.operationStatus = operationStatus; }
    
    public String getOperationParams() { return operationParams; }
    public void setOperationParams(String operationParams) { this.operationParams = operationParams; }
    
    public String getOperationIp() { return operationIp; }
    public void setOperationIp(String operationIp) { this.operationIp = operationIp; }
    
    public String getOperationUrl() { return operationUrl; }
    public void setOperationUrl(String operationUrl) { this.operationUrl = operationUrl; }
    
    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }
}