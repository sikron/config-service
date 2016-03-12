package com.skronawi.configservice.api;

import com.skronawi.keyvalueservice.api.Credentials;

public class ConfigSourceConfiguration {

    protected ConfigServiceConfiguration.SourceType sourceType;
    protected int order;
    protected boolean isMandatory = true; //default
    protected String url;
    protected Credentials credentials;
    protected String driverClassName;

    public ConfigServiceConfiguration.SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ConfigServiceConfiguration.SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void assertValid() {
        assert sourceType != null;
        assert order > 0;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        return "ConfigSourceConfiguration{" +
                "sourceType=" + sourceType +
                ", order=" + order +
                ", isMandatory=" + isMandatory +
                ", url='" + url + '\'' +
                ", credentials=" + credentials +
                ", driverClassName='" + driverClassName + '\'' +
                '}';
    }
}
