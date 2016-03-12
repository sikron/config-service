package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.NotInitializedException;

import java.util.Map;

public interface PropertyWriteAccess {

    Property setByKey(String key, String value) throws NotInitializedException;

    void set(Map<String, String> keyValues) throws NotInitializedException;

    void deleteByKey(String key) throws NotInitializedException;

    void deleteByKeyPrefix(String prefix) throws NotInitializedException;
}
