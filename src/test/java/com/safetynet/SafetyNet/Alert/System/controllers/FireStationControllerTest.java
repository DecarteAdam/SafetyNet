package com.safetynet.SafetyNet.Alert.System.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNet.Alert.System.controller.FireStationController;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.services.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FireStationController.class)
public class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FireStationService fireStationService;

    @Test
    public void testGetFireStation() throws Exception {
        mockMvc.perform(get("/firestation/firestations"))
                .andExpect(status().isOk());
    }

    @Test
    public void createNewFireStation() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("Address");

        mockMvc.perform(post("/firestation")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFireStation() throws Exception {
        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("Address");

        mockMvc.perform(put("/firestation")
                .contentType("application/json")
                .param("s", fireStation.getStation())
                .param("a", fireStation.getAddress())
                .content(objectMapper.writeValueAsString(fireStation)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFireStation() throws Exception {
        mockMvc.perform(delete("/firestation")
                .param("s", "1")
                .param("a", "Address"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetPersonsByStation() throws Exception {
        mockMvc.perform(get("/firestation")
                .param("stationNumber", "1"))
                .andExpect(status().isOk());
    }
}
