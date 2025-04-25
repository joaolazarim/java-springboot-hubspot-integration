package com.meetime.hubspotintegration.controller;

import com.meetime.hubspotintegration.enums.ErrorMessageEnum;
import com.meetime.hubspotintegration.util.WebhookSignatureValidator;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final WebhookSignatureValidator validator;

    public WebhookController(WebhookSignatureValidator validator) {
        this.validator = validator;
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> handleContactWebhook(HttpServletRequest request, @RequestBody String rawBody) {
        if (!validator.isValid(request, rawBody)) {
            logger.error(ErrorMessageEnum.INVALID_HUBSPOT_SIGNATURE.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorMessageEnum.INVALID_HUBSPOT_SIGNATURE.getMessage());
        }

        logger.info("Valid HubSpot signature");
        logger.info("Received contact webhook request: {}", rawBody);

        return ResponseEntity.ok("Webhook received successfully");
    }
}