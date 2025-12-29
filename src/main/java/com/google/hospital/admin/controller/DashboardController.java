package com.google.hospital.admin.controller;

import com.google.hospital.admin.dto.DashboardStats;
import com.google.hospital.admin.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardStats> getDashboardStats(
            @RequestParam(defaultValue = "3fa85f64-5717-4562-b3fc-2c963f66afa6") UUID hospitalId) {
        return ResponseEntity.ok(dashboardService.getStats(hospitalId));
    }
}
