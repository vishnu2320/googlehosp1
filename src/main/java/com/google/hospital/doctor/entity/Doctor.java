package com.google.hospital.doctor.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "doctor")
@Getter
@Setter
public class Doctor extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    private UUID departmentId;
    private String name;
    private String speciality;
    private String specialization; // for DatabaseSeeder compatibility
    private String registrationNo;
    private String licenseNo; // for DatabaseSeeder compatibility
    private String qualification;
    private BigDecimal consultationFeeDefault;
    private Boolean activeFlag;
}
