package com.google.hospital.pharmacy.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "item_batch")
@Getter
@Setter
public class ItemBatch extends BaseEntity {

    @Column(nullable = false)
    private UUID itemId;

    private String batchNo;
    private LocalDate expiryDate;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Integer openingQty;
    private Integer currentQty;
}
