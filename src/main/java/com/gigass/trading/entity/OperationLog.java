package com.gigass.trading.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 操作日志实体类
 * 用于记录系统中的所有操作
 */
@Entity
@Table(name = "operation_log")
public class OperationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_type", nullable = false, length = 50)
    private String operationType;

    @Column(name = "operation_description", nullable = false)
    private String operationDescription;

    @Column(name = "operation_time", nullable = false)
    private LocalDateTime operationTime;

    @Lob
    @Column(name = "operation_params")
    private String operationParams;

    @Lob
    @Column(name = "operation_result")
    private String operationResult;

    @Column(name = "operation_ip", length = 50)
    private String operationIp;

    @Column(name = "operation_url")
    private String operationUrl;

    @Column(name = "http_method", length = 10)
    private String httpMethod;

    @Column(name = "operation_status", length = 20)
    private String operationStatus;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", length = 100)
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationDescription() {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription) {
        this.operationDescription = operationDescription;
    }

    public LocalDateTime getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(LocalDateTime operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationParams() {
        return operationParams;
    }

    public void setOperationParams(String operationParams) {
        this.operationParams = operationParams;
    }

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getOperationIp() {
        return operationIp;
    }

    public void setOperationIp(String operationIp) {
        this.operationIp = operationIp;
    }

    public String getOperationUrl() {
        return operationUrl;
    }

    public void setOperationUrl(String operationUrl) {
        this.operationUrl = operationUrl;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "OperationLog{" +
                "id=" + id +
                ", operationType='" + operationType + '\'' +
                ", operationDescription='" + operationDescription + '\'' +
                ", operationTime=" + operationTime +
                ", operationIp='" + operationIp + '\'' +
                ", operationStatus='" + operationStatus + '\'' +
                '}';
    }
} 