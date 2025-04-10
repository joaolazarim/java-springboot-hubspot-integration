package com.meetime.hubspotintegration.dto;

import lombok.Data;

@Data
public class HistoryEntryDTO {

    private String sourceId;
    private String sourceType;
    private String sourceLabel;
    private Integer updatedByUserId;
    private String value;
    private String timestamp;
}