package com.google.hospital.pharmacy.repository;

import com.google.hospital.pharmacy.entity.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, UUID> {
    List<StockTransaction> findByItemId(UUID itemId);

    List<StockTransaction> findByBatchId(UUID batchId);
}
