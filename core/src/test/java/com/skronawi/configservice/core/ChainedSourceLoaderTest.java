package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.api.Configuration;
import com.skronawi.configservice.api.KeyValuesConfigSourceConfiguration;
import com.skronawi.configservice.api.MandatoryConfigurationException;
import com.skronawi.configservice.core.strategies.KeyValuesLoadingStrategy;
import com.skronawi.configservice.core.strategies.LoaderUtil;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChainedSourceLoaderTest {

    public static final String NAME = "test";

    private static final Configuration CONFIG = () -> NAME;

    @Test
    public void testEmptyLoaders() throws Exception {
        Map<String, String> keyValues = new ChainedSourceLoader(new ArrayList<>()).load(CONFIG);
        Assert.assertEquals(keyValues, new HashMap<String, String>());
    }

    @Test
    public void testOneLoader() throws Exception {

        SourceLoaderBuilder sourceLoaderBuilder = SourceLoaderBuilder.init()
                .put(NAME + "." + "a", "1")
                .put(NAME + "." + "b", "2");
        SourceLoader sourceLoader = sourceLoaderBuilder.buildAndInit();

        Map<String, String> loadedKeyValues = chainLoad(sourceLoader);

        Assert.assertEquals(loadedKeyValues, removePrefix(sourceLoaderBuilder));
    }

    @Test
    public void testTwoDisjunctLoaders() throws Exception {

        SourceLoaderBuilder sourceLoaderBuilder1 = SourceLoaderBuilder.init()
                .put(NAME + "." + "a", "1")
                .put(NAME + "." + "b", "2");
        SourceLoader sourceLoader1 = sourceLoaderBuilder1.buildAndInit();

        SourceLoaderBuilder sourceLoaderBuilder2 = SourceLoaderBuilder.init()
                .put(NAME + "." + "c", "3")
                .put(NAME + "." + "d", "4");
        SourceLoader sourceLoader2 = sourceLoaderBuilder2.buildAndInit();

        Map<String, String> loadedKeyValues = chainLoad(sourceLoader1, sourceLoader2);

        Assert.assertEquals(loadedKeyValues, mergeMapsAndRemovePrefix(sourceLoaderBuilder1.keyValues,
                sourceLoaderBuilder2.keyValues));
    }

    @Test
    public void testTwoOverlappingLoaders() throws Exception {

        SourceLoaderBuilder sourceLoaderBuilder1 = SourceLoaderBuilder.init()
                .put(NAME + "." + "a", "1")
                .put(NAME + "." + "b", "2");
        SourceLoader sourceLoader1 = sourceLoaderBuilder1.buildAndInit();

        SourceLoaderBuilder sourceLoaderBuilder2 = SourceLoaderBuilder.init()
                .put(NAME + "." + "b", "3") // <-- 'b' again, so (b,2) is replaced by (b,3)
                .put(NAME + "." + "d", "4");
        SourceLoader sourceLoader2 = sourceLoaderBuilder2.buildAndInit();

        Map<String, String> loadedKeyValues = chainLoad(sourceLoader1, sourceLoader2);

        Assert.assertEquals(loadedKeyValues, mergeMapsAndRemovePrefix(sourceLoaderBuilder1.keyValues,
                sourceLoaderBuilder2.keyValues));
    }

    @Test
    public void testOneOptionalLoaderThrowsException(
            @Mocked SourceLoadingStrategy sourceLoadingStrategy,
            @Mocked ConfigSourceConfiguration configSourceConfiguration) throws Exception {

        new Expectations() {
            {
                sourceLoadingStrategy.load(anyString);
                result = new SourceLoadingException("blub");
            }
        };
        expectConfigToBeMandatory(configSourceConfiguration, false);

        SourceLoaderImpl sourceLoader = new SourceLoaderImpl(sourceLoadingStrategy, configSourceConfiguration);
        sourceLoader.init();

        Map<String, String> keyValues = new ChainedSourceLoader(Arrays.asList(sourceLoader)).load(CONFIG);
        Assert.assertEquals(keyValues, new HashMap<String, String>());
    }

    @Test(expectedExceptions = MandatoryConfigurationException.class)
    public void testOneMandatoryLoaderThrowsException(
            @Mocked SourceLoadingStrategy sourceLoadingStrategy,
            @Mocked ConfigSourceConfiguration configSourceConfiguration) throws Exception {

        new Expectations() {
            {
                sourceLoadingStrategy.load(anyString);
                result = new SourceLoadingException("blub");
            }
        };
        expectConfigToBeMandatory(configSourceConfiguration, true);

        SourceLoaderImpl sourceLoader = new SourceLoaderImpl(sourceLoadingStrategy, configSourceConfiguration);
        sourceLoader.init();

        new ChainedSourceLoader(Arrays.asList(sourceLoader)).load(CONFIG);
    }

    @Test
    public void testOptionalThrowingLoaderDoesNotContribute() throws Exception {

        SourceLoaderBuilder sourceLoaderBuilder1 = SourceLoaderBuilder.init()
                .put(NAME + "." + "a", "1")
                .put(NAME + "." + "b", "2");
        SourceLoader sourceLoader1 = sourceLoaderBuilder1.buildAndInit();

        SourceLoaderBuilder sourceLoaderBuilder2 = SourceLoaderBuilder.init()
                .put(NAME + "." + "b", "3")
                .put(NAME + "." + "d", "4")
                .doThrowOptionally(true); //these key-values are NOT gathered into the combined result
        SourceLoader sourceLoader2 = sourceLoaderBuilder2.buildAndInit();

        Map<String, String> loadedKeyValues = chainLoad(sourceLoader1, sourceLoader2);

        Assert.assertEquals(loadedKeyValues, removePrefix(sourceLoaderBuilder1));
    }

    private Map<String, String> removePrefix(SourceLoaderBuilder sourceLoaderBuilder1) {
        return LoaderUtil.removeKeyPrefix(NAME, sourceLoaderBuilder1.keyValues);
    }

    private Map<String, String> mergeMapsAndRemovePrefix(HashMap<String, String>... keyValuesMaps) {
        HashMap<String, String> keyValues = new HashMap<>();
        for (HashMap<String, String> map : keyValuesMaps) {
            keyValues.putAll(map);
        }
        return LoaderUtil.removeKeyPrefix(NAME, keyValues);
    }

    private Map<String, String> chainLoad(SourceLoader... sourceLoaders) throws MandatoryConfigurationException {
        return new ChainedSourceLoader(Arrays.asList(sourceLoaders)).load(CONFIG);
    }

    private void expectConfigToBeMandatory(ConfigSourceConfiguration configuration, boolean isMandatory) {
        new Expectations() {
            {
                configuration.isMandatory();
                result = isMandatory;
            }
        };
    }

    private static class SourceLoaderBuilder {

        private HashMap<String, String> keyValues;
        private boolean doThrow;

        private SourceLoaderBuilder() {
            keyValues = new HashMap<>();
        }

        public static SourceLoaderBuilder init() {
            return new SourceLoaderBuilder();
        }

        public SourceLoaderBuilder put(String key, String value) {
            keyValues.put(key, value);
            return this;
        }

        public SourceLoaderBuilder doThrowOptionally(boolean doThrow) {
            this.doThrow = doThrow;
            return this;
        }

        public SourceLoader buildAndInit() {
            KeyValuesConfigSourceConfiguration keyValuesConfigSourceConfiguration =
                    new KeyValuesConfigSourceConfiguration(keyValues);

            SourceLoaderImpl sourceLoader = null;
            if (doThrow) {
                sourceLoader = new SourceLoaderImpl(new ThrowingStrategy(), new OptionalConfiguration());
            } else {
                sourceLoader = new SourceLoaderImpl(new KeyValuesLoadingStrategy(),
                        keyValuesConfigSourceConfiguration);
            }
            sourceLoader.init();
            return sourceLoader;
        }
    }
}
