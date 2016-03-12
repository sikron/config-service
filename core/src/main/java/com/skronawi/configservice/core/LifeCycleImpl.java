package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigServiceConfiguration;
import com.skronawi.configservice.api.Configuration;
import com.skronawi.configservice.api.LifeCycle;
import com.skronawi.configservice.api.MandatoryConfigurationException;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.KeyValueService;
import com.skronawi.keyvalueservice.api.NotInitializedException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LifeCycleImpl implements LifeCycle {

    private KeyValueService keyValueService;
    private Set<Configuration> configurations;
    private ChainedSourceLoader chainedSourceLoader;
    private List<SourceLoader> sourceLoaders;
    private ConfigServiceConfiguration configServiceConfiguration;
    private AtomicBoolean isInitialized = new AtomicBoolean(false);
    //prevent re-initialization. thus any provided key-store cannot get in-valid or stale.
    private AtomicBoolean hasBeenInitializedOnce = new AtomicBoolean(false);

    public LifeCycleImpl(KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
        configurations = new HashSet<>();
    }

    @Override
    public void init(ConfigServiceConfiguration configServiceConfiguration) throws AlreadyInitializedException {

        if (hasBeenInitializedOnce.getAndSet(true)) {
            throw new AlreadyInitializedException();
        }

        sourceLoaders = SourceLoaderFactory.from(configServiceConfiguration.getConfigSourceConfigurations());
        for (SourceLoader sourceLoader : sourceLoaders) {
            sourceLoader.init();
        }

        configurations = new HashSet<>();
        chainedSourceLoader = new ChainedSourceLoader(sourceLoaders);

        this.configServiceConfiguration = configServiceConfiguration;

        isInitialized.set(true);
    }

    @Override
    public void load(Set<Configuration> configurations) throws MandatoryConfigurationException,
            NotInitializedException {
        this.configurations = configurations;
        loadImpl(configurations);
    }

    private void loadImpl(Set<Configuration> configurations) throws MandatoryConfigurationException,
            NotInitializedException {

        assertInitialized();

        //no stream possible, as 'chainedSourceLoader.load' throws MandatoryConfigurationException
        Map<String, String> keyValues = new HashMap<>();
        for (Configuration configuration : configurations) {
            keyValues.putAll(chainedSourceLoader.load(configuration));
        }
        keyValueService.getKeyValueStore().setAll(keyValues);
    }

    @Override
    public void reload() throws MandatoryConfigurationException, NotInitializedException {
        loadImpl(configurations);
    }

    @Override
    public void teardown() {
        if (!isInitialized.getAndSet(false)) {
            return;
        }
        sourceLoaders.stream().forEach(SourceLoader::teardown);
    }

    @Override
    public boolean isInitialized() {
        return isInitialized.get();
    }

    @Override
    public ConfigServiceConfiguration getServiceConfiguration() throws NotInitializedException {
        assertInitialized();
        return configServiceConfiguration;
    }

    @Override
    public Set<Configuration> getLoadConfigurations() {
        return configurations;
    }

    public KeyValueService getKeyValueService() throws NotInitializedException {
        assertInitialized();
        return keyValueService;
    }

    private void assertInitialized() throws NotInitializedException {
        if (!isInitialized()) {
            throw new NotInitializedException();
        }
    }
}
