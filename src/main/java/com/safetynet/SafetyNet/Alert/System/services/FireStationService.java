package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FireStationService {

    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;

    public FireStationService(ReadJsonFile readJsonFile, WriteJsonFile writeJsonFile) {
        this.readJsonFile = readJsonFile;
        this.writeJsonFile = writeJsonFile;
    }

    public FireStation saveFireStation(FireStation fireStation) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        dataModel.getFirestations().add(fireStation);
        return this.writeJsonFile.writeFileFireStation(dataModel.getFirestations());
    }

    public FireStation updateFireStation(FireStation fireStation, String station, String address) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        dataModel.getFirestations().removeIf(f -> f.getStation().equals(station) && f.getAddress().equals(address));

        dataModel.getFirestations().add(fireStation);
        return this.writeJsonFile.writeFileFireStation(dataModel.getFirestations());
    }

    public List<FireStation> getFireStation() throws IOException {
        return readJsonFile.readFile().getFirestations();
    }

    public FireStation deleteFireStation(String station, String address) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getFirestations().removeIf(f -> f.getStation().equals(station) && f.getAddress().equals(address))){
            return this.writeJsonFile.writeFileFireStation(dataModel.getFirestations());
        }

        return null;
    }
}
