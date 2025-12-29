package com.google.hospital.patient.controller;

import com.google.hospital.patient.entity.Visit;
import com.google.hospital.patient.service.VisitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitService visitService;

    // S2-3: Create OP Visit
    @Test
    @WithMockUser(roles = "RECEPTION")
    public void testCreateVisit_Success() throws Exception {
        Visit mockVisit = new Visit();
        mockVisit.setId(UUID.randomUUID());
        mockVisit.setStatus("PLANNED"); // Use String, not Enum
        mockVisit.setType("OP");

        when(visitService.createVisit(any(Visit.class))).thenReturn(mockVisit);

        String payload = """
                {
                    "patientId": "22222222-2222-2222-2222-222222222222",
                    "doctorId": "33333333-3333-3333-3333-333333333333",
                    "type": "OP",
                    "reason": "Fever"
                }
                """;

        mockMvc.perform(post("/api/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("OP"))
                .andExpect(jsonPath("$.status").value("PLANNED"));
    }
}
