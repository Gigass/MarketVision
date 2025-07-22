package com.jolywood.trading.module.acquisition;

import com.jolywood.trading.dto.HotSearchData;
import java.util.List;

public interface SocialMediaCrawler {
    
    /**
     * 获取热搜数据
     * @param limit 限制数量
     * @return 热搜数据列表
     */
    List<HotSearchData> fetchHotSearchData(int limit);
    
    /**
     * 获取平台名称
     * @return 平台名称
     */
    String getPlatformName();
    
    /**
     * 检查爬虫是否可用
     * @return 是否可用
     */
    boolean isAvailable();
    
    /**
     * 获取数据源URL
     * @return 数据源URL
     */
    String getDataSourceUrl();
}