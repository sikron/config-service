package com.skronawi.configservice.service;

import com.skronawi.configservice.api.ConfigService;
import com.skronawi.configservice.api.ConfigServiceConfiguration;
import com.skronawi.configservice.core.ConfigServiceImpl;
import com.skronawi.keyvalueservice.api.AlreadyInitializedException;
import com.skronawi.keyvalueservice.api.KeyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Configuration
@PropertySources({
        @PropertySource("classpath:configservice.properties"),
        @PropertySource(value = "file:configservice.properties", ignoreResourceNotFound = true)
})
public class ConfigServiceProvisioning {

    /*
    hope is to get the configuration merged in this order
    1. classpath:configservice.properties (in the jar)
    2. file:configservice.properties (next to the jar)

    i don't know how env and system variables are used here...
     */
    @Autowired
    private Environment environment;

    //TODO use a InMemoryKeyValueService as default, unless a specific one is given

    @Bean
    public ConfigService configService(KeyValueService keyValueService) {
        Map<String, String> keyValues = getGatheredConfig();
        ConfigServiceConfiguration configServiceConfiguration =
                ConfigServiceConfiguration.Builder.from(keyValues);
        ConfigServiceImpl configService = new ConfigServiceImpl(keyValueService);
        configService.getLifeCycle().init(configServiceConfiguration);
        return configService;
    }

    private Map<String, String> getGatheredConfig() {
        Map<String, Object> map = new HashMap<>();
        for (org.springframework.core.env.PropertySource<?> ps :
                ((AbstractEnvironment) environment).getPropertySources()) {
            PropertySource propertySource = (PropertySource) ps;
            if (propertySource instanceof MapPropertySource) {
                map.putAll(((MapPropertySource) propertySource).getSource());
            }
        }
        Map<String, String> keyValues = new HashMap<>();
        for (AbstractMap.Entry<String, Object> entry : map.entrySet()) {
            keyValues.put(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return keyValues;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
