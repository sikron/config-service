package com.skronawi.configservice.api;

public interface Property<T> {

    String getKey();

    T getValue();
}
