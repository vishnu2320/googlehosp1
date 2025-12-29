package com.google.hospital.patient.repository;

import com.google.hospital.patient.entity.PatientPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientPhotoRepository extends JpaRepository<PatientPhoto, UUID> {
    List<PatientPhoto> findByPatientId(UUID patientId);
}
