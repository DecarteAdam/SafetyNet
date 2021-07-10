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

    /**
     * Return all medical records from JSON
     * @return The medicalRecords object
     */
    public List<MedicalRecords> getMedicalRecords() throws IOException {
        return readJsonFile.readFile().getMedicalrecords();
    }

    /**
     * Save medical record into JSON
     * @param medicalRecords The medicalRecords object to save
     * @return The medical record
     */
    public MedicalRecords saveMedicalRecords(MedicalRecords medicalRecords) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        if(dataModel.getMedicalrecords().contains(medicalRecords)){
            return null;
        }
        dataModel.getMedicalrecords().add(medicalRecords);
        this.writeJsonFile.writeFileMedicalRecords(dataModel.getMedicalrecords());
        return medicalRecords;
    }

    /**
     * Update the existing medical record in JSON
     * @param medicalRecords The medicalRecords object to update
     * @param firstname The persons first name used  as id
     * @param lastName The persons last name used as id
     */
    public void updateMedicalRecords(MedicalRecords medicalRecords, String firstname, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        dataModel.getMedicalrecords().removeIf(f -> f.getFirstName().equals(firstname) && f.getLastName().equals(lastName));

        dataModel.getMedicalrecords().add(medicalRecords);
        this.writeJsonFile.writeFileMedicalRecords(dataModel.getMedicalrecords());
    }


    /**
     * Delete medical record from JSON
     * @param firstName The person's first name used as id
     * @param lastName The person's last name used as id
     */
    public void deleteMedicalRecords(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        dataModel.getMedicalrecords().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName));

        this.writeJsonFile.writeFileMedicalRecords(dataModel.getMedicalrecords());
    }

}
