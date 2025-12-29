package com.google.hospital.billing.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "payment_date_time", nullable = false)
    private LocalDateTime paymentDateTime = LocalDateTime.now();

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_mode")
    private String paymentMode; // CASH, CARD, UPI
}
