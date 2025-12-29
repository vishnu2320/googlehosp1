package com.google.hospital.pharmacy.controller;

import com.google.hospital.pharmacy.dto.IssueRequest;
import com.google.hospital.pharmacy.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PharmacyIssueController {

    @Autowired
    private PharmacyService pharmacyService;

    @PostMapping("/bills/{billId}/pharmacy-issues")
    public ResponseEntity<String> issueMedicines(@PathVariable UUID billId, @RequestBody IssueRequest request) {
        pharmacyService.issueMedicines(billId, request);
        return ResponseEntity.ok("Medicines issued successfully");
    }
}
