package com.meetime.hubspotintegration.exception;

public class StateSignatureException extends RuntimeException {

    public StateSignatureException(String message) {
        super(message);
    }
    public StateSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}