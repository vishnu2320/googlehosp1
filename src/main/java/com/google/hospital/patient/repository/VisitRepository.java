package com.google.hospital.patient.repository;

import com.google.hospital.patient.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {
    List<Visit> findByPatientId(UUID patientId);

    List<Visit> findByHospitalId(UUID hospitalId);
}
