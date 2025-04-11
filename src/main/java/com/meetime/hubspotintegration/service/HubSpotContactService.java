package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.dto.ContactDTO;
import com.meetime.hubspotintegration.dto.ContactResponseDTO;

public interface HubSpotContactService {

    ContactResponseDTO createContact(ContactDTO contact, String accessToken);
}
