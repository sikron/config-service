package com.skronawi.configservice.core;

public class SourceLoadingException extends Exception {
    public SourceLoadingException(Exception e) {
        super(e);
    }

    public SourceLoadingException(String message) {
        super(message);
    }
}
