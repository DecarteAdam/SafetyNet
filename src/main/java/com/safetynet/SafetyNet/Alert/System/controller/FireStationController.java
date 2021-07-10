package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.services.FireStationService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private final FireStationService fireStationService;

    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    /**
     * Return all fire stations from JSON
     * @return The fireStation object
     */
    @GetMapping("firestations")
    public List<FireStation> getFireStations() throws IOException {
        return this.fireStationService.getFireStation();
    }

    /**
     * Save the fire station into JSON
     * @param fireStation The fireStation object to save
     * @return The fireStation object
     */
    @PostMapping
    public FireStation saveFireStation(@RequestBody FireStation fireStation) throws IOException {
        return this.fireStationService.saveFireStation(fireStation);
    }

    /**
     * Update existing fire station into JSON
     * @param fireStation The fire Station object
     * @param station The fire station number
     * @param address The fire station address
     * @return The fire station
     */
    @PutMapping
    public FireStation updatePerson(@RequestBody FireStation fireStation,
                             @RequestParam("s") String station,
                             @RequestParam("a") String address) throws IOException {
        return this.fireStationService.updateFireStation(fireStation, station, address);
    }

    /**
     * Delete the fire station from JSON
     * @param station The fire station number
     * @param address The fire station address
     */
    @DeleteMapping
    public void deleteFireStation(@RequestParam("s") String station,
                                  @RequestParam("a") String address) throws IOException {
        this.fireStationService.deleteFireStation(station, address);
    }

    @GetMapping
    public Map<String, Object> getPersonsByStation(@RequestParam("stationNumber") String station) throws IOException {

        return this.fireStationService.getPersonsByStation(station);
    }
}
