package com.google.hospital.pharmacy.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class IssueRequest {
    private UUID hospitalId;
    private List<IssueItem> items;

    @Getter
    @Setter
    public static class IssueItem {
        private UUID itemId;
        private UUID batchId;
        private Integer quantity;
    }
}
