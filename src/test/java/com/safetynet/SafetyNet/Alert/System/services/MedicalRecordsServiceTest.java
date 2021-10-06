package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MedicalRecordsServiceTest {

    @InjectMocks
     MedicalRecordsService service;
    @Autowired
     ReadJsonFile readJsonFile;
    @Autowired
     WriteJsonFile writeJsonFile;


    @BeforeEach
    private void setUpPerTest() {
        try {
            service = new MedicalRecordsService(readJsonFile, writeJsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }



    @Test
    @DisplayName("Should return medical record")
    public void saveMedicalRecord() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("Test");
        medicalRecord.setLastName("Tester");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        MedicalRecords actual = service.saveMedicalRecords(medicalRecord);

        MedicalRecords addedMR = this.readJsonFile.readFile().getMedicalrecords().get(this.readJsonFile.readFile().getMedicalrecords().size() -1);

        Assertions.assertEquals(addedMR.getFirstName(), actual.getFirstName());
        Assertions.assertEquals(addedMR.getLastName(), actual.getLastName());

        /* Delete*/
        this.service.deleteMedicalRecords(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    //@Test
    @DisplayName("Should return NULL because medical record already exist in JSON file")
    public void saveMedicalRecordIfExist() throws IOException {

        List<String> medications = new ArrayList<>();
        medications.add("aznol:350mg");
        medications.add("hydrapermazol:100mg");

        List<String> allergies = new ArrayList<>();
        allergies.add("nillacilan");

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);

        MedicalRecords actual = service.saveMedicalRecords(medicalRecord);

        Assertions.assertNull(actual);
    }

    //@Test
    @DisplayName("Should return updated person")
    public void updateMedical() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg", "doliprane"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        MedicalRecords medicalPrev = new MedicalRecords();
        medicalPrev.setFirstName("John");
        medicalPrev.setLastName("Boyd");
        medicalPrev.setBirthdate("03/06/1984");
        medicalPrev.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg"));
        medicalPrev.setAllergies(List.of("nillacilan"));

        MedicalRecords actual = service.updateMedicalRecords(medicalRecord, medicalRecord.getFirstName(), medicalRecord.getLastName());

        List<MedicalRecords> expected = this.readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(f -> f.getFirstName().equals(medicalRecord.getFirstName())
                        &&
                        f.getLastName().equals(medicalRecord.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(expected.get(0), actual);

        service.updateMedicalRecords(medicalPrev, medicalPrev.getFirstName(), medicalPrev.getLastName());
    }


    //@Test
    @DisplayName("Should return empty if the medical record doesn't exist")
    public void ShouldReturnEmpty() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg", "doliprane"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        MedicalRecords actual = service.updateMedicalRecords(medicalRecord, "Théo", "D");

        Assertions.assertNull(actual);
    }


    //@Test
    @DisplayName("Should return empty because person was deleted")
    public void deletePersonShouldReturnNull() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg", "doliprane"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        List<MedicalRecords> personList = new ArrayList<>();
        personList.add(medicalRecord);

        service.saveMedicalRecords(medicalRecord);

        List<MedicalRecords> exist = this.readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(f -> f.getFirstName().equals(medicalRecord.getFirstName())
                        &&
                        f.getLastName().equals(medicalRecord.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(personList.get(0), exist.get(0));

        service.deleteMedicalRecords(medicalRecord.getFirstName(), medicalRecord.getLastName());

        List<MedicalRecords> actual = this.readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(f -> f.getFirstName().equals(medicalRecord.getFirstName())
                        &&
                        f.getLastName().equals(medicalRecord.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(0,  actual.size());
    }

    //@Test
    @DisplayName("Should return null because person doesn't exist")
    public void deletePersonShouldMatchNull() throws IOException {

        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setFirstName("John");
        medicalRecord.setLastName("Boyd");
        medicalRecord.setBirthdate("03/06/1984");
        medicalRecord.setMedications(List.of("aznol:350mg", "hydrapermazol:100mg", "doliprane"));
        medicalRecord.setAllergies(List.of("nillacilan"));

        /* Ensure that person doesn't exist */
        List<MedicalRecords> exist = this.readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(f -> f.getFirstName().equals(medicalRecord.getFirstName())
                        &&
                        f.getLastName().equals(medicalRecord.getLastName()))
                .collect(Collectors.toList());

        /* Should be 0 because person doesn't exist */
        Assertions.assertEquals(0, exist.size());

        /* Execute delete method */
        service.deleteMedicalRecords("Théo", "D");

        List<MedicalRecords> actual = this.readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(f -> f.getFirstName().equals(medicalRecord.getFirstName())
                        &&
                        f.getLastName().equals(medicalRecord.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(0,  actual.size());
    }

    @Test
    @DisplayName("Get all Medical records from JSON")
    public void getMedicalRecords() throws IOException {

        List<MedicalRecords> actual = this.service.getMedicalRecords();

        List<MedicalRecords> expected = this.readJsonFile.readFile().getMedicalrecords();

        Assertions.assertEquals(expected.size(), actual.size());
    }
}
