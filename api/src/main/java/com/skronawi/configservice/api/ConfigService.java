package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.NotInitializedException;

public interface ConfigService {

    LifeCycle getLifeCycle();

    PropertyReadAccess getPropertyReadAccess() throws NotInitializedException;

    PropertyWriteAccess getPropertyWriteAccess() throws NotInitializedException;
}
