package com.google.hospital.lab.controller;

import com.google.hospital.lab.entity.LabTest;
import com.google.hospital.lab.entity.TestRequest;
import com.google.hospital.lab.service.LabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LabController {

    @Autowired
    private LabService labService;

    // --- Lab Test Catalog ---

    @PostMapping("/lab-tests")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LabTest> createLabTest(@RequestBody LabTest labTest) {
        // Hardcoded Hospital ID for MVP if not present
        if (labTest.getHospitalId() == null) {
            labTest.setHospitalId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        }
        return ResponseEntity.ok(labService.createLabTest(labTest));
    }

    @GetMapping("/hospitals/{hospitalId}/lab-tests")
    public ResponseEntity<List<LabTest>> getLabTests(@PathVariable UUID hospitalId) {
        return ResponseEntity.ok(labService.getAllLabTests(hospitalId));
    }

    // --- Test Requests ---

    @PostMapping("/test-requests")
    public ResponseEntity<TestRequest> requestTest(@RequestBody TestRequest request) {
        if (request.getHospitalId() == null) {
            request.setHospitalId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        }
        return ResponseEntity.ok(labService.createTestRequest(request));
    }

    @GetMapping("/hospitals/{hospitalId}/test-requests/pending")
    @PreAuthorize("hasRole('ADMIN')") // Admin/Lab Tech only
    public ResponseEntity<List<TestRequest>> getPendingRequests(@PathVariable UUID hospitalId) {
        return ResponseEntity.ok(labService.getPendingRequests(hospitalId));
    }

    @GetMapping("/patients/{patientId}/test-requests")
    public ResponseEntity<List<TestRequest>> getPatientRequests(@PathVariable UUID patientId) {
        return ResponseEntity.ok(labService.getPatientRequests(patientId));
    }

    @PutMapping("/test-requests/{id}/result")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TestRequest> updateResult(@PathVariable UUID id, @RequestBody TestRequest resultData) {
        return ResponseEntity.ok(labService.updateTestResult(id, resultData.getResult(), resultData.getRemarks()));
    }
}
