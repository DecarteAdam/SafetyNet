package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fire")
public class Fire {

    private final PersonService personService;

    public Fire(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Map<String, Object>> getPhones(@RequestParam("address") String address) throws IOException {
        return this.personService.getPersonsFromFire(address);
    }
}
