package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.strategies.FileLoadingStrategy;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

public class FileLoadingStrategyTest {

    private Map<String, String> keyValues;
    private String absolutePath;

    @BeforeClass
    public void setup() throws Exception {

        keyValues = LoaderTestUtil.loadTestKeyValues();

        absolutePath = new File("src/test/resources/").getAbsolutePath(); //no slash at the end!
    }

    @Test
    public void testLoad() throws Exception {

        ConfigSourceConfiguration configSourceConfiguration = new ConfigSourceConfiguration();
        configSourceConfiguration.setUrl(absolutePath + "/");

        FileLoadingStrategy fileLoadingStrategy = new FileLoadingStrategy();
        fileLoadingStrategy.init(configSourceConfiguration);
        Map<String, String> keyValues = fileLoadingStrategy.load("test");

        Assert.assertEquals(keyValues, this.keyValues);
    }
}
