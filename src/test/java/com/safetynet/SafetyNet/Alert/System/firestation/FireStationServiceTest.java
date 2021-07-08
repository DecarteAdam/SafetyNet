package com.safetynet.SafetyNet.Alert.System.firestation;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.services.FireStationService;
import com.safetynet.SafetyNet.Alert.System.services.PersonService;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
public class FireStationServiceTest {

    @InjectMocks
    FireStationService fireStationService;
    @Mock
    ReadJsonFile readJsonFile;
    @Mock
    WriteJsonFile writeJsonFile;
    @Mock
    PersonService personService;

    @BeforeEach
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


            fireStationService = new FireStationService(readJsonFile, writeJsonFile, personService);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }



    public void shouldReturnFireStation() throws IOException {

        FireStation fireStation = new FireStation();
        fireStation.setStation("1");
        fireStation.setAddress("5 rue Lauth");


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
}
