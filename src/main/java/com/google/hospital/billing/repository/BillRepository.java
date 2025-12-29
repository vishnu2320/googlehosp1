package com.google.hospital.billing.repository;

import com.google.hospital.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientId(UUID patientId);

    List<Bill> findByVisitId(UUID visitId);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(b.netAmount) FROM Bill b WHERE b.hospitalId = :hospitalId")
    java.math.BigDecimal sumNetAmountByHospitalId(UUID hospitalId);

    List<Bill> findByHospitalId(UUID hospitalId);
}
