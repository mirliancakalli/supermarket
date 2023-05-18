package com.example.supermarket.dto;

public enum SelectionEnum {
    ID("id", Long.class),
    CARD_ID("cardId", Long.class),
    PHONE_NUMBER("phoneNumber", String.class);

    private final String key;
    private final Class<?> type;

    SelectionEnum(String key, Class<?> type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public Class<?> getType() {
        return type;
    }
}
