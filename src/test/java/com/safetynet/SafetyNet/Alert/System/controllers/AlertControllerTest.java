package com.safetynet.SafetyNet.Alert.System.controllers;

import com.safetynet.SafetyNet.Alert.System.controller.AlertController;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AlertController.class)
public class AlertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void phoneAlert() throws Exception {
        mockMvc.perform(get("/phoneAlert")
                .param("firestation", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void fireAlert() throws Exception {
        mockMvc.perform(get("/fire")
                .param("address", "5 rue lauth 67000 Strasbourg"))
                .andExpect(status().isOk());
    }

    @Test
    public void childAlert() throws Exception {
        mockMvc.perform(get("/childAlert")
                .param("address", "5 rue lauth, 67000 Strasbourg"))
                .andExpect(status().isOk());
    }
}
