package com.google.hospital.pharmacy.entity;

import com.google.hospital.common.BaseEntity;
import com.google.hospital.pharmacy.enums.StockSource;
import com.google.hospital.pharmacy.enums.TxnType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_transaction")
@Getter
@Setter
public class StockTransaction extends BaseEntity {

    @Column(nullable = false)
    private UUID itemId;
    private UUID batchId;

    @Enumerated(EnumType.STRING)
    private TxnType txnType;

    @Enumerated(EnumType.STRING)
    private StockSource source;

    private Integer qty;
    private LocalDateTime txnDate;
    private String referenceType;
    private String referenceId;
}
