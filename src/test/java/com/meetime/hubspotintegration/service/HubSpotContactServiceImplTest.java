package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.builder.ContactDTOBuilder;
import com.meetime.hubspotintegration.builder.ContactResponseDTOBuilder;
import com.meetime.hubspotintegration.builder.HubSpotPropertiesBuilder;
import com.meetime.hubspotintegration.config.HubSpotProperties;
import com.meetime.hubspotintegration.dto.ContactDTO;
import com.meetime.hubspotintegration.dto.ContactResponseDTO;
import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import com.meetime.hubspotintegration.exception.HubSpotIntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class HubSpotContactServiceImplTest {

    private HubSpotContactServiceImpl hubSpotContactService;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        HubSpotProperties hubSpotProperties = HubSpotPropertiesBuilder.build();
        restTemplate = mock(RestTemplate.class);
        hubSpotContactService = new HubSpotContactServiceImpl(restTemplate, hubSpotProperties);
    }

    @Test
    public void testCreateContact_Success() {
        ContactDTO contactDTO = ContactDTOBuilder.build();

        ContactResponseDTO expectedResponse = ContactResponseDTOBuilder.build();

        ResponseEntity<ContactResponseDTO> mockResponseEntity =
                new ResponseEntity<>(expectedResponse, HttpStatus.CREATED);
        when(restTemplate.exchange(
                eq("https://api.hubapi.com/crm/v3/objects/contacts"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ContactResponseDTO.class)
        )).thenReturn(mockResponseEntity);

        String accessToken = "mockAccessToken";
        ContactResponseDTO result = hubSpotContactService.createContact(contactDTO, accessToken);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("jplazarim@gmail.com", result.getProperties().get("email"));
        assertEquals("Joao", result.getProperties().get("firstname"));
        assertEquals("Lazarim", result.getProperties().get("lastname"));
        verify(restTemplate, times(1)).exchange(
                eq("https://api.hubapi.com/crm/v3/objects/contacts"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ContactResponseDTO.class)
        );
    }

    @Test
    public void testCreateContact_Failure() {
        ContactDTO contactDTO = ContactDTOBuilder.build();
        when(restTemplate.exchange(
                eq("https://api.hubapi.com/crm/v3/objects/contacts"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(ContactResponseDTO.class)
        )).thenThrow(new RuntimeException("Mocked exception"));

        String accessToken = "mockAccessToken";

        Exception exception = assertThrows(HubSpotIntegrationException.class, () -> {
            hubSpotContactService.createContact(contactDTO, accessToken);
        });
        assertEquals(exception.getMessage(), ErrorMessageEnum.ERROR_CREATING_CONTACT.getMessage());
    }
}