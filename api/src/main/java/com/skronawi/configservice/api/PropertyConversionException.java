package com.skronawi.configservice.api;

public class PropertyConversionException extends RuntimeException {

    public final TargetType targetType;
    public final String key;
    public final String value;

    public enum TargetType {
        BOOLEAN,
        DOUBLE,
        INTEGER
    }

    public PropertyConversionException(TargetType targetType, String key, String value) {
        this.targetType = targetType;
        this.key = key;
        this.value = value;
    }
}
