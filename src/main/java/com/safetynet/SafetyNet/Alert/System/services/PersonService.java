package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;

    public PersonService(ReadJsonFile readJsonFile, WriteJsonFile writeJsonFile) {
        this.readJsonFile = readJsonFile;
        this.writeJsonFile = writeJsonFile;
    }

    /**
     * Return all persons from JSON
     * @return The person
     */
    public List<Person> getPersons() throws IOException {
        return readJsonFile.readFile().getPersons();
    }

    /**
     * Save person into JSON
     * @param person The object to save
     * @return person object if saved.
     * return null if person already exist
     */
    public Person savePerson(Person person) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        if(dataModel.getPersons().contains(person)){
            return null;
        }
        dataModel.getPersons().add(person);
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        return person;
    }

    /**
     * Update existing person
     * @param person The person to update
     * @param firstName The person's first name (used as id)
     * @param lastName The person's last name (used as id)
     * @return Person
     */
    public Person updatePerson(Person person, String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))) {
            dataModel.getPersons().add(person);
            return this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }
        return null;
    }

    /**
     * Delete person
     * @param firstName The person's first name (used as id)
     * @param lastName The person's last name (used as id)
     */
    public void deletePerson(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))) {
             this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }

    }

    public List<Object> getChildren(String address) throws IOException {
       /* Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);*/
        List<Object> list = new ArrayList<>();

        // Retrieve all persons from address
        Map<String, Person> personsList = readJsonFile.readFile().getPersons().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName(), Function.identity()));

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName(), Function.identity()));


        // filter persons by by name in medical records
        personsList.forEach((key, person) -> {
            if (medicalRecordsList.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecordsList.get(key);
                list.add(Map.of(
                        "lastName", person.getLastName(),
                        "firstName", person.getFirstName(),
                        "age", getAge(medicalRecord.getBirthdate()),
                        "members", Map.of(
                                "medications", medicalRecord.getMedications(),
                                "allergies", medicalRecord.getAllergies()
                        )
                ));
            }
        });

        // Find Persons <= 18 y/o
       /* List<MedicalRecords> children = medList.stream().filter(b -> {
            try {
                return b.getBirthdate().after(Date.from(cal.toInstant()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());

        persons.put("Children", children);*/

        return list;
    }

    /**
     * Return a list of phone numbers of persons covered by fire station
     * @param station The fire station number
     * @return The list of phone numbers
     */
    public List<String> getPhones(String station) throws IOException {
        //find fireStations by station number
        List<FireStation> fireStationList = readJsonFile.readFile().getFirestations()
                .stream().filter(f -> f.getStation().equals(station))
                .collect(Collectors.toList());
        /*Get user phone numbers*/
        List<String> phoneNumbers = readJsonFile.readFile().getPersons().stream().map(Person::getPhone).collect(Collectors.toList());
        /*Return duplicate filtered user phone numbers*/
        return phoneNumbers.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Return persons with medical records from given address and fire station numbers
     * @param address The address of the persons
     * @return list of persons
     */
    public List<Object> getPersonsFromFire(String address) throws IOException {
        /* Retrieve persons from given address */
        Map<String, Person> personsList = readJsonFile.readFile().getPersons().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName(), Function.identity()));

        /* Retrieve firestation from given address*/
        List<FireStation> fireStations = readJsonFile.readFile().getFirestations().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toList());

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName(), Function.identity()));

        List<Object> list = new ArrayList<>();

        personsList.forEach((key, person) -> {
            if (medicalRecordsList.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecordsList.get(key);
                list.add(Map.of(
                        "lastName", person.getLastName(),
                        "firstName", person.getFirstName(),
                        "phoneNumber", person.getPhone(),
                        "age", getAge(medicalRecord.getBirthdate()),
                        "medicalRecords", Map.of(
                                "medications", medicalRecord.getMedications(),
                                "allergies", medicalRecord.getAllergies()
                        )
                ));
            }
        });

        /* Add fire stations at the end of the list */
        list.add(fireStations.stream().map(FireStation::getStation));

        return list;
    }

        public static int getAge (String dateOfBirth){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
        LocalDate localDate = LocalDate.parse(dateOfBirth, formatter);
            return Period.between(localDate, LocalDate.now()).getYears();
        }
    }
