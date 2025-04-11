package com.meetime.hubspotintegration.builder;

import com.meetime.hubspotintegration.dto.ContactDTO;

import java.util.HashMap;
import java.util.Map;

public class ContactDTOBuilder {

    public static ContactDTO build() {
        ContactDTO contactDTO = new ContactDTO();
        Map<String, Object> properties = new HashMap<>();
        properties.put("email", "jplazarim1237@gmail.com");
        properties.put("lastname", "Lazarim");
        properties.put("firstname", "Joao 521");
        contactDTO.setProperties(properties);

        return contactDTO;
    }
}