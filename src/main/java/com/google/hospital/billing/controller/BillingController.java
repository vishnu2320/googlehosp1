package com.google.hospital.billing.controller;

import com.google.hospital.billing.entity.Bill;
import com.google.hospital.billing.entity.BillItem;
import com.google.hospital.billing.entity.Payment;
import com.google.hospital.billing.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BillingController {

    @Autowired
    private BillingService billingService;

    @PostMapping("/visits/{visitId}/bills")
    public ResponseEntity<Bill> createBill(@PathVariable UUID visitId,
            @RequestBody com.google.hospital.billing.dto.CreateBillRequest request) {
        request.setPatientId(request.getPatientId()); // Just ensuring
        request.setVisitId(visitId);
        return ResponseEntity.ok(billingService.createBill(request));
    }

    @GetMapping("/bills/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable UUID id) {
        return ResponseEntity.ok(billingService.getBill(id));
    }

    @PostMapping("/bills/{id}/items")
    public ResponseEntity<Bill> addItem(@PathVariable UUID id, @RequestBody BillItem item) {
        return ResponseEntity.ok(billingService.addItem(id, item));
    }

    @PostMapping("/bills/{id}/payments")
    public ResponseEntity<Bill> addPayment(@PathVariable UUID id, @RequestBody Payment payment) {
        return ResponseEntity.ok(billingService.addPayment(id, payment));
    }
}
