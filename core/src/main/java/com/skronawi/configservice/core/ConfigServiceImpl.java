package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigService;
import com.skronawi.configservice.api.LifeCycle;
import com.skronawi.configservice.api.PropertyReadAccess;
import com.skronawi.configservice.api.PropertyWriteAccess;
import com.skronawi.keyvalueservice.api.KeyValueService;
import com.skronawi.keyvalueservice.api.NotInitializedException;

public class ConfigServiceImpl implements ConfigService {

    private final LifeCycleImpl lifeCycle;

    public ConfigServiceImpl(KeyValueService keyValueService) {
        lifeCycle = new LifeCycleImpl(keyValueService);
    }

    @Override
    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    @Override
    public PropertyReadAccess getPropertyReadAccess() throws NotInitializedException {
        if (!lifeCycle.isInitialized()) {
            throw new NotInitializedException();
        }
        return new PropertyReadAccessImpl(lifeCycle.getKeyValueService().getKeyValueStore());
    }

    @Override
    public PropertyWriteAccess getPropertyWriteAccess() throws NotInitializedException {
        throw new UnsupportedOperationException("property write access not implemented");
    }
}
