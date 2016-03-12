package com.skronawi.configservice.core;

import com.skronawi.configservice.api.ConfigSourceConfiguration;
import com.skronawi.configservice.api.MandatoryConfigurationException;
import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.Test;

public class SourceLoaderTest {

    @Test(expectedExceptions = MandatoryConfigurationException.class)
    public void testMandatory(@Mocked SourceLoadingStrategy strategy, @Mocked ConfigSourceConfiguration configuration)
            throws Exception {

        SourceLoaderImpl sourceLoader = new SourceLoaderImpl(strategy, configuration);
        sourceLoader.init();

        expectStrategyToThrow(strategy);
        expectConfigToBeMandatory(configuration, true);

        sourceLoader.load(() -> "blub");
    }

    @Test
    public void testNonMandatory(@Mocked SourceLoadingStrategy strategy, @Mocked ConfigSourceConfiguration configuration)
            throws Exception {

        SourceLoaderImpl sourceLoader = new SourceLoaderImpl(strategy, configuration);
        sourceLoader.init();

        expectStrategyToThrow(strategy);
        expectConfigToBeMandatory(configuration, false);

        sourceLoader.load(() -> "blub");
    }

    private void expectConfigToBeMandatory(ConfigSourceConfiguration configuration, boolean isMandatory) {
        new Expectations() {
            {
                configuration.isMandatory();
                result = isMandatory;
            }
        };
    }

    private void expectStrategyToThrow(SourceLoadingStrategy strategy) throws Exception {
        new Expectations() {
            {
                strategy.load(anyString);
                result = new IllegalArgumentException();
            }
        };
    }
}
