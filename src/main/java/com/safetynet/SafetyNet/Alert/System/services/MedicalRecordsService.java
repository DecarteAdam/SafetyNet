package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MedicalRecordsService {

    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;

    public MedicalRecordsService(ReadJsonFile readJsonFile, WriteJsonFile writeJsonFile) {
        this.readJsonFile = readJsonFile;
        this.writeJsonFile = writeJsonFile;
    }


    public void saveMedicalRecords(MedicalRecords medicalRecords) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        dataModel.getMedicalrecords().add(medicalRecords);
        this.writeJsonFile.writeFileMedicalRecords(dataModel.getMedicalrecords());
    }

    public void updateMedicalRecords(MedicalRecords medicalRecords, String firstname, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        dataModel.getMedicalrecords().removeIf(f -> f.getFirstName().equals(firstname) && f.getLastName().equals(lastName));

        dataModel.getMedicalrecords().add(medicalRecords);
        this.writeJsonFile.writeFileMedicalRecords(dataModel.getMedicalrecords());
    }

    public List<MedicalRecords> getMedicalRecords() throws IOException {
        return readJsonFile.readFile().getMedicalrecords();
    }

    public void deleteMedicalRecords(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        dataModel.getMedicalrecords().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName));

        this.writeJsonFile.writeFileMedicalRecords(dataModel.getMedicalrecords());
    }

}
