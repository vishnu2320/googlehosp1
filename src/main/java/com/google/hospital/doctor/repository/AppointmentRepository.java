package com.google.hospital.doctor.repository;

import com.google.hospital.doctor.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByDoctorIdAndScheduledStartBetween(UUID doctorId, LocalDateTime start, LocalDateTime end);

    List<Appointment> findByHospitalIdAndScheduledStartBetween(UUID hospitalId, LocalDateTime start, LocalDateTime end);
}
