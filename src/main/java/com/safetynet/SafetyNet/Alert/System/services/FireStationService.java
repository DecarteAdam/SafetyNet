package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FireStationService {

    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;

    public FireStationService(ReadJsonFile readJsonFile, WriteJsonFile writeJsonFile) {
        this.readJsonFile = readJsonFile;
        this.writeJsonFile = writeJsonFile;
    }

    /**
     * Save a new fire station
     * @param fireStation The fire station to save
     * @return Saved fire station
     */
    public FireStation saveFireStation(FireStation fireStation) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        if(dataModel.getFirestations().contains(fireStation)){
            return null;
        }
        dataModel.getFirestations().add(fireStation);
        this.writeJsonFile.writeFileFireStation(dataModel.getFirestations());
        return fireStation;
    }

    /**
     * Update existing fire station
     * @param fireStation The fire station to update
     * @param station The station number
     * @param address The station address
     * @return The updated station
     */
    public FireStation updateFireStation(FireStation fireStation, String station, String address) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getFirestations().removeIf(f -> f.getStation().equals(station) && f.getAddress().equals(address))) {
            dataModel.getFirestations().add(fireStation);
            return this.writeJsonFile.writeFileFireStation(dataModel.getFirestations());
        }
        return null;
    }

    /**
     * Get all fire stations from JSON file
     * @return The list of all fire stations
     */
    public List<FireStation> getFireStation() throws IOException {
        return readJsonFile.readFile().getFirestations();
    }

    /**
     * Delete Fire station from JSON file
     * @param station The station number
     * @param address The station address
     * @return If the fire station deleted return fire station else return null
     */
    public FireStation deleteFireStation(String station, String address) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getFirestations().removeIf(f -> f.getStation().equals(station) && f.getAddress().equals(address))) {
            return this.writeJsonFile.writeFileFireStation(dataModel.getFirestations());
        }

        return null;
    }


    /**
     * Get persons covered by given fire station
     * @param stationNumber The fire station number
     * @return The map of persons
     */
    public Map<String, Object> getPersonsByStation(String stationNumber) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);

        Map<String, Object> persons = new HashMap<>();

        //find fireStations by station number
        List<FireStation> fireStationList = readJsonFile.readFile().getFirestations()
                .stream().filter(f -> f.getStation().equals(stationNumber))
                .collect(Collectors.toList());

        // retrieve all persons
        List<Person> personList = readJsonFile.readFile().getPersons();

        List<MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords();

        // filter persons by firestation address
        List<Person> listOutput =
                personList.stream()
                        .filter(e -> fireStationList.stream().map(FireStation::getAddress)
                                .anyMatch(name -> name.equals(e.getAddress())))
                        .collect(Collectors.toList());

        // filter persons by birthdate
        List<MedicalRecords> medList = medicalRecordsList.stream().filter(f -> listOutput.stream()
                .map(Person::getFirstName)
                .anyMatch(n ->n.equals(f.getFirstName())))
                .collect(Collectors.toList());
        // count children
        long children = medList.stream().filter(b -> MedicalRecords.getAge(b.getBirthdate()) < 19).count();

        // count adults
        long adult = medList.stream().filter(b -> MedicalRecords.getAge(b.getBirthdate()) > 18).count();

        persons.put("Persons", listOutput);
        persons.put("Children", Collections.singletonList(children));
        persons.put("Adult", Collections.singletonList(adult));

        return persons;
    }

}
