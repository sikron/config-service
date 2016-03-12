package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.NotInitializedException;

import java.util.Set;

public interface LifeCycle {

    void init(ConfigServiceConfiguration configServiceConfiguration) throws AlreadyInitializedException;

    void load(Set<Configuration> configurations) throws MandatoryConfigurationException, NotInitializedException;

    void reload() throws MandatoryConfigurationException, NotInitializedException;

    void teardown();

    boolean isInitialized();

    ConfigServiceConfiguration getServiceConfiguration() throws NotInitializedException;

    Set<Configuration> getLoadConfigurations();
}
