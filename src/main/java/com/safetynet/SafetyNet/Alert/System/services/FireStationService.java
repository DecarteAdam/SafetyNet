package com.safetynet.SafetyNet.Alert.System.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
@Service
public class FireStationService {

    public void writeFile(Person person) throws IOException {

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("src/main/resources/person2.json");
        DataModel test = objectMapper.readValue(file, DataModel.class);

        if(person != null){
            long count = test.getPersons().stream().filter(o -> o.getFirstName().equals(person.getFirstName()) && o.getLastName().equals(person.getLastName())).count();

            if(count == 0) {
                test.getPersons().add(person);
            }
        }

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, test);
    }
}
