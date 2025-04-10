package com.meetime.hubspotintegration.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ContactResponseDTO {

    private String createdAt;
    private boolean archived;
    private String archivedAt;
    private Map<String, List<HistoryEntryDTO>> propertiesWithHistory;
    private String id;
    private String objectWriteTraceId;
    private Map<String, Object> properties;
    private String updatedAt;
}