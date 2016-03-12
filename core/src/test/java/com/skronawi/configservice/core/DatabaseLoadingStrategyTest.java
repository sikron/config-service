package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.strategies.DatabaseLoadingStrategy;
import com.skronawi.keyvalueservice.api.Credentials;
import org.apache.commons.io.IOUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class DatabaseLoadingStrategyTest {

    public static final String DRIVER_CLASS_NAME = "org.hsqldb.jdbcDriver";
    public static final String URL = "jdbc:hsqldb:mem:test";
    public static final String USER = "sa";
    public static final String PASSWORD = "";
    public static final String TABLE = "key_values";

    private Map<String, String> keyValues;

    @BeforeClass
    public void setup() throws Exception {

        createDatabase();

        //same as in insertData()
        keyValues = LoaderTestUtil.loadTestKeyValues();
    }

    @Test
    public void testLoad() throws Exception {

        ConfigSourceConfiguration configSourceConfiguration = new ConfigSourceConfiguration();
        configSourceConfiguration.setUrl(URL);
        configSourceConfiguration.setDriverClassName(DRIVER_CLASS_NAME);
        configSourceConfiguration.setCredentials(new Credentials(USER, PASSWORD));

        DatabaseLoadingStrategy databaseLoadingStrategy = new DatabaseLoadingStrategy();
        databaseLoadingStrategy.init(configSourceConfiguration);
        Map<String, String> keyValues = databaseLoadingStrategy.load(TABLE);

        Assert.assertEquals(keyValues, this.keyValues);
    }

    private void createDatabase() throws SQLException, IOException {
        executeStatementInFile("/create-db.sql");
        executeStatementInFile("/populate-key_values.sql");
    }

    private void executeStatementInFile(String filePath) throws SQLException, IOException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        Statement stmt = connection.createStatement();
        stmt.execute(IOUtils.toString(getClass().getResourceAsStream(filePath)));
        if (!connection.getAutoCommit()) {
            connection.commit();
        }
        connection.close();
    }
}
