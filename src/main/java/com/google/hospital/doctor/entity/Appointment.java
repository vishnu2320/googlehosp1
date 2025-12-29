package com.google.hospital.doctor.entity;

import com.google.hospital.common.BaseEntity;
import com.google.hospital.doctor.enums.AppointmentStatus;
import com.google.hospital.doctor.enums.BookedChannel;
import com.google.hospital.patient.enums.VisitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "appointment")
@Getter
@Setter
public class Appointment extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID doctorId;

    @Enumerated(EnumType.STRING)
    private VisitType visitType;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Enumerated(EnumType.STRING)
    private BookedChannel bookedChannel;

    private String createdBy;
}
