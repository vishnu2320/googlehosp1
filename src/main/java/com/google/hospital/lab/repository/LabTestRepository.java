package com.google.hospital.lab.repository;

import com.google.hospital.lab.entity.LabTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LabTestRepository extends JpaRepository<LabTest, UUID> {
    List<LabTest> findByHospitalId(UUID hospitalId);
}
