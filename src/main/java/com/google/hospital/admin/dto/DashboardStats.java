package com.google.hospital.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStats {
    private long totalPatients;
    private long totalDoctors;
    private long totalAppointments;
    private BigDecimal totalRevenue;
}
