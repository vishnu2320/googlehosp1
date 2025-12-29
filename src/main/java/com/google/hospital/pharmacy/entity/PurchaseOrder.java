package com.google.hospital.pharmacy.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "purchase_order")
@Getter
@Setter
public class PurchaseOrder extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    private String vendorName;
    private LocalDate orderDate;
    private String status;
}
