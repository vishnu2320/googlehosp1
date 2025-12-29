package com.google.hospital.lab.repository;

import com.google.hospital.lab.entity.TestRequest;
import com.google.hospital.lab.enums.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestRequestRepository extends JpaRepository<TestRequest, UUID> {
    List<TestRequest> findByHospitalIdAndStatus(UUID hospitalId, TestStatus status);

    List<TestRequest> findByPatientId(UUID patientId);

    List<TestRequest> findByDoctorId(UUID doctorId);
}
