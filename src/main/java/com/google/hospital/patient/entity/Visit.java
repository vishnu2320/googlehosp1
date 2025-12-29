package com.google.hospital.patient.entity;

import com.google.hospital.common.BaseEntity;
import com.google.hospital.patient.enums.VisitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "visit")
@Getter
@Setter
public class Visit extends BaseEntity {

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID hospitalId;

    @Enumerated(EnumType.STRING)
    private VisitType visitType;

    private String visitNo;

    private UUID doctorId;
    private UUID departmentId;

    private LocalDateTime visitDateTime;
    private String status; // e.g. OPEN, CLOSED
}
