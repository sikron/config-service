package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigServiceConfiguration;
import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.api.DefaultConfigServiceConfiguration;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class DefaultConfigTest {

    @Test
    public void test() throws Exception {

        ConfigServiceImpl configService = new ConfigServiceImpl();
        configService.getLifeCycle().init(new DefaultConfigServiceConfiguration());

        List<ConfigSourceConfiguration> configSourceConfigurations = configService.getLifeCycle()
                .getServiceConfiguration().getConfigSourceConfigurations();
        Assert.assertEquals(configSourceConfigurations.size(), 1);

        ConfigSourceConfiguration sourceConfiguration = configSourceConfigurations.iterator().next();
        Assert.assertEquals(sourceConfiguration.getOrder(), 1);
        Assert.assertEquals(sourceConfiguration.getUrl(), "/");
        Assert.assertEquals(sourceConfiguration.getSourceType(), ConfigServiceConfiguration.SourceType.CLASSPATH);
    }
}
