package com.gigass.trading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("trading_records")
public class TradingRecord {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("hot_search_id")
    private Long hotSearchId; // 关联的热搜记录ID
    
    @TableField("stock_code")
    private String stockCode; // 股票代码
    
    @TableField("trade_type")
    private TradeType tradeType; // 交易类型
    
    @TableField("entry_price")
    private BigDecimal entryPrice; // 入场价格
    
    @TableField("exit_price")
    private BigDecimal exitPrice; // 出场价格
    
    @TableField("quantity")
    private Integer quantity; // 交易数量
    
    @TableField("stop_loss")
    private BigDecimal stopLoss; // 止损价格
    
    @TableField("take_profit")
    private BigDecimal takeProfit; // 止盈价格
    
    @TableField("profit_loss")
    private BigDecimal profitLoss; // 盈亏
    
    @TableField("status")
    private TradeStatus status; // 交易状态
    
    @TableField("entry_time")
    private LocalDateTime entryTime; // 入场时间
    
    @TableField("exit_time")
    private LocalDateTime exitTime; // 出场时间
    
    @TableField("created_time")
    private LocalDateTime createdTime;
    
    public TradingRecord() {
        this.createdTime = LocalDateTime.now();
        this.status = TradeStatus.PENDING;
    }
    
    public enum TradeType {
        BUY, SELL
    }
    
    public enum TradeStatus {
        PENDING, EXECUTED, MONITORING, CLOSED, CANCELLED
    }
    
    // TODO: 添加完整的getter/setter方法
}