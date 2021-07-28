package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.PersonWithMedicalRecords;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class AlertController {

    private final PersonService personService;

    public AlertController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhones(@RequestParam("firestation") String station) throws IOException {
        return this.personService.getPhones(station);
    }

    @GetMapping("/childAlert")
    public List<Object> getPerson(@RequestParam("address") String address) throws IOException {
        return this.personService.getChildren(address);
    }

    /**
     * Return persons with medical records from given address and fire station numbers
     * @param address The address of the persons
     * @return list of persons
     */
    @GetMapping("/fire")
    public List<Object> fireAlert(@RequestParam("address") String address) throws IOException {
        return this.personService.getPersonsFromFire(address);
    }

    @GetMapping("/flood")
    public Map<String, List<PersonWithMedicalRecords>> floodAlert(@RequestParam("stations") List<String> stations) throws IOException {
        return this.personService.getPersonsFromFlood(stations);
    }

    @GetMapping("/personInfo")
    public List<Object> personInfo(@RequestParam("firstName") String firstName,
                                   @RequestParam("lastName") String lastName) throws IOException {
        return this.personService.personInfo(firstName, lastName);
    }

    @GetMapping("/communityEmail")
    public List<String> communityEmail(@RequestParam("city") String city) throws IOException {
        return this.personService.communityEmail(city);
    }
}
