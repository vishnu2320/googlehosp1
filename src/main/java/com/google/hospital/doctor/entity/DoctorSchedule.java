package com.google.hospital.doctor.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "doctor_schedule")
@Getter
@Setter
public class DoctorSchedule extends BaseEntity {

    @Column(nullable = false)
    private UUID doctorId;

    @Column(nullable = false)
    private DayOfWeek weekday; // Using Java's DayOfWeek enum

    private LocalTime slotStartTime;
    private LocalTime slotEndTime;
    private Integer maxPatientsPerSlot;
    private String locationRoom;
}
