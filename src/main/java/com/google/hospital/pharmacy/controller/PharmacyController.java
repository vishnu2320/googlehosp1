package com.google.hospital.pharmacy.controller;

import com.google.hospital.pharmacy.entity.Item;
import com.google.hospital.pharmacy.service.PharmacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @PostMapping("/hospitals/{hospitalId}/items")
    public ResponseEntity<Item> createItem(@PathVariable UUID hospitalId, @RequestBody Item item) {
        item.setHospitalId(hospitalId);
        return ResponseEntity.ok(pharmacyService.createItem(item));
    }

    @GetMapping("/hospitals/{hospitalId}/stock")
    public ResponseEntity<List<Item>> getStock(@PathVariable UUID hospitalId) {
        return ResponseEntity.ok(pharmacyService.getStockSummary(hospitalId));
    }
}
