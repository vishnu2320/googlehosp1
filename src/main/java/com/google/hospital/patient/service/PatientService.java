package com.google.hospital.patient.service;

import com.google.hospital.patient.entity.Patient;
import com.google.hospital.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByHospitalIdAndPhone(patient.getHospitalId(), patient.getPhone())) {
            throw new IllegalArgumentException("Patient with this mobile number already exists.");
        }

        // Simple MRN generation logic for MVP - in real world this needs a sequence or
        // specialized service
        if (patient.getMrn() == null || patient.getMrn().isEmpty()) {
            patient.setMrn("MRN-" + System.currentTimeMillis());
        }
        return patientRepository.save(patient);
    }

    public Patient getPatient(UUID id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public List<Patient> searchPatients(UUID hospitalId, String query) {
        return patientRepository.searchPatients(hospitalId, query);
    }
}
