package com.google.hospital.patient.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "patient_photo")
@Getter
@Setter
public class PatientPhoto extends BaseEntity {

    @Column(nullable = false)
    private UUID patientId;

    private String photoUrl;
    private LocalDateTime capturedAt;
    private String capturedBy;
}
