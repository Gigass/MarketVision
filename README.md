# MarketVision 热搜驱动交易系统

## 项目简介
MarketVision 是一个基于多智能体协作的热搜驱动量化交易决策系统，集成了AI信号筛选、自动化数据采集、智能策略生成、实时监控与反馈优化等功能，支持多平台热搜数据采集与自动化交易。

## 主要功能
- 多平台热搜数据定时采集（微博、百度、知乎等）
- AI信号筛选与多维度评分
- 股票关联分析与风险评估
- 智能交易策略生成与自动下单
- 实时行情与热搜监控
- 交易后绩效分析与反馈优化
- 全链路操作日志自动记录到数据库
- 完善的生命周期流程与可扩展架构

## 快速启动
1. **环境要求**：
   - JDK 21+
   - Maven 3.8+
   - MySQL 8+（可选，默认H2内存数据库）
   - Redis（可选）

2. **配置数据库**（可选）
   - 默认使用H2内存数据库，无需配置。
   - 如需切换到MySQL，请修改 `src/main/resources/application.yml` 中的 `spring.datasource` 配置。

3. **启动项目**
   ```bash
   mvn clean package
   java -jar target/MarketVision-1.0.0.jar
   ```
   或直接用IDE运行 `com.gigass.MarketVisionApplication`。

4. **访问接口**
   - 默认API前缀：`http://localhost:8080/api/`
   - H2控制台：`http://localhost:8080/h2-console`

## 目录结构
```
MarketVision/
├── src/main/java/com/gigass/
│   ├── MarketVisionApplication.java      # 启动类
│   ├── trading/                         # 交易主逻辑
│   ├── ai/                              # AI与智能体相关
│   ├── ...
├── src/main/resources/
│   ├── application.yml                  # 配置文件
│   └── docs/                            # 架构、生命周期、类结构等文档
│       ├── 架构设计.md
│       ├── 生命周期.md
│       └── 类结构说明.md
├── pom.xml
└── README.md
```

## 主要文档
- `src/main/resources/docs/架构设计.md`：系统架构与模块说明
- `src/main/resources/docs/生命周期.md`：生命周期与业务流程
- `src/main/resources/docs/类结构说明.md`：所有核心类与包结构说明

## 贡献与扩展
- 欢迎提交PR、Issue，或自定义扩展AI模型、交易策略、数据源等。
- 代码结构清晰，便于二次开发。

## 联系方式
如有问题或合作意向，请联系：`dev@gigass.com`

---

> 本项目仅供学习与研究，涉及真实交易请谨慎评估风险。 