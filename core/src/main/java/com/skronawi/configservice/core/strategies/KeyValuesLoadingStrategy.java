package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.api.KeyValuesConfigSourceConfiguration;
import com.skronawi.configservice.core.SourceLoadingException;

import java.util.HashMap;
import java.util.Map;

public class KeyValuesLoadingStrategy extends AbstractSourceLoadingStrategy {

    private KeyValuesConfigSourceConfiguration configSource;

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {
        this.configSource = (KeyValuesConfigSourceConfiguration) configSourceConfiguration;
    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {
        try {
            Map<String, String> keyValues = new HashMap<>();
            configSource.getKeyValues().entrySet()
                    .stream()
                    .filter(entry -> String.valueOf(entry.getKey()).startsWith(name))
                    .forEach(entry -> keyValues.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
            return LoaderUtil.removeKeyPrefix(name, keyValues);
        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
