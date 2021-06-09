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

    // CRUD
    @GetMapping
    public List<MedicalRecords> getMedicalRecord() throws IOException {

        return this.medicalRecordsService.getMedicalRecords();
    }

    @PostMapping
    public void saveMedicalRecord(@RequestBody MedicalRecords medicalRecords) throws IOException {

        this.medicalRecordsService.saveMedicalRecords(medicalRecords);
    }
    @PutMapping
    public void updatePerson(@RequestBody MedicalRecords medicalRecords,
                             @RequestParam("f") String firstName,
                             @RequestParam("l") String lastName) throws IOException {

        this.medicalRecordsService.updateMedicalRecords(medicalRecords, firstName, lastName);
    }

    @DeleteMapping
    public void deletePerson(@RequestParam("f") String firstName,
                             @RequestParam("l") String lastName) throws IOException {

        this.medicalRecordsService.deleteMedicalRecords(firstName, lastName);
    }
}
