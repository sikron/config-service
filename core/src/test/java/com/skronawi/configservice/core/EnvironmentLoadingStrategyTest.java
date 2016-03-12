package com.skronawi.configservice.core;

import com.skronawi.configservice.core.strategies.EnvironmentLoadingStrategy;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentLoadingStrategyTest {

    private Map<String, String> prefixedKeyValues;
    private Map<String, String> keyValues;

    @BeforeClass
    public void setup() throws Exception {

        keyValues = LoaderTestUtil.loadTestKeyValues();
        prefixedKeyValues = new HashMap<>();
        keyValues.entrySet().stream().forEach(entry -> prefixedKeyValues.put("test." + entry.getKey(), entry.getValue()));
    }

    //http://stackoverflow.com/questions/1629841/how-to-jmockit-system-getenvstring
    @Test
    public void testLoad(@Mocked System system) throws Exception {

        new NonStrictExpectations() {
            {
                System.getenv();
                times = 1;
                result = prefixedKeyValues;
            }
        };

        EnvironmentLoadingStrategy environmentLoadingStrategy = new EnvironmentLoadingStrategy();
        Map<String, String> keyValues = environmentLoadingStrategy.load("test.");

        Assert.assertEquals(keyValues, this.keyValues);
    }
}
