package com.gigass.trading.dto;

public class HotSearchData {
    private String source;
    private String title;
    private String content;
    private int heatIndex;

    // Getters and Setters
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getHeatIndex() {
        return heatIndex;
    }

    public void setHeatIndex(int heatIndex) {
        this.heatIndex = heatIndex;
    }
}