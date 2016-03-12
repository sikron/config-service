package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.core.SourceLoadingException;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentLoadingStrategy extends AbstractSourceLoadingStrategy {

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {
        try {
            //for environment variables
            Map<String, String> prefixedKeyValues = new HashMap<>();
            System.getenv().entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().startsWith(name))
                    .forEach(entry -> prefixedKeyValues.put(entry.getKey(), entry.getValue()));
            return LoaderUtil.removeKeyPrefix(name, prefixedKeyValues);
        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
