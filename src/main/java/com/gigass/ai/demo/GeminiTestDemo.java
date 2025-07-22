package com.gigass.ai.demo;

import com.gigass.ai.dto.ChatRequest;
import com.gigass.ai.dto.ChatResponse;
import com.gigass.ai.service.AiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Gemini API 测试Demo
 */
@Component
@Order(1) // 优先执行，在其他Demo之前
public class GeminiTestDemo implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(GeminiTestDemo.class);

    @Autowired
    private AiService aiService;

    @Override
    public void run(String... args) {
        System.out.println("=== Gemini API 测试开始 ===");
        
        // 测试1: 简单问答
        testSimpleChat();
        
        // 测试2: 股票分析相关
        testStockAnalysis();
        
        // 测试3: 热搜分析相关
        testHotSearchAnalysis();
        
        System.out.println("=== Gemini API 测试完成 ===");
    }

    private void testSimpleChat() {
        System.out.println("\n--- 测试1: 简单问答 ---");
        
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage("你好，请简单介绍一下你自己");
        
        try {
            ChatResponse response = aiService.chat(request);
            System.out.println("请求: " + request.getMessage());
            System.out.println("响应: " + response.getContent());
            System.out.println("使用模型: " + response.getModel());
        } catch (Exception e) {
            logger.error("简单问答测试失败", e);
            System.out.println("测试失败: " + e.getMessage());
        }
    }

    private void testStockAnalysis() {
        System.out.println("\n--- 测试2: 股票分析 ---");
        
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage("请分析一下AI概念股的投资价值，包括风险和机会");
        
        try {
            ChatResponse response = aiService.chat(request);
            System.out.println("请求: " + request.getMessage());
            System.out.println("响应: " + response.getContent());
        } catch (Exception e) {
            logger.error("股票分析测试失败", e);
            System.out.println("测试失败: " + e.getMessage());
        }
    }

    private void testHotSearchAnalysis() {
        System.out.println("\n--- 测试3: 热搜分析 ---");
        
        ChatRequest request = new ChatRequest();
        request.setModel("gemini");
        request.setMessage("假设微博热搜出现'ChatGPT概念股大涨'，请分析这个热搜的投资信号强度，从情感倾向、传播速度、真实性等维度评分（1-10分）");
        
        try {
            ChatResponse response = aiService.chat(request);
            System.out.println("请求: " + request.getMessage());
            System.out.println("响应: " + response.getContent());
        } catch (Exception e) {
            logger.error("热搜分析测试失败", e);
            System.out.println("测试失败: " + e.getMessage());
        }
    }
}