package com.meetime.hubspotintegration.controller;

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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    private final WebhookSignatureValidator validator;

    public WebhookController(WebhookSignatureValidator validator) {
        this.validator = validator;
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> handleContactWebhook(HttpServletRequest request, @RequestBody List<Map<String,Object>> payload) {
        if (!validator.isValid(request, payload)) {
            logger.error("Invalid HubSpot signature");
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid HubSpot signature");
        }

        logger.info("Valid HubSpot signature");
        logger.info("Received contact webhook request: {}", payload);

        return ResponseEntity.ok("Webhook received successfully");
    }
}