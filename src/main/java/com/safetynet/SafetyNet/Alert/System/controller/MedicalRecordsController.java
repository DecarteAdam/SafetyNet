package com.safetynet.SafetyNet.Alert.System.controller;

import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.services.MedicalRecordsService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordsController {

    private final MedicalRecordsService medicalRecordsService;

    public MedicalRecordsController(MedicalRecordsService medicalRecordsService) {
        this.medicalRecordsService = medicalRecordsService;
    }

    /**
     * Return all medical records from JSON
     * @return List of medicalRecords
     */
    @GetMapping
    public List<MedicalRecords> getMedicalRecord() throws IOException {
        return this.medicalRecordsService.getMedicalRecords();
    }

    /**
     * Save medical record into JSON
     * @param medicalRecords The medicalRecord object to save
     */
    @PostMapping
    public MedicalRecords saveMedicalRecord(@RequestBody MedicalRecords medicalRecords) throws IOException {
        return this.medicalRecordsService.saveMedicalRecords(medicalRecords);
    }

    /**
     * Update medical record in JSON
     * @param medicalRecords The medical record object to update
     * @param firstName The person's first name as id
     * @param lastName The person's last name as id
     */
    @PutMapping
    public void updateMedicalRecord(@RequestBody MedicalRecords medicalRecords,
                             @RequestParam("f") String firstName,
                             @RequestParam("l") String lastName) throws IOException {
        this.medicalRecordsService.updateMedicalRecords(medicalRecords, firstName, lastName);
    }

    /**
     * Delete medical record from JSON
     * @param firstName The person's first name as id
     * @param lastName The person's last name as id
     */
    @DeleteMapping
    public void deleteMedicalRecord(@RequestParam("f") String firstName,
                             @RequestParam("l") String lastName) throws IOException {
        this.medicalRecordsService.deleteMedicalRecords(firstName, lastName);
    }
}
