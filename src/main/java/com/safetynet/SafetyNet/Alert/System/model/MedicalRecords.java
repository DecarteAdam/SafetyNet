package com.safetynet.SafetyNet.Alert.System.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class MedicalRecords {


    private String firstName;
    private String lastName;

    private String birthdate;
    private List<String> medications;
    private List<String> allergies;


    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) {
       /* DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");*/
        this.birthdate = birthdate; /*LocalDate.parse(birthdate, formatter);*/
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getMedications() {
        return medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMedications(List<String> medications) {
        this.medications = medications;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public static int getAge (String dateOfBirth){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
        LocalDate localDate = LocalDate.parse(dateOfBirth, formatter);
        return Period.between(localDate, LocalDate.now()).getYears();
    }
}
