package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigServiceConfiguration;
import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.strategies.*;

import java.util.List;
import java.util.stream.Collectors;

public class SourceLoaderFactory {

    public static List<SourceLoader> from(List<ConfigSourceConfiguration> configSourceConfigurations) {
        return configSourceConfigurations.stream().map(SourceLoaderFactory::from).collect(Collectors.toList());
    }

    public static SourceLoader from(ConfigSourceConfiguration configSourceConfiguration) {

        SourceLoadingStrategy strategy = null;
        ConfigServiceConfiguration.SourceType type = configSourceConfiguration.getSourceType();
        if (type == ConfigServiceConfiguration.SourceType.CLASSPATH) {
            strategy = new ClassPathLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.DB) {
            strategy = new DatabaseLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.ENV) {
            strategy = new EnvironmentLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.FILE) {
            strategy = new FileLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.JNDI) {
            strategy = new JndiLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.REST) {
            strategy = new RestLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.SYSTEM) {
            strategy = new SystemLoadingStrategy();
        }
        if (type == ConfigServiceConfiguration.SourceType.KV) {
            strategy = new KeyValuesLoadingStrategy();
        }
        if (strategy == null) {
            throw new IllegalArgumentException("type unknown: " + type);
        }
        return new SourceLoaderImpl(strategy, configSourceConfiguration);
    }
}
