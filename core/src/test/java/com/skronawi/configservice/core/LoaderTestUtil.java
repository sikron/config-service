package com.skronawi.configservice.core;

import com.skronawi.configservice.core.strategies.LoaderUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class LoaderTestUtil {

    public static Map<String, String> loadTestKeyValues() throws IOException {
        Properties properties = new Properties();
        properties.load(LoaderTestUtil.class.getResourceAsStream("/test.properties"));
        return LoaderUtil.from(properties);
    }
}
