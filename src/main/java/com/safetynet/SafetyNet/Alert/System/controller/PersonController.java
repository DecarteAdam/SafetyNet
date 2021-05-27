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
    @GetMapping("Persons")
    public List<Person> getPerson() throws IOException {

        return this.personService.getPersons();
    }

    @PostMapping
    public void savePerson(@RequestBody Person person) throws IOException {

        this.personService.savePerson(person);
    }
    @PutMapping("Person")
    public void updatePerson(@RequestBody Person person,
                               @RequestParam("f") String firstName,
                               @RequestParam("l") String lastName) throws IOException {

        this.personService.updatePerson(person, firstName, lastName);
    }

    /*@DeleteMapping("Person/{firstName}")
    public String deletePerson(@PathVariable String firstName){

        return personRepository.deletePerson(firstName);
    }*/
}
