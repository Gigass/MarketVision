package com.gigass.trading.aspect;

import com.gigass.trading.entity.OperationLog;
import com.gigass.trading.repository.OperationLogRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面
 * 用于记录系统中的所有操作
 */
@Aspect
@Component
public class OperationLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    @Autowired
    private OperationLogRepository operationLogRepository;

    /**
     * 定义切点 - 所有controller包下的方法
     */
    @Pointcut("execution(* com.gigass.*.controller.*.*(..))")
    public void controllerPointcut() {
    }

    /**
     * 定义切点 - 所有service包下的方法
     */
    @Pointcut("execution(* com.gigass.*.service.*.*(..))")
    public void servicePointcut() {
    }

    /**
     * 定义切点 - 所有module包下的方法
     */
    @Pointcut("execution(* com.gigass.*.module.*.*.*(..))")
    public void modulePointcut() {
    }

    /**
     * 前置通知：在方法执行前记录日志
     */
    @Before("controllerPointcut() || servicePointcut() || modulePointcut()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
            String ip = request != null ? getIpAddress(request) : "unknown";
            String url = request != null ? request.getRequestURL().toString() : "unknown";
            String httpMethod = request != null ? request.getMethod() : "unknown";

            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();
            Object[] args = joinPoint.getArgs();

            // 记录操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setOperationType("METHOD_CALL");
            operationLog.setOperationDescription(className + "." + methodName);
            operationLog.setOperationTime(LocalDateTime.now());
            operationLog.setOperationParams(Arrays.toString(args));
            operationLog.setOperationIp(ip);
            operationLog.setOperationUrl(url);
            operationLog.setHttpMethod(httpMethod);
            operationLog.setOperationStatus("STARTED");

            // 保存操作日志
            operationLogRepository.insert(operationLog);

            logger.debug("操作开始: {}.{}, 参数: {}", className, methodName, Arrays.toString(args));
        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
        }
    }

    /**
     * 返回通知：方法正常返回后记录日志
     */
    @AfterReturning(pointcut = "controllerPointcut() || servicePointcut() || modulePointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        try {
            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();

            // 记录操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setOperationType("METHOD_RETURN");
            operationLog.setOperationDescription(className + "." + methodName);
            operationLog.setOperationTime(LocalDateTime.now());
            operationLog.setOperationResult(result != null ? result.toString() : "void");
            operationLog.setOperationStatus("COMPLETED");

            // 保存操作日志
            operationLogRepository.insert(operationLog);

            logger.debug("操作完成: {}.{}, 结果: {}", className, methodName, result);
        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
        }
    }

    /**
     * 异常通知：方法抛出异常后记录日志
     */
    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut() || modulePointcut()", throwing = "exception")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        try {
            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();

            // 记录操作日志
            OperationLog operationLog = new OperationLog();
            operationLog.setOperationType("METHOD_EXCEPTION");
            operationLog.setOperationDescription(className + "." + methodName);
            operationLog.setOperationTime(LocalDateTime.now());
            operationLog.setOperationResult(exception.getMessage());
            operationLog.setOperationStatus("FAILED");

            // 保存操作日志
            operationLogRepository.insert(operationLog);

            logger.error("操作异常: {}.{}, 异常: {}", className, methodName, exception.getMessage());
        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
        }
    }

    /**
     * 获取请求的IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 