package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PersonService {

    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;

    public PersonService(ReadJsonFile readJsonFile, WriteJsonFile writeJsonFile) {
        this.readJsonFile = readJsonFile;
        this.writeJsonFile = writeJsonFile;
    }

    public Person savePerson(Person person) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        dataModel.getPersons().add(person);
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        return person;
    }

    public Person updatePerson(Person person, String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))){
            dataModel.getPersons().add(person);
            return this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }

        return null;


    }

    public List<Person> getPersons() throws IOException {
        return readJsonFile.readFile().getPersons();
    }

    public Person deletePerson(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))){
            return this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }

        return null;
    }
}
