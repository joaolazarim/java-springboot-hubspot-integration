package com.meetime.hubspotintegration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hubspot")
public class HubSpotProperties {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String scopes;
    private String tokenUri;
    private String authorizationUri;
    private String apiBaseUri;
}