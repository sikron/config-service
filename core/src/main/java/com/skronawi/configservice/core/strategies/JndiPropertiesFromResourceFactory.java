package com.skronawi.configservice.core.strategies;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public abstract class JndiPropertiesFromResourceFactory implements ObjectFactory {

    protected abstract Collection<String> getKeysToSuppress();

    @Override
    public Object getObjectInstance(Object object, Name name, Context nameContext, Hashtable<?, ?> environment) throws Exception {
        Properties properties = new Properties();

        if (!(object instanceof Reference)) {
            throw new IllegalStateException("The specified object is not a Reference: " + object);
        }

        Collection<String> colKeysToSuppress = getKeysToSuppress();

        Reference reference = (Reference) object;
        Enumeration<RefAddr> addrs = reference.getAll();

        while (addrs.hasMoreElements()) {
            RefAddr addr = addrs.nextElement();
            String key = addr.getType();
            String value = (String) addr.getContent();

            if (!colKeysToSuppress.contains(key)) {
                properties.put(key, value);
            }
        }

        return properties;
    }
}