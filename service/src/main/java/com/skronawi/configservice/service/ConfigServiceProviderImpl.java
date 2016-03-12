package com.skronawi.configservice.service;

import com.skronawi.configservice.api.ConfigService;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.KeyValueService;
import com.skronawi.keyvalueservice.api.NotInitializedException;
import com.skronawi.keyvalueservice.service.KeyValueServiceProvisioning;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigServiceProviderImpl implements ConfigServiceProvider {

    private AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private AtomicBoolean isInitialized = new AtomicBoolean(false);
    private AtomicBoolean hasBeenInitializedOnce = new AtomicBoolean(false);

    @Override
    public void init() throws AlreadyInitializedException{
        if (hasBeenInitializedOnce.getAndSet(true)) {
            throw new AlreadyInitializedException();
        }
        annotationConfigApplicationContext =
                new AnnotationConfigApplicationContext(KeyValueServiceProvisioning.class,
                        ConfigServiceProvisioning.class);
        isInitialized.set(true);
    }

    @Override
    public void teardown() {
        isInitialized.set(false);
        annotationConfigApplicationContext.getBean(ConfigService.class).getLifeCycle().teardown();
        annotationConfigApplicationContext.getBean(KeyValueService.class).getLifeCycle().teardown();
    }

    @Override
    public ConfigService get() throws NotInitializedException{
        assertInitialized();
        return annotationConfigApplicationContext.getBean(ConfigService.class);
    }

    private void assertInitialized() {
        if (!isInitialized.get()) {
            throw new NotInitializedException();
        }
    }
}
