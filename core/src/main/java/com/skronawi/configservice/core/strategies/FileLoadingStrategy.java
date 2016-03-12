package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.core.SourceLoadingException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class FileLoadingStrategy extends AbstractFromUrlSourceLoadingStrategy {

    public static final String DEFAULT_PATH = "config/";

    public FileLoadingStrategy() {
        this.url = DEFAULT_PATH;
    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {

        File file = new File(url + name + ".properties");
        Properties properties = new Properties();

        try (InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
            return LoaderUtil.from(properties);

        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
