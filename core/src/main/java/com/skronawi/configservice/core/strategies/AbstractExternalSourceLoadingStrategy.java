package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.keyvalueservice.api.Credentials;

/*
external ~ credentials + url
 */
public abstract class AbstractExternalSourceLoadingStrategy extends AbstractFromUrlSourceLoadingStrategy {

    protected Credentials credentials;

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {
        super.init(configSourceConfiguration);
        credentials = configSourceConfiguration.getCredentials();
    }

    @Override
    public void assertValid() {
        assert credentials != null;
        assert credentials.getUsername() != null && credentials.getUsername().length() > 0;
        assert credentials.getPassword() != null && credentials.getPassword().length() > 0;
        assert url != null && url.length() > 0;
    }
}
