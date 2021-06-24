package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService = personService;
    }
    // CRUD
    @GetMapping("persons")
    public List<Person> getPerson() throws IOException {

        return this.personService.getPersons();
    }

    @PostMapping
    public Person savePerson(@RequestBody Person person) throws IOException {

        return this.personService.savePerson(person);
    }
    @PutMapping("person")
    public Person updatePerson(@RequestBody Person person,
                               @RequestParam("f") String firstName,
                               @RequestParam("l") String lastName) throws IOException {

        return this.personService.updatePerson(person, firstName, lastName);
    }

    @DeleteMapping("person")
    public Person deletePerson(@RequestParam("f") String firstName,
                               @RequestParam("l") String lastName) throws IOException {

        return this.personService.deletePerson(firstName, lastName);
    }
}
