package com.safetynet.SafetyNet.Alert.System.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WriteJsonFile {

    ObjectMapper objectMapper = new ObjectMapper();
    File file = new File("src/main/resources/person2.json");

    public void writeFilePerson(List<Person> person) throws IOException {

        //create ObjectMapper instance
        DataModel dataModel = objectMapper.readValue(file, DataModel.class);

        /*long count;
        if (firstName != null && lastName != null){
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(firstName) && o.getLastName().equals(lastName)).count();
            test.getPersons().add(person);
        }else {
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(person.getFirstName()) && o.getLastName().equals(person.getLastName())).count();

        }
        if(count == 0) {
            test.getPersons().add(person);
        }*/

        dataModel.getPersons().clear();
        dataModel.getPersons().addAll(person);

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, dataModel);
    }

    public void writeFileFireStation(List<FireStation> fireStations) throws IOException {

        //create ObjectMapper instance
        DataModel test = objectMapper.readValue(file, DataModel.class);

        /*long count;
        if (firstName != null && lastName != null){
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(firstName) && o.getLastName().equals(lastName)).count();
            test.getPersons().add(person);
        }else {
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(person.getFirstName()) && o.getLastName().equals(person.getLastName())).count();

        }
        if(count == 0) {
            test.getPersons().add(person);
        }*/

        test.getMedicalrecords().clear();
        test.getFirestations().addAll(fireStations);

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, test);
    }

    public void writeFileMedicalRecords(List<MedicalRecords> medicalRecords) throws IOException {

        //create ObjectMapper instance
        DataModel test = objectMapper.readValue(file, DataModel.class);

        /*long count;
        if (firstName != null && lastName != null){
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(firstName) && o.getLastName().equals(lastName)).count();
            test.getPersons().add(person);
        }else {
            count = test.getPersons().stream().filter(o -> o.getFirstName().equals(person.getFirstName()) && o.getLastName().equals(person.getLastName())).count();

        }
        if(count == 0) {
            test.getPersons().add(person);
        }*/

        test.getMedicalrecords().clear();
        test.getMedicalrecords().addAll(medicalRecords);

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, test);
    }

}

