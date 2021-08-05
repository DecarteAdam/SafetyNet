package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.FireStation;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class FireStationServiceTest {

    @InjectMocks
    FireStationService service;
    @Autowired
    ReadJsonFile readJsonFile;
    @Autowired
    WriteJsonFile writeJsonFile;

    @BeforeEach
    private void setUpPerTest() {
        try {
            service = new FireStationService(readJsonFile, writeJsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }



    @Test
    @DisplayName("Should return NULL because fire station already exist in JSON file")
    public void saveIfExist() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("3");
        fireStation.setAddress("1509 Culver St");

        FireStation actual = service.saveFireStation(fireStation);

        Assertions.assertNull(actual);
    }

    @Test
    @DisplayName("Should return Fire station")
    public void saveFireStation() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("2");
        fireStation.setAddress("6a rue Kruger");

        FireStation actual = service.saveFireStation(fireStation);

        FireStation added = this.readJsonFile.readFile().getFirestations().get(this.readJsonFile.readFile().getFirestations().size() -1);

        Assertions.assertEquals(added, actual);

        /* Delete*/
        this.service.deleteFireStation(fireStation.getStation(), fireStation.getAddress());
    }

    @Test
    @DisplayName("Should return updated person")
    public void updateFireStation() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("2");
        fireStation.setAddress("1509 Culver St");

        FireStation fireStationPrev = new FireStation();
        fireStationPrev.setStation("3");
        fireStationPrev.setAddress("1509 Culver St");


        FireStation actual = service.updateFireStation(fireStation, fireStationPrev.getStation(), fireStationPrev.getAddress());

        List<FireStation> expected = this.readJsonFile.readFile().getFirestations()
                .stream()
                .filter(f -> f.getStation().equals(fireStation.getStation())
                        &&
                        f.getAddress().equals(fireStation.getAddress()))
                .collect(Collectors.toList());

        Assertions.assertEquals(expected.get(0), actual);

        service.updateFireStation(fireStationPrev, fireStation.getStation(), fireStation.getAddress());
    }

    @Test
    @DisplayName("Should return null because firestation doesn't exist")
    public void ShouldReturnEmpty() throws IOException {
        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        FireStation actualFireStation = service.updateFireStation(fireStation, "2", "D");

        Assertions.assertNull(actualFireStation);
    }

    @Test
    @DisplayName("Should return empty list because fire station was deleted")
    public void deleteFireStationShouldReturnNull() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");

        List<FireStation> fireStationList = new ArrayList<>();
        fireStationList.add(fireStation);

        service.saveFireStation(fireStation);

        List<FireStation> exist = this.readJsonFile.readFile().getFirestations()
                .stream()
                .filter(f -> f.getStation().equals(fireStation.getStation())
                        &&
                        f.getAddress().equals(fireStation.getAddress()))
                .collect(Collectors.toList());

        Assertions.assertEquals(fireStationList.get(0), exist.get(0));

        service.deleteFireStation(fireStation.getStation(), fireStation.getAddress());

        List<FireStation> actual = this.readJsonFile.readFile().getFirestations()
                .stream()
                .filter(f -> f.getStation().equals(fireStation.getStation())
                        &&
                        f.getAddress().equals(fireStation.getAddress()))
                .collect(Collectors.toList());

        Assertions.assertEquals(0,  actual.size());
    }

    @Test
    @DisplayName("Should return null because fire station doesn't exist")
    public void deleteFireStationShouldMatchNull() throws IOException {

        service.deleteFireStation("8", "some address");

        List<FireStation> actual = this.readJsonFile.readFile().getFirestations()
                .stream()
                .filter(f -> f.getStation().equals("8")
                        &&
                        f.getAddress().equals("some address"))
                .collect(Collectors.toList());

        Assertions.assertEquals(0,  actual.size());
    }

    @Test
    @DisplayName("Should return list of all fire stations")
    public void shouldReturnListOfFireStations() throws IOException {

        List<FireStation> actual = service.getFireStation();

        List<FireStation> expected = this.readJsonFile.readFile().getFirestations();

        Assertions.assertEquals(expected.size(), actual.size());
    }

    @Test
    @DisplayName("Should return a list of all persons living at the address covered by the given fire station")
    public void getPersonsByStation() throws IOException {
        Map<String,Object> actual = service.getPersonsByStation("2");

        Assertions.assertEquals(3, actual.size());
    }
}
