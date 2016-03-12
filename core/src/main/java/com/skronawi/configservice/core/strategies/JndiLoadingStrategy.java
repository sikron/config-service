package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.core.SourceLoadingException;

import javax.naming.InitialContext;
import java.util.Map;
import java.util.Properties;

public class JndiLoadingStrategy extends AbstractFromUrlSourceLoadingStrategy {

    public static final String DEFAULT_PATH = "java:comp/env/config/";

    public JndiLoadingStrategy() {
        this.url = DEFAULT_PATH;
    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {

        try {
            InitialContext initContext = new InitialContext();
            Properties properties = (Properties) initContext.lookup(url);
            return LoaderUtil.from(properties);

        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
