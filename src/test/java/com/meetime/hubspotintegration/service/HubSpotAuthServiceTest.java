package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.builder.HubSpotPropertiesBuilder;
import com.meetime.hubspotintegration.builder.TokenResponseDTOBuilder;
import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.dto.TokenResponseDTO;
import com.meetime.hubspotintegration.exception.HubSpotAuthenticationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HubSpotAuthServiceTest {

    private RestTemplate restTemplate;
    private HubSpotAuthService hubSpotAuthService;

    @BeforeEach
    public void setup() {
        HubSpotProperties hubSpotProperties = HubSpotPropertiesBuilder.build();
        restTemplate = mock(RestTemplate.class);
        hubSpotAuthService = new HubSpotAuthService(hubSpotProperties, restTemplate);
    }

    @Test
    public void testGetAuthorizationUrl_Success() {
        String state = "testState";
        String authUrl = hubSpotAuthService.getAuthorizationUrl(state);

        assertTrue(authUrl.contains("client_id=mockClientId"));
        assertTrue(authUrl.contains("scope=crm.objects.contacts.write%20oauth%20crm.objects.contacts.read"));
        assertTrue(authUrl.contains("redirect_uri=http://localhost:8080/auth/callback"));
        assertTrue(authUrl.contains("state=" + state));
    }

    @Test
    public void testGetAuthorizationUrl_Failure() {
        HubSpotProperties invalidProperties = HubSpotPropertiesBuilder.build();
        invalidProperties.setAuthorizationUri(null);
        hubSpotAuthService = new HubSpotAuthService(invalidProperties, restTemplate);

        assertThrows(HubSpotAuthenticationException.class, () -> {
            hubSpotAuthService.getAuthorizationUrl("mockState");
        });
    }

    @Test
    public void testExchangeCodeForToken_Success() {
        TokenResponseDTO expectedToken = TokenResponseDTOBuilder.build();

        when(restTemplate.postForObject(eq("https://api.hubapi.com/oauth/v1/token"), any(LinkedMultiValueMap.class), eq(TokenResponseDTO.class)))
                .thenReturn(expectedToken);

        TokenResponseDTO result = hubSpotAuthService.exchangeCodeForToken("mockCode");

        assertNotNull(result);
        assertEquals("mockAccessToken", result.getAccessToken());
        assertEquals("mockRefreshToken", result.getRefreshToken());
        assertEquals(1800, result.getExpiresIn());
    }

    @Test
    public void testExchangeCodeForToken_Failure() {
        when(restTemplate.postForObject(eq("https://api.hubapi.com/oauth/v1/token"), any(LinkedMultiValueMap.class), eq(TokenResponseDTO.class)))
                .thenThrow(new RuntimeException("Mocked exception"));

        assertThrows(HubSpotAuthenticationException.class, () -> hubSpotAuthService.exchangeCodeForToken("mockCode"));
    }
}