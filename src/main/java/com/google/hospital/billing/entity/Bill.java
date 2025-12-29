package com.google.hospital.billing.entity;

import com.google.hospital.billing.enums.BillStatus;
import com.google.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bill")
@Getter
@Setter
public class Bill extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(nullable = false)
    private UUID patientId;

    @Column(nullable = false)
    private UUID visitId;

    @Column(nullable = false, unique = true)
    private String billNo;

    private LocalDateTime billDateTime;

    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal netAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;

    @Enumerated(EnumType.STRING)
    private BillStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String paymentModeSummary; // Storing as JSON string for now

    @OneToMany(mappedBy = "billId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<BillItem> items;
}
