package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;

import java.util.Map;

public class ThrowingStrategy implements SourceLoadingStrategy {

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {

    }

    @Override
    public void teardown() {

    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {
        throw new SourceLoadingException("ThrowingStrategy");
    }

    @Override
    public void assertValid() {

    }
}
