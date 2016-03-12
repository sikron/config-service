package com.skronawi.configservice.core;

import com.skronawi.configservice.core.strategies.SystemLoadingStrategy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class SystemLoadingStrategyTest {

    private Map<String, String> keyValues;

    @BeforeClass
    public void setup() throws Exception {

        keyValues = LoaderTestUtil.loadTestKeyValues();

        setSystemProperties();
    }

    private void setSystemProperties() {
        keyValues.entrySet().stream().forEach(entry -> System.setProperty("test." + entry.getKey(), entry.getValue()));
    }

    @Test
    public void testLoad() throws Exception {

        SystemLoadingStrategy systemLoadingStrategy = new SystemLoadingStrategy();
        Map<String, String> keyValues = systemLoadingStrategy.load("test.");

        Assert.assertEquals(keyValues, this.keyValues);
    }
}
