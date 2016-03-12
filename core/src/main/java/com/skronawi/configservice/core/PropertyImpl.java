package com.skronawi.configservice.core;

import com.skronawi.configservice.api.Property;

public class PropertyImpl<T> implements Property<T> {

    private String key;
    private T value;

    public PropertyImpl(String key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
