package com.google.hospital.pharmacy.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item")
@Getter
@Setter
public class Item extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    private String itemCode;
    private String itemName;
    private String category;
    private String hsnCode;
    private String uom;
    private BigDecimal gstPercent;
    private BigDecimal mrp;
    private BigDecimal salePriceDefault;
    private Boolean isDrug;
    private Boolean isConsumable;
    private String pharmaCompany;
}
