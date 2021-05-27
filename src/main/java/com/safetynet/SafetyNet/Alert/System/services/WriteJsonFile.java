package com.safetynet.SafetyNet.Alert.System.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
@Service
public class WriteJsonFile {

    public void writeFile(Person person, String firstName, String lastName) throws IOException {

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("src/main/resources/person2.json");
        DataModel test = objectMapper.readValue(file, DataModel.class);

        long count;
        if (firstName != null && lastName != null){
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(firstName) && o.getLastName().equals(lastName)).count();
            test.getPersons().add(person);
        }else {
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(person.getFirstName()) && o.getLastName().equals(person.getLastName())).count();

        }
        if(count == 0) {
            test.getPersons().add(person);
        }

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, test);
    }

}

