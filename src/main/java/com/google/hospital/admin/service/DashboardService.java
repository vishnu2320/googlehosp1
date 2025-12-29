package com.google.hospital.admin.service;

import com.google.hospital.admin.dto.DashboardStats;
import com.google.hospital.billing.repository.BillRepository;
import com.google.hospital.doctor.repository.AppointmentRepository;
import com.google.hospital.doctor.repository.DoctorRepository;
import com.google.hospital.patient.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillRepository billRepository;

    public DashboardStats getStats(UUID hospitalId) {
        long patientCount = patientRepository.count(); // In real app, filter by hospitalId
        long doctorCount = doctorRepository.count();
        long appointmentCount = appointmentRepository.count();
        BigDecimal revenue = billRepository.sumNetAmountByHospitalId(hospitalId);

        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        return new DashboardStats(patientCount, doctorCount, appointmentCount, revenue);
    }
}
