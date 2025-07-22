package com.jolywood.trading.module.assessment;

import com.jolywood.trading.entity.HotSearchRecord;
import com.jolywood.trading.entity.StockInfo;
import com.jolywood.trading.repository.StockInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FundFlowPredictor {
    
    private static final Logger logger = LoggerFactory.getLogger(FundFlowPredictor.class);
    
    @Autowired
    private StockInfoRepository stockInfoRepository;
    
    /**
     * 资金流向预判 - 分析类似事件中资金流向模式，预测可能的资金动向
     */
    public Double predictFundFlow(HotSearchRecord record) {
        logger.debug("执行资金流向预判: {}", record.getTitle());
        
        try {
            // 1. 分析相关股票的资金吸引力
            Double stockAttractionScore = analyzeStockAttraction(record);
            
            // 2. 预测资金流入规模
            Double inflowScaleScore = predictInflowScale(record);
            
            // 3. 分析资金流向持续性
            Double sustainabilityScore = analyzeSustainability(record);
            
            // 4. 评估机构资金参与度
            Double institutionalParticipationScore = assessInstitutionalParticipation(record);
            
            // 综合计算资金流向预判分数
            Double fundFlowScore = (stockAttractionScore + inflowScaleScore + 
                sustainabilityScore + institutionalParticipationScore) / 4.0;
            
            logger.debug("资金流向预判完成，分数: {}", fundFlowScore);
            return fundFlowScore;
            
        } catch (Exception e) {
            logger.error("资金流向预判失败: {}", e.getMessage(), e);
            return 0.0;
        }
    }
    
    /**
     * 分析相关股票的资金吸引力
     */
    private Double analyzeStockAttraction(HotSearchRecord record) {
        try {
            List<StockInfo> relatedStocks = stockInfoRepository.findByKeyword(record.getTitle());
            
            if (relatedStocks.isEmpty()) {
                return 30.0; // 无相关股票，吸引力较低
            }
            
            // 计算股票质量分数
            double avgMarketCap = relatedStocks.stream()
                .filter(s -> s.getMarketCap() != null)
                .mapToDouble(StockInfo::getMarketCap)
                .average()
                .orElse(0.0);
            
            // 市值越大，资金吸引力越强（简化逻辑）
            return Math.min(avgMarketCap / 1000000000.0 * 20, 100.0); // 归一化
            
        } catch (Exception e) {
            logger.warn("股票吸引力分析失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 预测资金流入规模
     */
    private Double predictInflowScale(HotSearchRecord record) {
        try {
            // 基于热度分数预测资金流入规模
            Integer heatScore = record.getHeatScore();
            if (heatScore == null) heatScore = 0;
            
            // 热度越高，预期资金流入越大
            return Math.min(heatScore / 10.0, 100.0);
            
        } catch (Exception e) {
            logger.warn("资金流入规模预测失败: {}", e.getMessage());
            return 50.0;
        }
    }
    
    /**
     * 分析资金流向持续性
     */
    private Double analyzeSustainability(HotSearchRecord record) {
        // TODO: 实现资金流向持续性分析
        // 1. 事件类型分析
        // 2. 历史持续性数据
        // 3. 市场环境因素
        
        return 60.0; // 默认分数
    }
    
    /**
     * 评估机构资金参与度
     */
    private Double assessInstitutionalParticipation(HotSearchRecord record) {
        // TODO: 实现机构资金参与度评估
        // 1. 事件影响范围
        // 2. 相关股票机构持仓情况
        // 3. 政策敏感度
        
        return 55.0; // 默认分数
    }
}