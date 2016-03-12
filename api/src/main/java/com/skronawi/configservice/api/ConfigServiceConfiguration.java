package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.Credentials;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ConfigServiceConfiguration {

    public enum SourceType {
        CLASSPATH("classpath"),
        FILE("file"),
        JNDI("jndi"),
        REST("rest"),
        SYSTEM("system"),
        ENV("env"),
        DB("database"),
        KV("kv"); //provided key values, only for programmatical use

        private final String type;

        SourceType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public static SourceType from(String type) {
            for (SourceType sourceType : SourceType.values()) {
                if (sourceType.getType().equalsIgnoreCase(type)) {
                    return sourceType;
                }
            }
            throw new IllegalArgumentException("unknown type: " + type);
        }
    }

    public static class Builder {
    /*

    configservice.source.1.type=classpath | file | jndi | database | rest | env | system
		- mandatory
	configservice.source.1.order=1..N
		- optional, ansonsten reihenfolge wie in file
		- höhere überschreiben niedrigere
	configservice.source.1.mandatory=true | false
		- optional, default is true
	configservice.source.1.url=http://... | jdbc://... | <path>
		- optional, resource impls sollen defaults haben!
	configservice.source.1.credentials=<username>:<secret>
		- optional
		- was mit jdbc, http ?

	db -> driverClassName
	rest -> auth type
     */

        public static final String CONFIGSERVER = "configservice";
        public static final String SOURCE = "source";

        public static final String TYPE = "type";
        public static final String ORDER = "order";
        public static final String MANDATORY = "mandatory";
        public static final String URL = "url";
        public static final String CREDENTIALS = "credentials";
        public static final String DRIVER_CLASS_NAME = "driverClassName";

        private static final String CONFIGSERVER_PREFIX = CONFIGSERVER + ".";
        private static final String CONFIGSERVER_SOURCE_PREFIX = CONFIGSERVER + "." + SOURCE + ".";

        private final ConfigServiceConfiguration configServiceConfiguration;

        private Builder() {
            configServiceConfiguration = new ConfigServiceConfiguration();
        }

        public static ConfigServiceConfiguration from(Map<String, String> configServiceKeyValueConfiguration) {

            Map<String, ConfigSourceConfiguration> configSources = new HashMap<>();
            ConfigServiceConfiguration configServiceConfiguration = new ConfigServiceConfiguration();

            configServiceKeyValueConfiguration.entrySet().stream().forEach(entry -> {

                        String key = entry.getKey();
                        String value = entry.getValue();

                        if (!key.startsWith(CONFIGSERVER_PREFIX)) {
                            return;
                        }

                        if (entry.getKey().startsWith(CONFIGSERVER_SOURCE_PREFIX)) {
                            handleConfigSourceParam(key, value, configSources);
                        }
                    }
            );

            List<ConfigSourceConfiguration> configSourceConfigurationList = configSources.values()
                    .stream().collect(Collectors.toList());

            //check source config validity
            configSourceConfigurationList.stream().forEach(ConfigSourceConfiguration::assertValid);

            //order sources by order ascending
            Collections.sort(configSourceConfigurationList, (theOne, theOther) -> new Integer(theOne.getOrder())
                    .compareTo(theOther.getOrder()));

            configServiceConfiguration.setConfigSourceConfigurations(configSourceConfigurationList);

            return configServiceConfiguration;
        }

        private static void handleConfigSourceParam(String key, String value, Map<String,
                ConfigSourceConfiguration> configSources) {

            String sourceId = getSourceId(key);

            ConfigSourceConfiguration existingConfigSourceConfigurationById = configSources.get(sourceId);

            if (existingConfigSourceConfigurationById == null) {
                existingConfigSourceConfigurationById = new ConfigSourceConfiguration();
                configSources.put(sourceId, existingConfigSourceConfigurationById);
            }

            if (key.equals(CONFIGSERVER_SOURCE_PREFIX + sourceId + "." + TYPE)) {
                existingConfigSourceConfigurationById.setSourceType(SourceType.from(value));
            }
            if (key.equals(CONFIGSERVER_SOURCE_PREFIX + sourceId + "." + ORDER)) {
                existingConfigSourceConfigurationById.setOrder(Integer.parseInt(value));
            }
            if (key.equals(CONFIGSERVER_SOURCE_PREFIX + sourceId + "." + MANDATORY)) {
                existingConfigSourceConfigurationById.setMandatory(Boolean.parseBoolean(value));
            }
            if (key.equals(CONFIGSERVER_SOURCE_PREFIX + sourceId + "." + URL)) {
                existingConfigSourceConfigurationById.setUrl(value);
            }
            if (key.equals(CONFIGSERVER_SOURCE_PREFIX + sourceId + "." + CREDENTIALS)) {
                String[] usernamePassword = value.split(":");
                assert usernamePassword.length == 2;
                existingConfigSourceConfigurationById.setCredentials(new Credentials(usernamePassword[0], usernamePassword[1]));
            }
            if (key.equals(CONFIGSERVER_SOURCE_PREFIX + sourceId + "." + DRIVER_CLASS_NAME)) {
                existingConfigSourceConfigurationById.setDriverClassName(value);
            }
        }

        private static String getSourceId(String key) {
            String pattern = "(.*)\\.(.*)\\.(\\d+)(.*)";
            Pattern compile = Pattern.compile(pattern);
            Matcher matcher = compile.matcher(key);
            if (!matcher.find()) {
                throw new IllegalArgumentException("problem matching the key for finding the id: " + key);
            }
            return matcher.group(3);
        }

        public static Builder init() {
            return new Builder();
        }

        public BuilderWithConfigSource configSource(SourceType type) {
            BuilderWithConfigSource builderWithConfigSource = new BuilderWithConfigSource(this);
            builderWithConfigSource.sourceType(type);
            builderWithConfigSource.order(configServiceConfiguration.configSourceConfigurations.stream()
                    .mapToInt(ConfigSourceConfiguration::getOrder).max().orElse(0) //it may be the first
                    + 1);
            return builderWithConfigSource;
        }

        public Builder configSource(ConfigSourceConfiguration configSourceConfiguration) {
            BuilderWithConfigSource builderWithConfigSource = new BuilderWithConfigSource(this);
            configSourceConfiguration.setOrder(configServiceConfiguration.configSourceConfigurations.stream()
                    .mapToInt(ConfigSourceConfiguration::getOrder).max().orElse(0) //it may be the first
                    + 1);
            builderWithConfigSource.configSourceConfiguration = configSourceConfiguration;
            builderWithConfigSource.finish();
            return builderWithConfigSource.builder;
        }

        public ConfigServiceConfiguration build() {
            Collections.sort(configServiceConfiguration.configSourceConfigurations,
                    (theOne, theOther) -> new Integer(theOne.getOrder()).compareTo(theOther.getOrder()));
            configServiceConfiguration.configSourceConfigurations.stream().forEach(ConfigSourceConfiguration::assertValid);
            return configServiceConfiguration;
        }
    }

    public static class BuilderWithConfigSource {

        private ConfigSourceConfiguration configSourceConfiguration;
        private final Builder builder;

        private BuilderWithConfigSource(Builder builder) {
            this.configSourceConfiguration = new ConfigSourceConfiguration();
            this.builder = builder;
        }

        public BuilderWithConfigSource sourceType(ConfigServiceConfiguration.SourceType sourceType) {
            configSourceConfiguration.setSourceType(sourceType);
            return this;
        }

        public BuilderWithConfigSource order(int order) {
            configSourceConfiguration.setOrder(order);
            return this;
        }

        public BuilderWithConfigSource mandatory(boolean mandatory) {
            configSourceConfiguration.setMandatory(mandatory);
            return this;
        }

        public BuilderWithConfigSource url(String url) {
            configSourceConfiguration.setUrl(url);
            return this;
        }

        public BuilderWithConfigSource credentials(Credentials credentials) {
            configSourceConfiguration.setCredentials(credentials);
            return this;
        }

        public Builder finish() {
            builder.configServiceConfiguration.configSourceConfigurations.add(configSourceConfiguration);
            return builder;
        }
    }

    private List<ConfigSourceConfiguration> configSourceConfigurations;

    public ConfigServiceConfiguration() {
        configSourceConfigurations = new ArrayList<>();
    }

    public List<ConfigSourceConfiguration> getConfigSourceConfigurations() {
        return configSourceConfigurations;
    }

    public void setConfigSourceConfigurations(List<ConfigSourceConfiguration> configSourceConfigurations) {
        this.configSourceConfigurations = configSourceConfigurations;
    }

    @Override
    public String toString() {
        return "ConfigServiceConfiguration{" +
                "configSourceConfigurations=" + configSourceConfigurations +
                '}';
    }
}
