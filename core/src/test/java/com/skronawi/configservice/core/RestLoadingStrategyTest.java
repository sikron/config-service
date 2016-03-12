package com.skronawi.configservice.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.core.strategies.RestLoadingStrategy;
import com.skronawi.keyvalueservice.api.Credentials;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

/*
http://wiremock.org/getting-started.html
 */
public class RestLoadingStrategyTest {

    public static final String REST_URL = "http://localhost:8080/config/";
    public static final String USER = "user";
    public static final String PASSWORD = "pass";

    public static final String NAME = "test";
    public static final String WIRE_MOCK_URL = "/config";

    public static final Credentials CREDENTIALS = new Credentials(USER, PASSWORD);

    private Map<String, String> keyValues;
    private WireMockServer wireMockServer;

    @BeforeClass
    public void setup() throws Exception {
        keyValues = LoaderTestUtil.loadTestKeyValues();
        wireMockServer = new WireMockServer(); //No-args constructor will start on port 8080
        wireMockServer.start();
    }

    @AfterClass(alwaysRun = true)
    public void teardown() throws Exception {
        wireMockServer.stop();
    }

    @AfterMethod(alwaysRun = true)
    public void reset() throws Exception {
        WireMock.reset();
    }

    @Test
    public void testLoad() throws Exception {

        expectSuccessfulKeyValueResponse();

        ConfigSourceConfiguration configSourceConfiguration = new ConfigSourceConfiguration();
        configSourceConfiguration.setUrl(REST_URL);

        RestLoadingStrategy restLoadingStrategy = new RestLoadingStrategy();
        restLoadingStrategy.init(configSourceConfiguration);

        Map<String, String> keyValues = restLoadingStrategy.load(NAME);
        Assert.assertEquals(keyValues, this.keyValues);
    }

    @Test
    public void testLoadAuth() throws Exception {

        expectSuccessfulKeyValueResponseWithAuth();

        ConfigSourceConfiguration configSourceConfiguration = new ConfigSourceConfiguration();
        configSourceConfiguration.setUrl(REST_URL);
        configSourceConfiguration.setCredentials(CREDENTIALS);

        RestLoadingStrategy restLoadingStrategy = new RestLoadingStrategy();
        restLoadingStrategy.init(configSourceConfiguration);

        Map<String, String> keyValues = restLoadingStrategy.load(NAME);
        Assert.assertEquals(keyValues, this.keyValues);
    }

    private void expectSuccessfulKeyValueResponse() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(WIRE_MOCK_URL + "/" + NAME))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK_200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(toJson(keyValues))));
    }

    private void expectSuccessfulKeyValueResponseWithAuth() throws JsonProcessingException {
        wireMockServer.stubFor(WireMock.get(WireMock.urlEqualTo(WIRE_MOCK_URL + "/" + NAME))
                .withHeader(HttpHeaders.AUTHORIZATION, WireMock.equalTo(authorizeBasic(CREDENTIALS)))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK_200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(toJson(keyValues))));
    }

    private String toJson(Map<String, String> keyValues) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(keyValues);
    }

    private String authorizeBasic(Credentials credentials) {
        return "Basic " + Base64Utils.encodeToString(
                (credentials.getUsername() + ":" + credentials.getPassword()).getBytes());
    }
}
