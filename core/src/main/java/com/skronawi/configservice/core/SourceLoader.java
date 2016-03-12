package com.skronawi.configservice.core;

import com.skronawi.configservice.api.Configuration;
import com.skronawi.configservice.api.MandatoryConfigurationException;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;

import java.util.Map;

public interface SourceLoader {
    Map<String, String> load(Configuration configuration) throws MandatoryConfigurationException;

    void init() throws AlreadyInitializedException;

    void teardown();
}
