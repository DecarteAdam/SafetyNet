package com.safetynet.SafetyNet.Alert.System.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNet.Alert.System.controller.PersonController;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @Test
    public void testGetPersons() throws Exception {
        mockMvc.perform(get("/person/persons"))
                .andExpect(status().isOk());
    }

    @Test
    public void createNewPerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Tester");

        mockMvc.perform(post("/person")
                .contentType("application/json")
        .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateNewPerson() throws Exception {
        Person person = new Person();
        person.setFirstName("Test");
        person.setLastName("Tester");

        mockMvc.perform(put("/person/person")
                .contentType("application/json")
                .param("f", person.getFirstName())
                .param("l", person.getLastName())
                .content(objectMapper.writeValueAsString(person)))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePerson() throws Exception {
        mockMvc.perform(delete("/person/person")
                .param("f", "Test")
                .param("l", "Tester"))
                .andExpect(status().isOk());
    }
}
