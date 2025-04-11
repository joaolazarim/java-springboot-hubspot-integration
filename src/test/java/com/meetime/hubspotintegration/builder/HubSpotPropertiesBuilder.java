package com.meetime.hubspotintegration.builder;

import com.meetime.hubspotintegration.config.HubSpotProperties;

public class HubSpotPropertiesBuilder {

    public static HubSpotProperties build() {
        HubSpotProperties properties = new HubSpotProperties();
        properties.setAuthorizationUri("https://app.hubspot.com/oauth/authorize");
        properties.setTokenUri("https://api.hubapi.com/oauth/v1/token");
        properties.setClientId("mockClientId");
        properties.setClientSecret("mockClientSecret");
        properties.setRedirectUri("http://localhost:8080/auth/callback");
        properties.setScopes("crm.objects.contacts.write%20oauth%20crm.objects.contacts.read");
        properties.setApiBaseUri("https://api.hubapi.com");
        return properties;
    }
}