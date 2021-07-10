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

    /**
     * return all persons from JSON file
     * @return The list of persons
     */
    @GetMapping("persons")
    public List<Person> getPerson() throws IOException {
        return this.personService.getPersons();
    }

    /**
     * Save person into JSON
     * @param person The person object to save
     * @return The saved person object
     */
    @PostMapping
    public Person savePerson(@RequestBody Person person) throws IOException {
        return this.personService.savePerson(person);
    }

    /**
     * Update existing person in JSON file
     * @param person The person to update
     * @param firstName The person's first name used as id
     * @param lastName The person's last name used as id
     * @return The updated person
     */
    @PutMapping
    public Person updatePerson(@RequestBody Person person,
                               @RequestParam("f") String firstName,
                               @RequestParam("l") String lastName) throws IOException {

        return this.personService.updatePerson(person, firstName, lastName);
    }

    /**
     * Delete the person from JSON file
     * @param firstName The person's first name used as id
     * @param lastName The person's last name used as id
     */
    @DeleteMapping
    public void deletePerson(@RequestParam("f") String firstName,
                               @RequestParam("l") String lastName) throws IOException {

         this.personService.deletePerson(firstName, lastName);
    }
}
