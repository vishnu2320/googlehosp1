package com.google.hospital.pharmacy.repository;

import com.google.hospital.pharmacy.entity.ItemBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemBatchRepository extends JpaRepository<ItemBatch, UUID> {
    List<ItemBatch> findByItemId(UUID itemId);

    List<ItemBatch> findByItemIdAndCurrentQtyGreaterThan(UUID itemId, Integer qty);
}
