package com.skronawi.configservice.core;

import com.skronawi.keyvalueservice.api.KeyNotExistingException;
import com.skronawi.keyvalueservice.api.KeyValueStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DummyKeyValueStore implements KeyValueStore {

    Map<String, String> keyValues = new HashMap<>();

    public DummyKeyValueStore(Map<String, String> keyValues) {
        this.keyValues = keyValues;
    }

    public DummyKeyValueStore() {
    }

    @Override
    public String get(String key) throws KeyNotExistingException {
        String value = keyValues.get(key);
        if (null == value) {
            throw new KeyNotExistingException("key not existing: " + key);
        }
        return value;
    }

    @Override
    public Map<String, String> getAll() {
        return new HashMap<>(keyValues);
    }

    @Override
    public Map<String, String> getByPrefix(String keyPrefix) {
        HashMap<String, String> prefixedKeyValues = new HashMap<>();
        keyValues.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(keyPrefix))
                .forEach(entry -> prefixedKeyValues.put(entry.getKey(), entry.getValue()));
        return prefixedKeyValues;
    }

    @Override
    public void set(String key, String value) {
        keyValues.put(key, value);
    }

    @Override
    public void setAll(Map<String, String> keyValues) {
        this.keyValues.putAll(keyValues);
    }

    @Override
    public void delete(String key) {
        keyValues.remove(key);
    }

    @Override
    public void deleteByPrefix(String keyPrefix) {
        Set<String> keysToRemove = new HashSet<>();
        keyValues.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(keyPrefix))
                .forEach(entry -> keysToRemove.add(entry.getKey()));
        for (String keyToRemove : keysToRemove) {
            keyValues.remove(keyToRemove);
        }
    }

    @Override
    public void deleteAll() {
        keyValues = new HashMap<>();
    }

    @Override
    public Set<String> getKeys() {
        return new HashSet<>(keyValues.keySet());
    }

    @Override
    public void init() {
    }

    @Override
    public void teardown() {
    }
}
