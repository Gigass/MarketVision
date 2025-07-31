package com.gigass.trading.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HotSearchResponse {
    private Integer code;
    private String name;
    private String title;
    private String type;
    private String description;
    private String link;
    private Integer total;
    private Boolean fromCache;
    private LocalDateTime updateTime;
    private List<HotSearchItem> data;

    @Data
    public static class HotSearchItem {
        private String id;
        private String title;
        private Long timestamp;
        private Long hot;
        private String url;
        private String mobileUrl;
    }
}