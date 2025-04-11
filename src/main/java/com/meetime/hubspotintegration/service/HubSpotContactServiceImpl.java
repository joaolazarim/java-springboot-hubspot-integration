package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.dto.ContactDTO;
import com.meetime.hubspotintegration.dto.ContactResponseDTO;
import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import com.meetime.hubspotintegration.exception.HubSpotIntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HubSpotContactServiceImpl implements HubSpotContactService {

    private static final Logger logger = LoggerFactory.getLogger(HubSpotContactServiceImpl.class);

    private final RestTemplate restTemplate;
    private final HubSpotProperties hubSpotProperties;

    public HubSpotContactServiceImpl(RestTemplate restTemplate, HubSpotProperties hubSpotProperties) {
        this.restTemplate = restTemplate;
        this.hubSpotProperties = hubSpotProperties;
    }

    @Override
    public ContactResponseDTO createContact(ContactDTO contact, String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken);

            HttpEntity<ContactDTO> requestEntity = new HttpEntity<>(contact, headers);

            String url = hubSpotProperties.getApiBaseUri().concat("/crm/v3/objects/contacts");

            ResponseEntity<ContactResponseDTO> responseEntity = restTemplate.exchange(
                    url, HttpMethod.POST, requestEntity, ContactResponseDTO.class);

            return responseEntity.getBody();
        } catch (Exception e) {
            logger.error(ErrorMessageEnum.ERROR_CREATING_CONTACT.getMessage(), e.getMessage());
            throw new HubSpotIntegrationException(ErrorMessageEnum.ERROR_CREATING_CONTACT.getMessage(), e);
        }
    }
}