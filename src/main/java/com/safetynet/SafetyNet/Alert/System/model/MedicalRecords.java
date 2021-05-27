package com.safetynet.SafetyNet.Alert.System.model;

import lombok.Data;

import java.util.List;
@Data
public class MedicalRecords {
    private String firstname;
    private String lastName;
    private String birthDate;
    private List<String> medications;
    private List<String> allergies;
}
