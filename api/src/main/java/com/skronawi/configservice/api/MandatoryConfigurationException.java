package com.skronawi.configservice.api;

public class MandatoryConfigurationException extends RuntimeException {

    private final Configuration configuration;

    public MandatoryConfigurationException(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
