package com.skronawi.configservice.core.strategies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is a class provided for use as an object factory for JNDI resources in an Apache Tomcat server. In the
 * webapp-specific context.xml there are resources specified from which this factory builds a properties object.
 * <p>
 * If this {@link TomcatJndiPropertiesFromResourceFactory} should be used to parse context.xml resources to
 * properties you must specify this factory in the context.xml as follows:
 * <p>
 * <pre>
 * {@code
 * <Resource name="myResourceName"
 *      auth="Container"
 *      type="java.util.Properties"
 *      factory="com.skronawi.configservice.core.strategies"
 *      description="key-value-pairs"
 *      singleton="true"
 *
 *      myKey=myValue
 *      myKey2=myValue2
 *      ...
 * \>
 * }
 * </pre>
 */
public class TomcatJndiPropertiesFromResourceFactory extends JndiPropertiesFromResourceFactory {

    private final List<String> listKeysToSuppress = new ArrayList<>();

    public TomcatJndiPropertiesFromResourceFactory() {
        this.listKeysToSuppress.add("auth");
        this.listKeysToSuppress.add("closeMethod");
        this.listKeysToSuppress.add("description");
        this.listKeysToSuppress.add("name");
        this.listKeysToSuppress.add("scope");
        this.listKeysToSuppress.add("singleton");
        this.listKeysToSuppress.add("type");
        this.listKeysToSuppress.add("factory");
    }

    @Override
    protected Collection<String> getKeysToSuppress() {
        return this.listKeysToSuppress;
    }
}