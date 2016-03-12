package com.skronawi.configservice.core.strategies;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.SourceLoadingException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class DatabaseLoadingStrategy extends AbstractExternalSourceLoadingStrategy {

    private JdbcTemplate jdbcTemplate;
    private String driverClassName;

    @Override
    public void init(ConfigSourceConfiguration configSourceConfiguration) {
        super.init(configSourceConfiguration);
        driverClassName = configSourceConfiguration.getDriverClassName();
        jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(createDataSource());
    }

    //TODO with spring for TX like in keyValueService?
    private DataSource createDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(driverClassName);
        driverManagerDataSource.setPassword(credentials.getPassword());
        driverManagerDataSource.setUsername(credentials.getUsername());
        driverManagerDataSource.setUrl(url);
        return driverManagerDataSource;
    }

    @Override
    public Map<String, String> load(String name) throws SourceLoadingException {
        //TODO try-catch for SourceLoadingException
        try {
            Map<String, String> keyValues = new HashMap<>();
            jdbcTemplate.query(
                    "SELECT the_key, the_value FROM " + name,
                    (row, rowNum) -> new AbstractMap.SimpleEntry<>(row.getString("the_key"), row.getString("the_value")))
                    .stream().forEach(entry -> keyValues.put(entry.getKey(), entry.getValue()));
            return keyValues;
        } catch (Exception e) {
            throw new SourceLoadingException(e);
        }
    }
}
