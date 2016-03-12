package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.strategies.ClassPathLoadingStrategy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class ClassPathLoadingStrategyTest {

    private Map<String, String> keyValues;

    @BeforeClass
    public void setup() throws Exception {
        keyValues = LoaderTestUtil.loadTestKeyValues();
    }

    @Test
    public void testLoad() throws Exception {

        ConfigSourceConfiguration configSourceConfiguration = new ConfigSourceConfiguration();
        configSourceConfiguration.setUrl("/");

        ClassPathLoadingStrategy classPathLoadingStrategy = new ClassPathLoadingStrategy();
        classPathLoadingStrategy.init(configSourceConfiguration);
        Map<String, String> keyValues = classPathLoadingStrategy.load("test");

        Assert.assertEquals(keyValues, this.keyValues);
    }
}
