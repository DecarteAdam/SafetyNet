package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.services.FireStationService;
import org.hibernate.mapping.Any;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firestations")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    // CRUD
    @GetMapping
    public List<FireStation> getFireStations() throws IOException {

        return this.fireStationService.getFireStation();
    }

    @PostMapping
    public FireStation saveFireStation(@RequestBody FireStation fireStation) throws IOException {

        return this.fireStationService.saveFireStation(fireStation);
    }
    @PutMapping
    public FireStation updatePerson(@RequestBody FireStation fireStation,
                             @RequestParam("s") String station,
                             @RequestParam("a") String address) throws IOException {

        return this.fireStationService.updateFireStation(fireStation, station, address);
    }

    @DeleteMapping
    public FireStation deleteFireStation(@RequestParam("s") String station,
                                  @RequestParam("a") String address) throws IOException {

        return this.fireStationService.deleteFireStation(station, address);
    }

    @GetMapping("firestations")
    public Map<String, Object> getPersonsByStation(@RequestParam("stationNumber") String stationNumber) throws IOException {

        return this.fireStationService.getPersonsByStation(stationNumber);
    }
}
