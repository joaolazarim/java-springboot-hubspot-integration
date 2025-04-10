package com.meetime.hubspotintegration.controller;

import com.meetime.hubspotintegration.dto.TokenResponseDTO;
import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import com.meetime.hubspotintegration.exception.StateSignatureException;
import com.meetime.hubspotintegration.service.HubSpotAuthService;
import com.meetime.hubspotintegration.util.StateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final HubSpotAuthService hubSpotAuthService;
    private final StateUtils stateUtils;

    public AuthController(HubSpotAuthService hubSpotAuthService, StateUtils stateUtils) {
        this.hubSpotAuthService = hubSpotAuthService;
        this.stateUtils = stateUtils;
    }

    @GetMapping("/url")
    public String getAuthorizationUrl() {
        String state = UUID.randomUUID().toString();
        String signedState = stateUtils.generateSignedState(state);

        return hubSpotAuthService.getAuthorizationUrl(signedState);
    }

    @GetMapping("/callback")
    public TokenResponseDTO callback(@RequestParam("code") String code,
                                     @RequestParam("state") String state) {

        if (!stateUtils.verifySignedState(state)) {
            logger.error(ErrorMessageEnum.INVALID_STATE.getMessage());
            throw new StateSignatureException(ErrorMessageEnum.INVALID_STATE.getMessage());
        }

        return hubSpotAuthService.exchangeCodeForToken(code);
    }
}