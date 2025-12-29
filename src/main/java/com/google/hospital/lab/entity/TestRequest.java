package com.google.hospital.lab.entity;

import com.google.hospital.common.BaseEntity;
import com.google.hospital.lab.enums.TestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "test_request")
@Getter
@Setter
public class TestRequest extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID doctorId;

    @Column(nullable = false)
    private UUID labTestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    private LocalDateTime requestDate;
    private LocalDateTime resultDate;

    @Column(length = 2000)
    private String result; // Simple text result for MVP. Could be JSON or separate entity.

    private String remarks;
}
