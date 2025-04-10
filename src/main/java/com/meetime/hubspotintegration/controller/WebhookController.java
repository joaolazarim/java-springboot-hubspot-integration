package com.meetime.hubspotintegration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @PostMapping("/contacts")
    public ResponseEntity<String> handleContactWebhook(@RequestBody List<Map<String, Object>> payload) {
        logger.info("Received contact webhook request: {}", payload);
        return ResponseEntity.ok("Webhook received successfully");
    }
}