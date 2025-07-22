package com.gigass.trading.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trading_records")
public class TradingRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "hot_search_id")
    private Long hotSearchId; // 关联的热搜记录ID
    
    @Column(name = "stock_code", nullable = false)
    private String stockCode; // 股票代码
    
    @Enumerated(EnumType.STRING)
    private TradeType tradeType; // 交易类型
    
    @Column(name = "entry_price", precision = 10, scale = 2)
    private BigDecimal entryPrice; // 入场价格
    
    @Column(name = "exit_price", precision = 10, scale = 2)
    private BigDecimal exitPrice; // 出场价格
    
    @Column(name = "quantity")
    private Integer quantity; // 交易数量
    
    @Column(name = "stop_loss", precision = 10, scale = 2)
    private BigDecimal stopLoss; // 止损价格
    
    @Column(name = "take_profit", precision = 10, scale = 2)
    private BigDecimal takeProfit; // 止盈价格
    
    @Column(name = "profit_loss", precision = 10, scale = 2)
    private BigDecimal profitLoss; // 盈亏
    
    @Enumerated(EnumType.STRING)
    private TradeStatus status; // 交易状态
    
    @Column(name = "entry_time")
    private LocalDateTime entryTime; // 入场时间
    
    @Column(name = "exit_time")
    private LocalDateTime exitTime; // 出场时间
    
    @Column(name = "created_time")
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