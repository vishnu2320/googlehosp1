package com.google.hospital.doctor.service;

import com.google.hospital.doctor.entity.Appointment;
import com.google.hospital.doctor.enums.AppointmentStatus;
import com.google.hospital.doctor.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment bookAppointment(Appointment appointment) {
        appointment.setStatus(AppointmentStatus.BOOKED);
        // Add overlap validation logic here for production
        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getDoctorAppointments(UUID doctorId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        return appointmentRepository.findByDoctorIdAndScheduledStartBetween(doctorId, start, end);
    }

    public Appointment updateStatus(UUID id, AppointmentStatus status) {
        Appointment apt = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        apt.setStatus(status);
        return appointmentRepository.save(apt);
    }
}
