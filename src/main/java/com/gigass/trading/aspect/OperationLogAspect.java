package com.gigass.trading.aspect;

import com.gigass.trading.service.LogService;
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
    private LogService logService;

    /**
     * 定义切点 - 所有controller包下的方法
     */
    @Pointcut("execution(* com.gigass.trading.controller..*(..))")
    public void controllerPointcut() {}

    /**
     * 定义切点 - 所有service包下的方法
     */
    @Pointcut("execution(* com.gigass.trading.service..*(..))")
    public void servicePointcut() {}

    /**
     * 定义切点 - 所有module包下的方法
     */
    @Pointcut("execution(* com.gigass.*.module.*.*.*(..))")
    public void modulePointcut() {
    }

    /**
     * 定义切点 - 使用了 OperationLog 注解的方法
     */
    @Pointcut("@annotation(com.gigass.trading.annotation.OperationLog)")
    public void operationLogPointcut() {}

    /**
     * 前置通知：在方法执行前记录日志到 Elasticsearch
     */
    @Before("operationLogPointcut()")
    public void doBefore(JoinPoint joinPoint) {
        // 避免递归和测试代码
        String className = joinPoint.getTarget().getClass().getName();
        if (className.contains("LogService") || 
            className.contains("Demo") || 
            className.contains("Repository") ||
            className.contains("Test")) {
            return;
        }

        try {
            String methodName="";
            // 只记录重要的业务操作到ES
            if (isImportantOperation(joinPoint)) {
                // 获取请求信息
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = attributes != null ? attributes.getRequest() : null;
                String ip = request != null ? getIpAddress(request) : "unknown";
                String url = request != null ? request.getRequestURL().toString() : "unknown";
                String httpMethod = request != null ? request.getMethod() : "unknown";

                // 获取方法签名
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                 methodName = method.getName();
                Object[] args = joinPoint.getArgs();

                logService.logOperation(
                    "METHOD_CALL",
                    className + "." + methodName,
                    Arrays.toString(args),
                    ip,
                    url,
                    httpMethod,
                    "STARTED"
                );
            }
            // 控制台日志降级为TRACE，减少输出
            logger.trace("操作开始: {}.{}", className, methodName);

        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
        }
    }

    /**
     * 返回通知：方法正常返回后记录日志到 Elasticsearch
     */
    @AfterReturning(pointcut = "controllerPointcut() || servicePointcut() || modulePointcut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 避免递归和测试代码
        String className = joinPoint.getTarget().getClass().getName();
        if (className.contains("LogService") || 
            className.contains("Demo") || 
            className.contains("Repository") ||
            className.contains("Test") ||
            className.contains("Scheduling")) {  // 添加定时任务排除
            return;
        }
        String methodName ="";
        try {
            // 只记录重要的业务操作
            if (isImportantOperation(joinPoint)) {
                // 获取方法签名
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Method method = signature.getMethod();
                methodName = method.getName();

                logService.logOperation(
                    "METHOD_SUCCESS",
                    className + "." + methodName,
                    result != null ? result.toString() : "null",
                    "unknown",
                    "unknown", 
                    "unknown",
                    "SUCCESS"
                );
            }
            
            logger.trace("操作成功: {}.{}", className, methodName);
        } catch (Exception e) {
            logger.error("记录操作日志失败", e);
        }
    }

    /**
     * 异常通知：方法抛出异常后记录日志到 Elasticsearch
     */
    @AfterThrowing(pointcut = "operationLogPointcut()", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        try {
            // 避免对LogService本身进行日志记录，防止无限递归
            if (joinPoint.getTarget().getClass().getSimpleName().contains("LogService")) {
                return;
            }
            
            // 获取方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = method.getName();

            // 只记录到 Elasticsearch
            logService.logOperation(
                "METHOD_EXCEPTION",
                className + "." + methodName,
                ex.getMessage(),
                "unknown",
                "unknown",
                "unknown",
                "FAILED"
            );

            logger.error("操作异常: {}.{}, 异常: {}", className, methodName, ex.getMessage());
        } catch (Exception e) {
            // 静默处理，避免影响主业务
            System.err.println("日志记录失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
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
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private boolean isImportantOperation(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        
        // 只记录重要的业务方法
        return methodName.startsWith("save") || 
               methodName.startsWith("update") || 
               methodName.startsWith("delete") ||
               methodName.startsWith("create") ||
               className.contains("Controller");
    }
}
