package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.SourceLoadingStrategy;

public abstract class AbstractSourceLoadingStrategy implements SourceLoadingStrategy {

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {
        //TODO prevent re-initialization
    }

    @Override
    public void teardown() {
        //TODO after teardown, no init must be possible
    }

    @Override
    public void assertValid() {

    }
}
