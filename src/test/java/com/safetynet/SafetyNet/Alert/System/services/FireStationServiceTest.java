package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
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
import java.util.Map;

import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class FireStationServiceTest {

    @InjectMocks
    FireStationService fireStationService;
    @Mock
    ReadJsonFile readJsonFile;
    @Mock
    WriteJsonFile writeJsonFile;

    @BeforeEach
    private void setUpPerTest() {
        try {
            FireStation fireStation = new FireStation();
            fireStation.setStation("1");
            fireStation.setAddress("5 rue Lauth");

            List<FireStation> fireStations = new ArrayList<>();
            fireStations.add(fireStation);

            Person person = new Person();
            person.setFirstName("Adam");
            person.setLastName("Decarte");
            person.setAddress("5 rue Lauth");
            person.setZip("67000");
            person.setCity("Strasbourg");
            person.setEmail("test@gmail.com");
            person.setPhone("+6 00 00 00 00");

            List<Person> persons = new ArrayList<>();
            persons.add(person);

            MedicalRecords medicalRecord = new MedicalRecords();
            medicalRecord.setFirstName("Adam");
            medicalRecord.setLastName("Decarte");
            medicalRecord.setBirthdate("08/28/1992");

            List<MedicalRecords> medicalRecordsList = new ArrayList<>();
            medicalRecordsList.add(medicalRecord);

            DataModel dataModel = new DataModel();
            dataModel.setFirestations(fireStations);
            dataModel.setPersons(persons);
            dataModel.setMedicalrecords(medicalRecordsList);

            when(readJsonFile.readFile()).thenReturn(dataModel);


            fireStationService = new FireStationService(readJsonFile, writeJsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }



    @Test
    @DisplayName("Should return NULL because fire station already exist in JSON file")
    public void saveIfExist() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);

        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);

        FireStation actualFireStation = fireStationService.saveFireStation(fireStation);

        Assertions.assertNull(actualFireStation);
    }

    @Test
    @DisplayName("Should return Fire station")
    public void saveFireStation() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("2");
        fireStation.setAddress("6a rue Kruger");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);

        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);

        FireStation actualFireStation = fireStationService.saveFireStation(fireStation);

        Assertions.assertEquals(fireStation, actualFireStation);
    }

    @Test
    public void updateFireStation() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);


        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);

        FireStation actualFireStation = fireStationService.updateFireStation(fireStation, fireStation.getStation(), fireStation.getAddress());

        Assertions.assertEquals(fireStation, actualFireStation);
    }

    @Test
    public void ShouldReturnEmpty() throws IOException {
        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");


        FireStation actualFireStation = fireStationService.updateFireStation(fireStation, "2", "D");

        Assertions.assertNull(actualFireStation);
    }

    @Test
    public void deleteFireStationShouldReturnNull() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);

        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);
        FireStation actualFireStation = fireStationService.deleteFireStation(fireStation.getStation(), fireStation.getAddress());

        Assertions.assertNull(actualFireStation);
    }

    @Test
    public void deleteFireStationShouldMatchNull() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);

        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);
        FireStation actualFireStation = fireStationService.deleteFireStation("9", "D");

        Assertions.assertNull(actualFireStation);
    }

    @Test
    public void shouldReturnListOfPersons() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);


        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);
        List<FireStation> actualFireStation = fireStationService.getFireStation();

        Assertions.assertEquals(1, actualFireStation.size());
    }

    @Test
    public void getPersonByStation() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStations = new ArrayList<>();
        fireStations.add(fireStation);

        when(writeJsonFile.writeFileFireStation(fireStations)).thenReturn(fireStation);
        Map<String, Object> actualFireStation = fireStationService.getPersonsByStation(fireStation.getStation());

        Assertions.assertEquals(3, actualFireStation.size());
    }
}
