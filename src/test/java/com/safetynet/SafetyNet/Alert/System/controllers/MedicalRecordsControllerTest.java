package com.safetynet.SafetyNet.Alert.System.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNet.Alert.System.controller.MedicalRecordsController;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.services.MedicalRecordsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordsController.class)
public class MedicalRecordsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MedicalRecordsService medicalRecordsService;

    @Test
    public void testGetMedicalRecords() throws Exception {
        mockMvc.perform(get("/medicalRecord"))
                .andExpect(status().isOk());
    }

    @Test
    public void createNewMedicalRecord() throws Exception {
        MedicalRecords medicalRecords = new MedicalRecords();
        medicalRecords.setFirstName("Test");
        medicalRecords.setLastName("Tester");

        mockMvc.perform(post("/medicalRecord")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(medicalRecords)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMedicalRecords() throws Exception {
        MedicalRecords medicalRecords = new MedicalRecords();
        medicalRecords.setFirstName("Test");
        medicalRecords.setLastName("Tester");

        mockMvc.perform(put("/medicalRecord")
                .contentType("application/json")
                .param("f", medicalRecords.getFirstName())
                .param("l", medicalRecords.getLastName())
                .content(objectMapper.writeValueAsString(medicalRecords)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMedicalRecord() throws Exception {
        mockMvc.perform(delete("/medicalRecord")
                .param("f", "Test")
                .param("l", "Tester"))
                .andExpect(status().isOk());
    }
}
