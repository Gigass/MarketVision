package com.gigass.trading.module.assessment;

import com.gigass.trading.entity.HotSearchRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReverseVerifier {
    
    private static final Logger logger = LoggerFactory.getLogger(ReverseVerifier.class);
    
    /**
     * 反向验证 - 检查市场是否已经对信息产生反应，避免追高
     */
    public Double performReverseVerification(HotSearchRecord record) {
        logger.debug("执行反向验证: {}", record.getTitle());
        
        try {
            // 1. 检查股价是否已经反应
            Double priceReactionScore = checkPriceReaction(record);
            
            // 2. 检查交易量是否异常
            Double volumeAnomalyScore = checkVolumeAnomaly(record);
            
            // 3. 检查市场情绪是否过热
            Double sentimentOverheatScore = checkSentimentOverheat(record);
            
            // 4. 检查信息传播饱和度
            Double informationSaturationScore = checkInformationSaturation(record);
            
            // 综合计算反向验证分数（分数越高表示市场反应越充分，投资价值越低）
            Double reactionLevel = (priceReactionScore + volumeAnomalyScore + 
                sentimentOverheatScore + informationSaturationScore) / 4.0;
            
            // 反向计算：市场反应越充分，验证分数越低
            Double reverseVerificationScore = Math.max(100.0 - reactionLevel, 0.0);
            
            logger.debug("反向验证完成，市场反应程度: {}, 验证分数: {}", 
                reactionLevel, reverseVerificationScore);
            
            return reverseVerificationScore;
            
        } catch (Exception e) {
            logger.error("反向验证失败: {}", e.getMessage(), e);
            return 50.0; // 中性分数
        }
    }
    
    /**
     * 检查股价是否已经反应
     */
    private Double checkPriceReaction(HotSearchRecord record) {
        try {
            // TODO: 实现股价反应检查
            // 1. 获取相关股票的实时价格
            // 2. 对比热搜发布前后的价格变化
            // 3. 分析价格变化幅度和速度
            
            // 简化逻辑：基于热度和时间估算
            Integer heatScore = record.getHeatScore();
            if (heatScore == null) heatScore = 0;
            
            // 热度越高，假设股价反应越充分
            return Math.min(heatScore / 15.0, 100.0);
            
        } catch (Exception e) {
            logger.warn("股价反应检查失败: {}", e.getMessage());
            return 30.0;
        }
    }
    
    /**
     * 检查交易量是否异常
     */
    private Double checkVolumeAnomaly(HotSearchRecord record) {
        try {
            // TODO: 实现交易量异常检查
            // 1. 获取相关股票的实时交易量
            // 2. 对比历史平均交易量
            // 3. 计算交易量异常程度
            
            // 简化逻辑：基于传播速度估算
            Double propagationSpeed = record.getPropagationSpeed();
            if (propagationSpeed == null) propagationSpeed = 0.0;
            
            return Math.min(propagationSpeed * 0.8, 100.0);
            
        } catch (Exception e) {
            logger.warn("交易量异常检查失败: {}", e.getMessage());
            return 30.0;
        }
    }
    
    /**
     * 检查市场情绪是否过热
     */
    private Double checkSentimentOverheat(HotSearchRecord record) {
        try {
            Double sentimentScore = record.getSentimentScore();
            if (sentimentScore == null) sentimentScore = 50.0;
            
            // 情绪极端化程度：距离中性值50的距离
            double extremeLevel = Math.abs(sentimentScore - 50.0);
            
            // 情绪越极端，市场可能越过热
            return Math.min(extremeLevel * 2.0, 100.0);
            
        } catch (Exception e) {
            logger.warn("市场情绪过热检查失败: {}", e.getMessage());
            return 30.0;
        }
    }
    
    /**
     * 检查信息传播饱和度
     */
    private Double checkInformationSaturation(HotSearchRecord record) {
        try {
            // TODO: 实现信息传播饱和度检查
            // 1. 分析信息在各平台的传播程度
            // 2. 检查媒体报道数量
            // 3. 评估信息传播的饱和程度
            
            // 简化逻辑：基于来源和热度估算
            String source = record.getSource();
            Integer heatScore = record.getHeatScore();
            if (heatScore == null) heatScore = 0;
            
            double sourceMultiplier = getSourceSaturationMultiplier(source);
            
            return Math.min(heatScore / 20.0 * sourceMultiplier, 100.0);
            
        } catch (Exception e) {
            logger.warn("信息传播饱和度检查失败: {}", e.getMessage());
            return 30.0;
        }
    }
    
    /**
     * 获取来源饱和度乘数
     */
    private double getSourceSaturationMultiplier(String source) {
        if (source == null) return 1.0;
        
        switch (source.toLowerCase()) {
            case "weibo":
                return 1.3; // 微博传播广，饱和度高
            case "douyin":
                return 1.2; // 抖音影响大
            case "zhihu":
                return 0.8; // 知乎相对小众
            default:
                return 1.0;
        }
    }
}