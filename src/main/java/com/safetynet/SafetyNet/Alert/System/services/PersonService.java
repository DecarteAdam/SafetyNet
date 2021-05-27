package com.safetynet.SafetyNet.Alert.System.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PersonService {



    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;

    public PersonService(ReadJsonFile readJsonFile, WriteJsonFile writeJsonFile) {
        this.readJsonFile = readJsonFile;
        this.writeJsonFile = writeJsonFile;
    }



  /*  ObjectMapper objectMapper = new ObjectMapper();
    InputStream inputStream = new FileInputStream(new File("src/main/resources/data.json"));
    TypeReference<List<Person>> typeReference = new TypeReference<List<Person>>() {};*/

    public void savePerson(Person person) throws IOException {
        this.writeFile(person);
    }

    public List<Person> getPersons() throws IOException {
        return this.readJsonFile.readFile().getPersons();
    }

    public void updatePerson(Person person, String firstName, String lastName) throws IOException {
        this.writeJsonFile.writeFile(person, firstName, lastName);
    }

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

    public List<Person> readFile() throws IOException {

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("src/main/resources/person2.json");
        DataModel test = objectMapper.readValue(file, DataModel.class);

         return test.getPersons();

    }

}
