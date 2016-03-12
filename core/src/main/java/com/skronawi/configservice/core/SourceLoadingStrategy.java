package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;

import java.util.Map;

public interface SourceLoadingStrategy {

    void init(ConfigSourceConfiguration configSourceConfiguration);

    void teardown();

    Map<String, String> load(String name) throws SourceLoadingException;

    void assertValid();
}
