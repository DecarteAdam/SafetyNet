package com.safetynet.SafetyNet.Alert.System.model;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataModel {
    List<Person> persons;
    List<FireStation> firestations;
    List<MedicalRecords> medicalrecords;
}
