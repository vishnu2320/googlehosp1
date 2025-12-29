package com.google.hospital.pharmacy.repository;

import com.google.hospital.pharmacy.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findByHospitalId(UUID hospitalId);

    // Suggest search query
    List<Item> findByHospitalIdAndItemNameContainingIgnoreCase(UUID hospitalId, String name);
}
