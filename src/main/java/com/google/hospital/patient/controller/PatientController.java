package com.google.hospital.patient.controller;

import com.google.hospital.patient.entity.Patient;
import com.google.hospital.patient.entity.Visit;
import com.google.hospital.patient.service.PatientService;
import com.google.hospital.patient.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private VisitService visitService;

    // Patient APIs
    @PostMapping("/hospitals/{hospitalId}/patients")
    public ResponseEntity<Patient> createPatient(@PathVariable UUID hospitalId, @RequestBody Patient patient) {
        patient.setHospitalId(hospitalId);
        return ResponseEntity.ok(patientService.createPatient(patient));
    }

    @GetMapping("/hospitals/{hospitalId}/patients")
    public ResponseEntity<List<Patient>> searchPatients(@PathVariable UUID hospitalId, @RequestParam String query) {
        return ResponseEntity.ok(patientService.searchPatients(hospitalId, query));
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(patientService.getPatient(id));
    }

    // Visit APIs
    @PostMapping("/patients/{patientId}/visits")
    public ResponseEntity<Visit> createVisit(@PathVariable UUID patientId, @RequestBody Visit visit) {
        visit.setPatientId(patientId);
        return ResponseEntity.ok(visitService.createVisit(visit));
    }

    @GetMapping("/visits/{id}")
    public ResponseEntity<Visit> getVisit(@PathVariable UUID id) {
        return ResponseEntity.ok(visitService.getVisit(id));
    }

    @GetMapping("/patients/{patientId}/visits")
    public ResponseEntity<List<Visit>> getPatientVisits(@PathVariable UUID patientId) {
        return ResponseEntity.ok(visitService.getVisitsByPatient(patientId));
    }

    // Photo APIs
    @Autowired
    private com.google.hospital.common.service.FileStorageService fileStorageService;

    @Autowired
    private com.google.hospital.patient.repository.PatientRepository patientRepository;

    @Autowired
    private com.google.hospital.patient.repository.PatientPhotoRepository patientPhotoRepository;

    @PostMapping("/patients/{id}/photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable UUID id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        // Update Patient entity (optional, or separate PatientPhoto entity)
        // For MVP, we'll CREATE a PatientPhoto record
        com.google.hospital.patient.entity.PatientPhoto photo = new com.google.hospital.patient.entity.PatientPhoto();
        photo.setPatientId(id);
        photo.setPhotoUrl(fileName);
        photo.setCapturedAt(java.time.LocalDateTime.now());
        patientPhotoRepository.save(photo);

        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/patients/photos/{fileName:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> getPhoto(@PathVariable String fileName) {
        org.springframework.core.io.Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = "application/octet-stream";
        // Try to determine file's content type (simplified)
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
            contentType = "image/jpeg";
        else if (fileName.endsWith(".png"))
            contentType = "image/png";

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
