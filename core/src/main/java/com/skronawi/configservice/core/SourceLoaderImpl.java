package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.api.Configuration;
import com.skronawi.configservice.api.MandatoryConfigurationException;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.NotInitializedException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SourceLoaderImpl implements SourceLoader {

    private ConfigSourceConfiguration configSourceConfiguration;
    private SourceLoadingStrategy sourceLoadingStrategy;

    private AtomicBoolean isInitialized = new AtomicBoolean(false);
    //prevent re-initialization
    private AtomicBoolean hasBeenInitialized = new AtomicBoolean(false);

    public SourceLoaderImpl(SourceLoadingStrategy sourceLoadingStrategy,
                            ConfigSourceConfiguration configSourceConfiguration) {
        this.sourceLoadingStrategy = sourceLoadingStrategy;
        this.configSourceConfiguration = configSourceConfiguration;
    }

    @Override
    public Map<String, String> load(Configuration configuration) throws MandatoryConfigurationException {

        assertInitialized();

        Map<String, String> keyValues = new HashMap<>();
        try {
            keyValues = sourceLoadingStrategy.load(configuration.getName());
        } catch (Exception e) {
            //TODO log
            if (configSourceConfiguration.isMandatory()) {
                throw new MandatoryConfigurationException(configuration);
            }
        }
        return keyValues;
    }

    @Override
    public void init() throws AlreadyInitializedException {
        if (hasBeenInitialized.getAndSet(true)) {
            throw new AlreadyInitializedException();
        }
        sourceLoadingStrategy.init(configSourceConfiguration);
        isInitialized.set(true);
    }

    @Override
    public void teardown() {
        isInitialized.set(false);
        sourceLoadingStrategy.teardown();
    }

    protected void assertInitialized() {
        if (!isInitialized.get()) {
            throw new NotInitializedException();
        }
    }
}
