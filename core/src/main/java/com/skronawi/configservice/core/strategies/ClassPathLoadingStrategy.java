package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.core.SourceLoadingException;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ClassPathLoadingStrategy extends AbstractFromUrlSourceLoadingStrategy {

    public static final String DEFAULT_PATH = "/config/";

    public ClassPathLoadingStrategy() {
        this.url = DEFAULT_PATH;
    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {

        Properties properties = new Properties();

        try (InputStream resourceAsStream = getClass().getResourceAsStream(url + name + ".properties")) {
            properties.load(resourceAsStream);
            return LoaderUtil.from(properties);

        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
