package com.meetime.hubspotintegration.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessageEnum {

    TOO_MANY_REQUESTS("Too many requests, please try again later"),
    MISSING_TOKEN("Missing Bearer token in the request header"),
    ERROR_GENERATING_AUTH_URL("Could not get authorization URL"),
    ERROR_EXCHANGING_CODE_FOR_TOKEN("Could not exchange code for token"),
    INVALID_STATE("Invalid state parameter"),
    ERROR_SIGNING_STATE("Error signing state parameter"),
    ERROR_CREATING_CONTACT("Error creating contact");

    private final String message;
}
