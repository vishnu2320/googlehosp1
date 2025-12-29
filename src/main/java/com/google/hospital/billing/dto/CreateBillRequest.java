package com.google.hospital.billing.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateBillRequest {
    private UUID hospitalId;
    private UUID patientId;
    private UUID visitId;
    private List<BillItemRequest> items;

    @Getter
    @Setter
    public static class BillItemRequest {
        private String serviceCode;
        private String serviceName;
        private BigDecimal unitPrice;
        private Integer quantity;
        private BigDecimal discount;
        private BigDecimal taxPercent;
        private UUID doctorId;
        private UUID departmentId;
    }
}
