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

    public void savePerson(Person person) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        dataModel.getPersons().add(person);
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
    }

    public void updatePerson(Person person, String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        for (int i = 0; i < dataModel.getPersons().size(); i++){
            dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName));
            if (i == 1){ // FIXME: 09/06/2021 Object deleted but but not updated or created
                break;
            }
            dataModel.getPersons().add(person);
        }
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
    }

    public List<Person> getPersons() throws IOException {
        return readJsonFile.readFile().getPersons();
    }

    public void deletePerson(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        for (int i = 0; i < dataModel.getPersons().size(); i++){
            dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName));

        }
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
    }
}
