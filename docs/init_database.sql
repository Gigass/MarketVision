-- MarketVision 数据库初始化脚本
-- 移除操作日志表，只保留业务表

USE marketvision;

-- 1. 热搜记录表
CREATE TABLE IF NOT EXISTS `hot_search_records` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `source` varchar(50) NOT NULL COMMENT '数据源：weibo, baidu, zhihu等',
  `title` varchar(500) NOT NULL COMMENT '热搜标题',
  `content` text COMMENT '热搜内容',
  `heat_score` int DEFAULT NULL COMMENT '热度分数',
  `sentiment_score` double DEFAULT NULL COMMENT '情感分数',
  `propagation_speed` double DEFAULT NULL COMMENT '传播速度',
  `verification_score` double DEFAULT NULL COMMENT '验证分数',
  `total_score` double DEFAULT NULL COMMENT '总分',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_source` (`source`),
  KEY `idx_created_time` (`created_time`),
  KEY `idx_total_score` (`total_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热搜记录表';

-- 2. 热搜历史表
CREATE TABLE IF NOT EXISTS `hotsearch_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `keyword` varchar(200) NOT NULL COMMENT '关键词',
  `timestamp` datetime NOT NULL COMMENT '时间戳',
  `heat` int DEFAULT NULL COMMENT '热度值',
  `related_stocks` json DEFAULT NULL COMMENT '关联股票(JSON格式)',
  PRIMARY KEY (`id`),
  KEY `idx_keyword` (`keyword`),
  KEY `idx_timestamp` (`timestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='热搜历史表';

-- 3. 股票信息表
CREATE TABLE IF NOT EXISTS `stock_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `stock_code` varchar(20) NOT NULL COMMENT '股票代码',
  `stock_name` varchar(100) NOT NULL COMMENT '股票名称',
  `market` varchar(10) NOT NULL COMMENT '市场：SH, SZ',
  `industry` varchar(100) DEFAULT NULL COMMENT '行业',
  `concept` json DEFAULT NULL COMMENT '概念(JSON格式)',
  `current_price` decimal(10,3) DEFAULT NULL COMMENT '当前价格',
  `market_cap` decimal(20,2) DEFAULT NULL COMMENT '市值',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '是否活跃',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stock_code` (`stock_code`),
  KEY `idx_market` (`market`),
  KEY `idx_industry` (`industry`),
  KEY `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股票信息表';

-- 4. 市场数据表
CREATE TABLE IF NOT EXISTS `market_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `stock_code` varchar(20) NOT NULL COMMENT '股票代码',
  `date` date NOT NULL COMMENT '交易日期',
  `industry` varchar(100) DEFAULT NULL COMMENT '行业',
  `open_price` decimal(10,3) NOT NULL COMMENT '开盘价',
  `close_price` decimal(10,3) NOT NULL COMMENT '收盘价',
  `high_price` decimal(10,3) NOT NULL COMMENT '最高价',
  `low_price` decimal(10,3) NOT NULL COMMENT '最低价',
  `volume` bigint NOT NULL COMMENT '成交量',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_stock_date` (`stock_code`, `date`),
  KEY `idx_date` (`date`),
  KEY `idx_industry` (`industry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='市场数据表';

-- 5. 交易记录表
CREATE TABLE IF NOT EXISTS `trading_records` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `hot_search_id` bigint DEFAULT NULL COMMENT '关联的热搜记录ID',
  `stock_code` varchar(20) NOT NULL COMMENT '股票代码',
  `trade_type` enum('BUY','SELL') NOT NULL COMMENT '交易类型',
  `entry_price` decimal(10,3) DEFAULT NULL COMMENT '入场价格',
  `exit_price` decimal(10,3) DEFAULT NULL COMMENT '出场价格',
  `quantity` int NOT NULL COMMENT '交易数量',
  `stop_loss` decimal(10,3) DEFAULT NULL COMMENT '止损价格',
  `take_profit` decimal(10,3) DEFAULT NULL COMMENT '止盈价格',
  `profit_loss` decimal(15,3) DEFAULT NULL COMMENT '盈亏',
  `status` enum('PENDING','EXECUTED','MONITORING','CLOSED','CANCELLED') DEFAULT 'PENDING' COMMENT '交易状态',
  `entry_time` datetime DEFAULT NULL COMMENT '入场时间',
  `exit_time` datetime DEFAULT NULL COMMENT '出场时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_hot_search_id` (`hot_search_id`),
  KEY `idx_stock_code` (`stock_code`),
  KEY `idx_status` (`status`),
  KEY `idx_entry_time` (`entry_time`),
  CONSTRAINT `fk_trading_hot_search` FOREIGN KEY (`hot_search_id`) REFERENCES `hot_search_records` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='交易记录表';

-- 创建完成提示
SELECT 'MarketVision 数据库表结构创建完成！(不包含操作日志表，日志记录到 Elasticsearch)' as message;