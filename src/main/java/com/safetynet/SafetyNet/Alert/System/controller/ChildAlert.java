package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/childAlert")
public class ChildAlert {

    private final PersonService personService;

    public ChildAlert(PersonService personService) {
        this.personService = personService;
    }


    /*@GetMapping
    public List<Person> getPerson(@RequestParam("address") String address) throws IOException {
        return this.personService.getChildren(address);
    }*/
}
