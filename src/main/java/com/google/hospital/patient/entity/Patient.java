package com.google.hospital.patient.entity;

import com.google.hospital.common.BaseEntity;
import com.google.hospital.common.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "patient")
@Getter
@Setter
public class Patient extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(unique = true, nullable = false)
    private String mrn;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate dob;
    private String phone;
    private String email;
    private String address;
    private String city;

    private String idProofType;
    private String idProofNo;
}
