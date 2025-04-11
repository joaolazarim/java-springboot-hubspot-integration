package com.meetime.hubspotintegration.builder;

import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.dto.TokenResponseDTO;

public class TokenResponseDTOBuilder {

    public static TokenResponseDTO build() {
        TokenResponseDTO tokenResponse = new TokenResponseDTO();
        tokenResponse.setAccessToken("mockAccessToken");
        tokenResponse.setRefreshToken("mockRefreshToken");
        tokenResponse.setExpiresIn(1800);

        return tokenResponse;
    }
}