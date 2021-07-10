package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/phoneAlert")
public class PhoneAlert {

    private final PersonService personService;


    public PhoneAlert(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<String> getPhones(@RequestParam("firestation") String station) throws IOException {
        return this.personService.getPhones(station);
    }
}
