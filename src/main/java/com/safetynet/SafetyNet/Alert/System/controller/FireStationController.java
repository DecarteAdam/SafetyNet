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
    public void saveFireStation(@RequestBody FireStation fireStation) throws IOException {

        this.fireStationService.saveFireStation(fireStation);
    }
    @PutMapping
    public void updatePerson(@RequestBody FireStation fireStation,
                             @RequestParam("f") String firstName,
                             @RequestParam("l") String lastName) throws IOException {

        this.fireStationService.updateFireStation(fireStation, firstName, lastName);
    }

    @DeleteMapping
    public void deleteFireStation(@RequestParam("f") String firstName,
                                  @RequestParam("l") String lastName) throws IOException {

        this.fireStationService.deleteFireStation(firstName, lastName);
    }
}
