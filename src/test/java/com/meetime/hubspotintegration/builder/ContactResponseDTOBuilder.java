package com.meetime.hubspotintegration.builder;

import com.meetime.hubspotintegration.dto.ContactDTO;
import com.meetime.hubspotintegration.dto.ContactResponseDTO;

import java.util.HashMap;
import java.util.Map;

public class ContactResponseDTOBuilder {

    public static ContactResponseDTO build() {
        ContactResponseDTO contactResponseDTO = new ContactResponseDTO();
        contactResponseDTO.setId("123");
        contactResponseDTO.setCreatedAt("2025-04-10T15:10:37.165Z");
        contactResponseDTO.setArchived(false);
        contactResponseDTO.setArchivedAt("2025-04-10T15:10:37.165Z");
        contactResponseDTO.setUpdatedAt("2025-04-10T15:10:37.165Z");
        Map<String, Object> responseProperties = new HashMap<>();
        responseProperties.put("email", "jplazarim@gmail.com");
        responseProperties.put("lastname", "Lazarim");
        responseProperties.put("firstname", "Joao");
        contactResponseDTO.setProperties(responseProperties);

        return contactResponseDTO;
    }
}