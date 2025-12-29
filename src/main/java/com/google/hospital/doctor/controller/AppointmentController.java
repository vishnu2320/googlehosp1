package com.google.hospital.doctor.controller;

import com.google.hospital.doctor.entity.Appointment;
import com.google.hospital.doctor.enums.AppointmentStatus;
import com.google.hospital.doctor.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/doctors/{doctorId}/appointments")
    public ResponseEntity<Appointment> bookAppointment(@PathVariable UUID doctorId,
            @RequestBody Appointment appointment) {
        appointment.setDoctorId(doctorId);
        return ResponseEntity.ok(appointmentService.bookAppointment(appointment));
    }

    @GetMapping("/doctors/{doctorId}/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(
            @PathVariable UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getDoctorAppointments(doctorId, date));
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateStatus(@PathVariable UUID id, @RequestBody AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }
}
