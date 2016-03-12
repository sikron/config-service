package com.skronawi.configservice.api;

import java.util.Map;

public class KeyValuesConfigSourceConfiguration extends ConfigSourceConfiguration {

    private Map<String, String> keyValues;

    public KeyValuesConfigSourceConfiguration(Map<String, String> keyValues) {
        this.keyValues = keyValues;
        sourceType = ConfigServiceConfiguration.SourceType.KV;
    }

    public Map<String, String> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(Map<String, String> keyValues) {
        this.keyValues = keyValues;
    }

    public void setSourceType(ConfigServiceConfiguration.SourceType sourceType) {
        throw new UnsupportedOperationException("this is a fix KeyValuesConfigSourceConfiguration, " +
                "its type cannot be changed. it can only be set programmatically, not by config");
    }
}
