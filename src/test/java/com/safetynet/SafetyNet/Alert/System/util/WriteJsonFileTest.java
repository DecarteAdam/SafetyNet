package com.safetynet.SafetyNet.Alert.System.util;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class WriteJsonFileTest {


    @Autowired
    ReadJsonFile readJsonFile;

    @Autowired
    WriteJsonFile writeJsonFile;

    /*@BeforeEach
    private void setUpPerTest() {
        try {
            FireStation fireStation = new FireStation();
            fireStation.setStation("1");
            fireStation.setAddress("5 rue Lauth");

            List<FireStation> fireStations = new ArrayList<>();
            fireStations.add(fireStation);

            DataModel dataModel = new DataModel();
            dataModel.setFirestations(fireStations);

            when(readJsonFile.readFile()).thenReturn(dataModel);


            fireStationService = new FireStationService(readJsonFile, writeJsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }*/

    @Test
    public void writePeron() throws IOException {
        Person person = new Person();
        person.setFirstName("Eric");
        person.setLastName("Cadigan");

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        Person addedPerson = this.writeJsonFile.writeFilePerson(personList);

        DataModel dataModel = this.readJsonFile.readFile();

        Assertions.assertEquals(addedPerson.getFirstName(), dataModel.getPersons().get(0).getFirstName());
        Assertions.assertEquals(addedPerson.getLastName(), dataModel.getPersons().get(0).getLastName());
    }

    @Test
    public void writeFireStation() throws IOException {
        FireStation fireStation = new FireStation();
        fireStation.setStation("3");
        fireStation.setAddress("1509 Culver St");

        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        FireStation addedFirestation = this.writeJsonFile.writeFileFireStation(fireStationList);

        DataModel dataModel = this.readJsonFile.readFile();

        Assertions.assertEquals(addedFirestation.getAddress(), dataModel.getFirestations().get(0).getAddress());
        Assertions.assertEquals(addedFirestation.getStation(), dataModel.getFirestations().get(0).getStation());
    }

    @Test
    public void writeMedicalRecords() throws IOException {
        MedicalRecords medicalRecords = new MedicalRecords();
        medicalRecords.setFirstName("Eric");
        medicalRecords.setLastName("Cadigan");

        List<MedicalRecords> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(medicalRecords);

        MedicalRecords addedMedicalRecord = this.writeJsonFile.writeFileMedicalRecords(medicalRecordsList);

        DataModel dataModel = this.readJsonFile.readFile();

        Assertions.assertEquals(addedMedicalRecord.getFirstName(), dataModel.getMedicalrecords().get(0).getFirstName());
        Assertions.assertEquals(addedMedicalRecord.getLastName(), dataModel.getMedicalrecords().get(0).getLastName());
    }

}
