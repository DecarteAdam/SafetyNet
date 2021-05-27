package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.repository.PersonRepository;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {



    private final PersonService personService;

    public PersonController(PersonService personService){
        this.personService = personService;
    }
    // CRUD
   /* @GetMapping("Persons")
    public List<Person> getPerson(){

        return this.personService.getPersons();
    }*/

    @PostMapping
    public Person savePerson(@RequestBody Person person){

        return this.personService.save(person);
    }
/*
    @PutMapping("Person")
    public Person updatePerson(@RequestBody Person person,
                               @RequestParam("f") String firstName,
                               @RequestParam("l") String lastName){

        return personRepository.updatePerson(person);
    }

    @DeleteMapping("Person/{firstName}")
    public String deletePerson(@PathVariable String firstName){

        return personRepository.deletePerson(firstName);
    }*/
}
