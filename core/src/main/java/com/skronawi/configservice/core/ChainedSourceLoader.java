package com.skronawi.configservice.core;

import com.skronawi.configservice.api.Configuration;
import com.skronawi.configservice.api.MandatoryConfigurationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChainedSourceLoader {

    private List<SourceLoader> sourceLoaders;

    public ChainedSourceLoader(List<SourceLoader> sourceLoaders) {
        this.sourceLoaders = sourceLoaders;
    }

    public Map<String, String> load(final Configuration configuration) throws MandatoryConfigurationException {

        //no stream possible, as 'chainedSourceLoader.load' throws MandatoryConfigurationException
        Map<String, String> keyValues = new HashMap<>();
        for (SourceLoader sourceLoader : sourceLoaders) {
            Map<String, String> sourceKeyValues = sourceLoader.load(configuration);
            keyValues.putAll(sourceKeyValues);
        }
        return keyValues;
    }
}
