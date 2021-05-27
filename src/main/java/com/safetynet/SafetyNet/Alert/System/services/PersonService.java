package com.safetynet.SafetyNet.Alert.System.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.repository.PersonRepository;
import org.springframework.asm.TypeReference;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
@Service
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

  /*  ObjectMapper objectMapper = new ObjectMapper();
    InputStream inputStream = new FileInputStream(new File("src/main/resources/data.json"));
    TypeReference<List<Person>> typeReference = new TypeReference<List<Person>>() {};*/

    public Person save(Person person){
        return this.personRepository.save(person);
    }

    /*public List<Person> getPersons(){
        return this.personRepository.findAll();
    }*/
}
