package com.google.hospital.billing.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "bill_item")
@Getter
@Setter
public class BillItem extends BaseEntity {

    @Column(nullable = false)
    private UUID billId;

    private String serviceCode;
    private String serviceName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal grossAmount;
    private BigDecimal discountAmount;
    private BigDecimal netAmount;
    private BigDecimal taxAmount;

    private UUID doctorId;
    private UUID departmentId;
    private Boolean isPharmacyItem;
}
