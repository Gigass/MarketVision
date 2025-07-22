package com.gigass.trading.module.acquisition;

import com.gigass.trading.dto.HotSearchData;
import java.util.List;

public interface SocialMediaCrawler {
    boolean isAvailable();
    List<HotSearchData> fetchHotSearchData(int count);
} 