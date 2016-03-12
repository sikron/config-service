package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.api.ConfigSourceConfiguration;

public abstract class AbstractFromUrlSourceLoadingStrategy extends AbstractSourceLoadingStrategy {

    protected String url;

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {
        String url = configSourceConfiguration.getUrl();
        this.url = url == null || url.length() == 0 ? this.url : url;
    }
}
