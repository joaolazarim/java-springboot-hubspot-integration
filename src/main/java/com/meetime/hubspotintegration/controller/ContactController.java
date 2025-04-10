package com.meetime.hubspotintegration.controller;

import com.meetime.hubspotintegration.dto.ContactDTO;
import com.meetime.hubspotintegration.dto.ContactResponseDTO;
import com.meetime.hubspotintegration.service.HubSpotContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.meetime.hubspotintegration.util.TokenUtils.getToken;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final HubSpotContactService hubSpotContactService;

    public ContactController(HubSpotContactService hubSpotContactService) {
        this.hubSpotContactService = hubSpotContactService;
    }

    @PostMapping
    public ResponseEntity<ContactResponseDTO> createContact(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody ContactDTO contact) {

        String token = getToken(bearerToken);
        ContactResponseDTO response = hubSpotContactService.createContact(contact, token);
        return ResponseEntity.ok(response);
    }
}