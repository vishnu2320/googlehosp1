package com.google.hospital.lab.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "lab_test")
@Getter
@Setter
public class LabTest extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String code; // e.g., CBC, XRAY-CHEST

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private UUID hospitalId;
}
