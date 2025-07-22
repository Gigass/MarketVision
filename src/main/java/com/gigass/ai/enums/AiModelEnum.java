package com.gigass.ai.enums;

public enum AiModelEnum {
    OPENAI("openai", "OpenAI GPT"),
    GEMINI("gemini", "Google Gemini"),
    CLAUDE("claude", "Anthropic Claude");

    private final String code;
    private final String description;

    AiModelEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static AiModelEnum fromCode(String code) {
        for (AiModelEnum model : values()) {
            if (model.code.equals(code)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Unknown AI model: " + code);
    }
}