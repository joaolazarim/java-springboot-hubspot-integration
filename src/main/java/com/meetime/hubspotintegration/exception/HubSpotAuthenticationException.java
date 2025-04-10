package com.meetime.hubspotintegration.exception;

public class HubSpotAuthenticationException extends RuntimeException {

    public HubSpotAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}