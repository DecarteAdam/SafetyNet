package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.safetynet.SafetyNet.Alert.System.model.MedicalRecords.getAge;

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
     *
     * @return The person
     */
    public List<Person> getPersons() throws IOException {
        return readJsonFile.readFile().getPersons();
    }

    /**
     * Save person into JSON
     *
     * @param person The object to save
     * @return person object if saved.
     * return null if person already exist
     */
    public Person savePerson(Person person) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        if (dataModel.getPersons().contains(person)) {
            return null;
        }
        dataModel.getPersons().add(person);
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        return person;
    }

    /**
     * Update existing person
     *
     * @param person    The person to update
     * @param firstName The person's first name (used as id)
     * @param lastName  The person's last name (used as id)
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
     *
     * @param firstName The person's first name (used as id)
     * @param lastName  The person's last name (used as id)
     */
    public void deletePerson(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))) {
            this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }

    }

    /**
     * Return children with other family members
     *
     * @param address The child address
     * @return List of children with their members
     */
    public List<Object> getChildren(String address) throws IOException {
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
                if (getAge(medicalRecord.getBirthdate()) < 18) {
                    list.add(Map.of(
                            "lastName", person.getLastName(),
                            "firstName", person.getFirstName(),
                            "age", getAge(medicalRecord.getBirthdate()),
                            "members", members(medicalRecordsList, personsList)
                    ));
                }

            }
        });

        return list;
    }

    /**
     * Resolve members of the children
     *
     * @param medicalRecords The medical records of the family
     * @param persons        The members of the family
     * @return List of adult members of the family
     */
    public List<Object> members(Map<String, MedicalRecords> medicalRecords, Map<String, Person> persons) {
        List<Object> members = new ArrayList<>();

        persons.forEach((key, person) -> {
            if (medicalRecords.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecords.get(key);
                if (getAge(medicalRecord.getBirthdate()) > 18) {
                    members.add(Map.of(
                            "lastName", person.getLastName(),
                            "firstName", person.getFirstName(),
                            "age", getAge(medicalRecord.getBirthdate())
                    ));
                }

            }
        });
        return members;
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

        List<Person> persons = readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> fireStationList.stream()
                        .map(FireStation::getAddress)
                        .anyMatch(a -> a.equals(f.getAddress()))).collect(Collectors.toList());
        /*Get user phone numbers*/
        List<String> phoneNumbers = persons.stream().map(Person::getPhone).collect(Collectors.toList());
        /*Return duplicate filtered user phone numbers*/
        return phoneNumbers.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Return persons with medical records from given address and fire station numbers
     *
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
}
