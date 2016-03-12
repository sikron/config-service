package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.core.SourceLoadingException;

import java.util.HashMap;
import java.util.Map;

public class SystemLoadingStrategy extends AbstractSourceLoadingStrategy {

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {
        try {
            //for "java -Dtest="true" -jar myApplication.jar"
            Map<String, String> prefixedKeyValues = new HashMap<>();
            System.getProperties().entrySet()
                    .stream()
                    .filter(entry -> String.valueOf(entry.getKey()).startsWith(name))
                    .forEach(entry -> prefixedKeyValues.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
            return LoaderUtil.removeKeyPrefix(name, prefixedKeyValues);
        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
