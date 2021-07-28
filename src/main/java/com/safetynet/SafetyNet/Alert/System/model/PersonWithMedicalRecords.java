package com.safetynet.SafetyNet.Alert.System.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonWithMedicalRecords {
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
    private int age;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;



}
