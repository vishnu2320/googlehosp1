package com.google.hospital.patient.controller;

import com.google.hospital.patient.entity.Patient;
import com.google.hospital.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    // S2-1: Register Patient
    @Test
    @WithMockUser(roles = "RECEPTION")
    public void testRegisterPatient_Success() throws Exception {
        Patient mockPatient = new Patient();
        mockPatient.setId(UUID.randomUUID());
        mockPatient.setFirstName("John");
        mockPatient.setLastName("Doe");
        mockPatient.setMrn("MRN-12345");
        mockPatient.setPhone("9876543210");

        when(patientService.registerPatient(any(Patient.class))).thenReturn(mockPatient);

        String payload = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "gender": "MALE",
                    "dob": "1990-01-01",
                    "phone": "9876543210",
                    "hospitalId": "11111111-1111-1111-1111-111111111111"
                }
                """;

        mockMvc.perform(post("/api/hospitals/11111111-1111-1111-1111-111111111111/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk()) // Controller returns 200 OK, not Created
                .andExpect(jsonPath("$.mrn").value("MRN-12345"))
                .andExpect(jsonPath("$.phone").value("9876543210"));
    }

    // S2-2: Search Patient
    @Test
    @WithMockUser(roles = "RECEPTION")
    public void testSearchPatient_Success() throws Exception {
        Patient mockPatient = new Patient();
        mockPatient.setFirstName("Alice");
        mockPatient.setMrn("MRN-FINDME");

        when(patientService.searchPatients(any(UUID.class), any(String.class)))
                .thenReturn(Collections.singletonList(mockPatient));

        mockMvc.perform(get("/api/hospitals/11111111-1111-1111-1111-111111111111/patients")
                .param("query", "Alice")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alice"));
    }
}
