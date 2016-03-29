package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.KeyNotExistingException;
import com.skronawi.keyvalueservice.api.NotInitializedException;

import java.util.Set;

public interface PropertyReadAccess {

    Property<String> getByKey(String key) throws KeyNotExistingException, NotInitializedException;

    Property<String> getStringByKeyOrDefault(String key, String defaultValue) throws NotInitializedException;

    Property<Boolean> getBooleanByKeyOrDefault(String key, boolean defaultValue) throws PropertyConversionException,
            NotInitializedException;

    Property<Integer> getIntByKeyOrDefault(String key, int defaultValue) throws PropertyConversionException,
            NotInitializedException;

    Property<Double> getDoubleByKeyOrDefault(String key, double defaultValue) throws PropertyConversionException,
            NotInitializedException;

    Set<String> getKeys() throws NotInitializedException;
}
