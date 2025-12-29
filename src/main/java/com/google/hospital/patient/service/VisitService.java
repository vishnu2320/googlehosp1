package com.google.hospital.patient.service;

import com.google.hospital.patient.entity.Visit;
import com.google.hospital.patient.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    public Visit createVisit(Visit visit) {
        if (visit.getVisitDateTime() == null) {
            visit.setVisitDateTime(LocalDateTime.now());
        }
        visit.setStatus("OPEN");
        // visitNo logic omitted for MVP brevity
        visit.setVisitNo("VIS-" + System.currentTimeMillis());
        return visitRepository.save(visit);
    }

    public Visit getVisit(UUID id) {
        return visitRepository.findById(id).orElseThrow(() -> new RuntimeException("Visit not found"));
    }

    public List<Visit> getVisitsByPatient(UUID patientId) {
        return visitRepository.findByPatientId(patientId);
    }
}
