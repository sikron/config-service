package com.skronawi.configservice.api;

import java.util.ArrayList;
import java.util.List;

//a configuration for a configservice, which only reads from classpath
public class DefaultConfigServiceConfiguration extends ConfigServiceConfiguration {

    private List<ConfigSourceConfiguration> configSourceConfigurations;

    public DefaultConfigServiceConfiguration() {
        configSourceConfigurations = new ArrayList<>();
        ConfigSourceConfiguration configSourceConfiguration = new ConfigSourceConfiguration();
        configSourceConfiguration.setUrl("/");
        configSourceConfiguration.setSourceType(SourceType.CLASSPATH);
        configSourceConfiguration.setOrder(1);
        configSourceConfigurations.add(configSourceConfiguration);
    }

    @Override
    public List<ConfigSourceConfiguration> getConfigSourceConfigurations() {
        return configSourceConfigurations;
    }
}
