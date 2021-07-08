package com.safetynet.SafetyNet.Alert.System.model;

import lombok.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecords {
    DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.FRANCE);

    private String firstName;
    private String lastName;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Date birthdate;
    private List<String> medications;
    private List<String> allergies;


    public Date getBirthdate() throws ParseException {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) throws ParseException {
        this.birthdate = formatter.parse(birthdate);
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
}
