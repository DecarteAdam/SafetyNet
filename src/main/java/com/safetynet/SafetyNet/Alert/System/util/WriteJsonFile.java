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

    public Person writeFilePerson(List<Person> person) throws IOException {

        //create ObjectMapper instance
        DataModel dataModel = objectMapper.readValue(file, DataModel.class);

        dataModel.getPersons().clear();
        dataModel.getPersons().addAll(person);

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, dataModel);

        return dataModel.getPersons().get(dataModel.getPersons().size() -1);
    }

    public FireStation writeFileFireStation(List<FireStation> fireStations) throws IOException {

        //create ObjectMapper instance
        DataModel dataModel = objectMapper.readValue(file, DataModel.class);

        dataModel.getFirestations().clear();
        dataModel.getFirestations().addAll(fireStations);

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, dataModel);

        return dataModel.getFirestations().get(dataModel.getFirestations().size() -1);
    }

    public void writeFileMedicalRecords(List<MedicalRecords> medicalRecords) throws IOException {

        //create ObjectMapper instance
        DataModel dataModel = objectMapper.readValue(file, DataModel.class);

        dataModel.getMedicalrecords().clear();
        dataModel.getMedicalrecords().addAll(medicalRecords);

        //configure objectMapper for pretty input
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //write customerObj object to person2.json file
        objectMapper.writeValue(file, dataModel);
    }

}

