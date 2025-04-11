package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.dto.TokenResponseDTO;

public interface HubSpotAuthService {

    String getAuthorizationUrl(String state);
    TokenResponseDTO exchangeCodeForToken(String code);
}
