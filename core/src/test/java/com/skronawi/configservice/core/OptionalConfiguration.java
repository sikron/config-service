package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;

public class OptionalConfiguration extends ConfigSourceConfiguration {

    @Override
    public boolean isMandatory() {
        return false;
    }
}
