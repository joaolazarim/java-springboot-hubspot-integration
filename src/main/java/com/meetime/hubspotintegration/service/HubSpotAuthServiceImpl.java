package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.dto.TokenResponseDTO;
import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import com.meetime.hubspotintegration.exception.HubSpotAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class HubSpotAuthServiceImpl implements HubSpotAuthService {

    private static final Logger logger = LoggerFactory.getLogger(HubSpotAuthServiceImpl.class);

    private final HubSpotProperties hubSpotProperties;
    private final RestTemplate restTemplate;

    public HubSpotAuthServiceImpl(HubSpotProperties hubSpotProperties, RestTemplate restTemplate) {
        this.hubSpotProperties = hubSpotProperties;
        this.restTemplate = restTemplate;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        try {
            return UriComponentsBuilder.fromHttpUrl(hubSpotProperties.getAuthorizationUri())
                    .queryParam("client_id", hubSpotProperties.getClientId())
                    .queryParam("scope", hubSpotProperties.getScopes())
                    .queryParam("redirect_uri", hubSpotProperties.getRedirectUri())
                    .queryParam("state", state)
                    .build()
                    .toUriString();
        } catch (Exception e) {
            logger.error(ErrorMessageEnum.ERROR_GENERATING_AUTH_URL.getMessage(), e);
            throw new HubSpotAuthenticationException(ErrorMessageEnum.ERROR_GENERATING_AUTH_URL.getMessage(), e);
        }
    }

    @Override
    public TokenResponseDTO exchangeCodeForToken(String code) {
        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", "authorization_code");
            form.add("client_id", hubSpotProperties.getClientId());
            form.add("client_secret", hubSpotProperties.getClientSecret());
            form.add("redirect_uri", hubSpotProperties.getRedirectUri());
            form.add("code", code);

            return restTemplate.postForObject(
                    hubSpotProperties.getTokenUri(),
                    form,
                    TokenResponseDTO.class
            );
        } catch (Exception e) {
            logger.error(ErrorMessageEnum.ERROR_EXCHANGING_CODE_FOR_TOKEN.getMessage(), e);
            throw new HubSpotAuthenticationException(ErrorMessageEnum.ERROR_EXCHANGING_CODE_FOR_TOKEN.getMessage(), e);
        }
    }
}