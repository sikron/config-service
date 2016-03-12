package com.skronawi.configservice.core.strategies;

import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class LoaderUtil {

    public static Map<String, String> from(Properties properties) {
        return properties.keySet()
                .stream()
                .collect(
                        Collectors.toMap(String::valueOf, key -> String.valueOf(properties.get(key))));
    }

    public static Map<String, String> removeKeyPrefix(String prefix, Map<String, String> keyValues) {
        return keyValues.keySet()
                .stream()
                .collect(
                        Collectors.toMap(key -> key.substring(prefix.length(), key.length()), keyValues::get));
    }
}
