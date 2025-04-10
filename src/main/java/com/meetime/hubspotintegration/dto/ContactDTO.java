package com.meetime.hubspotintegration.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ContactDTO {

    private Map<String, Object> properties;
}