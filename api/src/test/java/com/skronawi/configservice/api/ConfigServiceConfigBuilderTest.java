package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.Credentials;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class ConfigServiceConfigBuilderTest {

    @Test
    public void testBuilderToString() throws Exception{

        ConfigServiceConfiguration configuration = ConfigServiceConfiguration.Builder.init()
                .configSource(ConfigServiceConfiguration.SourceType.CLASSPATH)
                    .order(100)
                    .mandatory(true)
                    .finish()
                .configSource(ConfigServiceConfiguration.SourceType.REST)
                    .order(200)
                    .credentials(new Credentials("user", "pass"))
                    .mandatory(false)
                    .url("http://localhost/config")
                    .finish()
            .build();

        System.out.println(configuration);
    }

    @Test
    public void implicitOrder() throws Exception{

        ConfigServiceConfiguration configuration = ConfigServiceConfiguration.Builder.init()
                .configSource(ConfigServiceConfiguration.SourceType.CLASSPATH)
                    .finish()
                .configSource(ConfigServiceConfiguration.SourceType.REST)
                    .finish()
            .build();

        System.out.println(configuration);
    }

    @Test
    public void wrongOrderIsOrderedCorrectly() throws Exception{

        ConfigServiceConfiguration configuration = ConfigServiceConfiguration.Builder.init()
                .configSource(ConfigServiceConfiguration.SourceType.CLASSPATH)
                    .order(200)
                    .finish()
                .configSource(ConfigServiceConfiguration.SourceType.REST)
                    .order(100)
                    .finish()
            .build();

        System.out.println(configuration);
    }

    @Test
    public void negativeOrderIsNotAllowed() throws Exception{

        ConfigServiceConfiguration.Builder.init()
                .configSource(ConfigServiceConfiguration.SourceType.CLASSPATH)
                    .order(-1)
                    .finish()
            .build();
    }

    @Test
    public void explicitConfigSource() throws Exception{

        KeyValuesConfigSourceConfiguration kvc = new KeyValuesConfigSourceConfiguration(new HashMap<>());

        ConfigServiceConfiguration configuration = ConfigServiceConfiguration.Builder.init()
                .configSource(kvc)
                .configSource(ConfigServiceConfiguration.SourceType.REST)
                    .finish()
            .build();

        System.out.println(configuration);
    }

    @Test
    public void testFromMap() throws Exception {

        Map<String, String> properties = new HashMap<>();

        properties.put("configservice.source.1.type", "database");
        properties.put("configservice.source.1.order", "100");
        properties.put("configservice.source.1.mandatory", "true");
        properties.put("configservice.source.1.url", "http://localhost");
        properties.put("configservice.source.1.credentials", "<username>:<secret>");
        properties.put("configservice.source.1.driverClassName", "org.postgresql.Driver");

        properties.put("configservice.source.2.type", "system");
        properties.put("configservice.source.2.order", "300");

        properties.put("configservice.source.2.type", "env");
        properties.put("configservice.source.2.order", "40");

        //problematic stuff
//        properties.put("configservice.source.6.order", "10");
//        properties.put("configservice.source.a.order", "classpath");
//        properties.put("configservice.source.-1.order", "classpath");

        ConfigServiceConfiguration configuration = ConfigServiceConfiguration.Builder.from(properties);

        System.out.println(configuration);
    }
}
