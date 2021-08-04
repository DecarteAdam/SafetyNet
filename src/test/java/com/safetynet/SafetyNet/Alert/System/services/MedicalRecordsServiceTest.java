package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class MedicalRecordsServiceTest {

    @InjectMocks
     MedicalRecordsService medicalRecordsService;
    @Mock
     ReadJsonFile readJsonFile;
    @Mock
     WriteJsonFile writeJsonFile;

    @BeforeEach
    private void setUpPerTest() {
        try {
            MedicalRecords medicalRecord = new MedicalRecords();
            medicalRecord.setFirstName("Adam");
            medicalRecord.setLastName("Decarte");
            medicalRecord.setBirthdate("08/28/1992");

            List<MedicalRecords> medicalRecordsList = new ArrayList<>();
            medicalRecordsList.add(medicalRecord);

            DataModel dataModel = new DataModel();
            dataModel.setMedicalrecords(medicalRecordsList);

            when(readJsonFile.readFile()).thenReturn(dataModel);


            medicalRecordsService = new MedicalRecordsService(readJsonFile, writeJsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }



    @Test
    public void shouldReturnMedicalRecords() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("Adam");
        medicalRecord.setLastName("Decarte");
        medicalRecord.setBirthdate("08/28/1992");

        List<MedicalRecords> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(medicalRecord);

        when(writeJsonFile.writeFileMedicalRecords(medicalRecordsList)).thenReturn(medicalRecord);
        MedicalRecords actual = medicalRecordsService.saveMedicalRecords(medicalRecord);

        Assertions.assertEquals(medicalRecord, actual);
    }

    /*@Test
    public void saveMedicalRecordIfExist() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("Eric");
        medicalRecord.setLastName("Cadigan");
        medicalRecord.setBirthdate("03/06/1994");
        medicalRecord.setMedications(new ArrayList<>());
        medicalRecord.setAllergies(new ArrayList<>());


        List<MedicalRecords> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(medicalRecord);

        when(writeJsonFile.writeFileMedicalRecords(medicalRecordsList)).thenReturn(medicalRecord);
        MedicalRecords actual = medicalRecordsService.saveMedicalRecords(medicalRecord);

        Assertions.assertEquals(medicalRecord, actual);
    }*/

    @Test
    public void updatePerson() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("Adam");
        medicalRecord.setLastName("Decarte");
        medicalRecord.setBirthdate("08/28/1992");

        List<MedicalRecords> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(medicalRecord);

        when(writeJsonFile.writeFileMedicalRecords(medicalRecordsList)).thenReturn(medicalRecord);

        MedicalRecords actual = medicalRecordsService.updateMedicalRecords(medicalRecord, medicalRecord.getFirstName(), medicalRecord.getLastName());

        Assertions.assertEquals(medicalRecord, actual);
    }


    public void ShouldReturnEmpty() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("Adam");
        medicalRecord.setLastName("Decarte");
        medicalRecord.setBirthdate("08/28/1992");

        List<MedicalRecords> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(medicalRecord);

        MedicalRecords actual = medicalRecordsService.updateMedicalRecords(medicalRecord, "Adam", "D");

        Assertions.assertNull(actual);
    }


   /* @Test
    @DisplayName("Delete Medical record")
    public void delete() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("Clive");
        medicalRecord.setLastName("Ferguson");
        medicalRecord.setBirthdate("08/28/1992");

        List<MedicalRecords> medicalRecordsList = new ArrayList<>();
        medicalRecordsList.add(medicalRecord);

        when(writeJsonFile.writeFileMedicalRecords(medicalRecordsList)).thenReturn(medicalRecord);
        medicalRecordsService.deleteMedicalRecords(medicalRecord.getFirstName(), medicalRecord.getLastName());

        Assertions.assertEquals(medicalRecordsList.size(),  0);
    }*/

    @Test
    @DisplayName("Get all Medical records from JSON")
    public void getMedicalRecords() throws IOException {

        List<MedicalRecords> medicalRecords = this.medicalRecordsService.getMedicalRecords();

        Assertions.assertEquals(medicalRecords.size(), 1);
    }

    /*@Test
    public void shouldReturnListOfPersons() throws IOException {

        Person person = new Person();
        person.setFirstName("Adam");
        person.setLastName("Decarte");
        person.setAddress("5 rue Lauth");
        person.setZip("67000");
        person.setCity("Strasbourg");
        person.setEmail("test@gmail.com");
        person.setPhone("+6 00 00 00 00");

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        when(writeJsonFile.writeFilePerson(personList)).thenReturn(person);
        List<Person> actualPerson = personService.getPersons();

        Assertions.assertEquals(1, actualPerson.size());
    }*/
}
