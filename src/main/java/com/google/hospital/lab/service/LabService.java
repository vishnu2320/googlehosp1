package com.google.hospital.lab.service;

import com.google.hospital.lab.entity.LabTest;
import com.google.hospital.lab.entity.TestRequest;
import com.google.hospital.lab.enums.TestStatus;
import com.google.hospital.lab.repository.LabTestRepository;
import com.google.hospital.lab.repository.TestRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LabService {

    @Autowired
    private LabTestRepository labTestRepository;

    @Autowired
    private TestRequestRepository testRequestRepository;

    public LabTest createLabTest(LabTest labTest) {
        return labTestRepository.save(labTest);
    }

    public List<LabTest> getAllLabTests(UUID hospitalId) {
        return labTestRepository.findByHospitalId(hospitalId);
    }

    public TestRequest createTestRequest(TestRequest request) {
        request.setStatus(TestStatus.PENDING);
        request.setRequestDate(LocalDateTime.now());
        return testRequestRepository.save(request);
    }

    public List<TestRequest> getPendingRequests(UUID hospitalId) {
        return testRequestRepository.findByHospitalIdAndStatus(hospitalId, TestStatus.PENDING);
    }

    public List<TestRequest> getPatientRequests(UUID patientId) {
        return testRequestRepository.findByPatientId(patientId);
    }

    public TestRequest updateTestResult(UUID requestId, String result, String remarks) {
        TestRequest request = testRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Test Request not found"));

        request.setResult(result);
        request.setRemarks(remarks);
        request.setStatus(TestStatus.COMPLETED);
        request.setResultDate(LocalDateTime.now());

        return testRequestRepository.save(request);
    }
}
