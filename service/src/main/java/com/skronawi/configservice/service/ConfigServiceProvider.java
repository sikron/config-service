package com.skronawi.configservice.service;

import com.skronawi.configservice.api.ConfigService;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.NotInitializedException;

public interface ConfigServiceProvider {

    void init() throws AlreadyInitializedException;

    void teardown();

    ConfigService get() throws NotInitializedException;
}
