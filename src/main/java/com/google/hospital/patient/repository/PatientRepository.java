package com.google.hospital.patient.repository;

import com.google.hospital.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    @Query("SELECT p FROM Patient p WHERE p.hospitalId = :hospitalId AND " +
            "(LOWER(p.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "p.phone LIKE CONCAT('%', :query, '%') OR " +
            "p.mrn LIKE CONCAT('%', :query, '%'))")
    List<Patient> searchPatients(@Param("hospitalId") UUID hospitalId, @Param("query") String query);

    boolean existsByHospitalIdAndPhone(UUID hospitalId, String phone);
}
