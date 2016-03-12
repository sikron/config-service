package com.skronawi.configservice.core;

import com.skronawi.configservice.api.Property;
import com.skronawi.configservice.api.PropertyConversionException;
import com.skronawi.configservice.api.PropertyReadAccess;
import com.skronawi.keyvalueservice.api.KeyNotExistingException;
import com.skronawi.keyvalueservice.api.KeyValueStore;
import com.skronawi.keyvalueservice.api.NotInitializedException;

import java.util.Set;

public class PropertyReadAccessImpl implements PropertyReadAccess {

    private final KeyValueStore keyValueStore;

    public PropertyReadAccessImpl(KeyValueStore keyValueStore) {
        this.keyValueStore = keyValueStore;
    }

    @Override
    public Property<String> getByKey(String key) throws KeyNotExistingException, NotInitializedException {
        String value = keyValueStore.get(key);
        if (null == value) {
            throw new KeyNotExistingException(key);
        }
        return new PropertyImpl<>(key, value);
    }

    @Override
    public Property<String> getStringByKeyOrDefault(String key, String defaultValue) {
        try {
            return getByKey(key);
        } catch (KeyNotExistingException e) {
            return new PropertyImpl<>(key, defaultValue);
        }
    }

    @Override
    public Property<Boolean> getBooleanByKeyOrDefault(String key, boolean defaultValue)
            throws PropertyConversionException, NotInitializedException {

        Property<String> property = getStringOrNullSilently(key);

        boolean value = defaultValue;
        if (property != null) {
            try {
                value = Boolean.parseBoolean(property.getValue());
            } catch (Exception e) {
                throw new PropertyConversionException(PropertyConversionException.TargetType.BOOLEAN, property.getKey(),
                        property.getValue());
            }
        }
        return new PropertyImpl<>(key, value);
    }

    @Override
    public Property<Integer> getIntByKeyOrDefault(String key, int defaultValue)
            throws PropertyConversionException, NotInitializedException {

        Property<String> property = getStringOrNullSilently(key);

        int value = defaultValue;
        if (property != null) {
            try {
                value = Integer.parseInt(property.getValue());
            } catch (Exception e) {
                throw new PropertyConversionException(PropertyConversionException.TargetType.INTEGER, property.getKey(),
                        property.getValue());
            }
        }
        return new PropertyImpl<>(key, value);
    }

    @Override
    public Property<Double> getDoubleByKeyOrDefault(String key, double defaultValue)
            throws PropertyConversionException, NotInitializedException {

        Property<String> property = getStringOrNullSilently(key);

        double value = defaultValue;
        if (property != null) {
            try {
                value = Double.parseDouble(property.getValue());
            } catch (Exception e) {
                throw new PropertyConversionException(PropertyConversionException.TargetType.DOUBLE, property.getKey(),
                        property.getValue());
            }
        }
        return new PropertyImpl<>(key, value);
    }

    @Override
    public Set<String> getKeys() throws NotInitializedException {
        return keyValueStore.getKeys();
    }

    private Property<String> getStringOrNullSilently(String key) {
        Property<String> property = null;
        try {
            property = getByKey(key);
        } catch (KeyNotExistingException e) {
            // ok
        }
        return property;
    }
}
