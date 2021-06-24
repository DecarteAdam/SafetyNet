package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.services.FireStationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/firestations")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    // CRUD
    @GetMapping("firestations")
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
    public List<FireStation> getPersonsByStation(@RequestParam("stationNumber") String station) throws IOException {

        return this.fireStationService.getFireStation();
    }
}
