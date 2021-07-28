package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.*;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.safetynet.SafetyNet.Alert.System.model.MedicalRecords.getAge;
import static java.util.stream.Collectors.groupingBy;

@Service
public class PersonService {

    private final ReadJsonFile readJsonFile;
    private final WriteJsonFile writeJsonFile;
    private final List<Object> persons = new ArrayList<>();

    /*Increments for duplicate names*/
    private final AtomicInteger counterForPersons = new AtomicInteger(1);
    private final AtomicInteger counterForMedical = new AtomicInteger(1);

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


        // Retrieve all persons from address
        Map<String, Person> personsList = readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForPersons.getAndIncrement(), Function.identity()));

        List<Person> personList = readJsonFile.readFile().getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(m -> personList.stream()
                        .anyMatch(p -> p.getFirstName().equals(m.getFirstName()) && p.getLastName().equals(m.getLastName())))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForMedical.getAndIncrement(), Function.identity()));


        // filter persons by by name in medical records
        personsList.forEach((key, person) -> {
            if (medicalRecordsList.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecordsList.get(key);
                if (getAge(medicalRecord.getBirthdate()) < 18) {
                    persons.add(Map.of(
                            "lastName", person.getLastName(),
                            "firstName", person.getFirstName(),
                            "age", getAge(medicalRecord.getBirthdate()),
                            "members", members(medicalRecordsList, personsList)
                    ));
                }

            }
        });

        return persons;
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
        Map<String, Person> personsList = readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForPersons.getAndIncrement(), Function.identity()));

        List<Person> personList = readJsonFile.readFile().getPersons().stream()
                .filter(p -> p.getAddress().equals(address))
                .collect(Collectors.toList());

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(m -> personList.stream()
                        .anyMatch(p -> p.getFirstName().equals(m.getFirstName()) && p.getLastName().equals(m.getLastName())))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForMedical.getAndIncrement(), Function.identity()));


        personsList.forEach((key, person) -> {
            if (medicalRecordsList.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecordsList.get(key);
                persons.add(Map.of(
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

        /* Retrieve firestation from given address*/
        List<FireStation> fireStations = readJsonFile.readFile().getFirestations().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toList());

        /* Add fire stations at the end of the list */
        persons.add(fireStations.stream().map(FireStation::getStation));

        return persons;
    }

    public Map<String,List<PersonWithMedicalRecords>> getPersonsFromFlood(List<String> stations) throws IOException {
        List<PersonWithMedicalRecords> list = new ArrayList<>();

        //find fireStations by station number
        List<FireStation> fireStations = readJsonFile.readFile().getFirestations().stream()
                .filter(s -> stations.contains(s.getStation()))
                .collect(Collectors.toList());


        /* Retrieve persons from given address */
        Map<String, Person> persons = readJsonFile.readFile().getPersons().stream()
                .filter(p -> fireStations.stream()
                        .anyMatch(ps -> ps.getAddress().equals(p.getAddress())))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForPersons.getAndIncrement(), Function.identity()));

        List<Person> personsList = readJsonFile.readFile().getPersons().stream()
                .filter(p -> fireStations.stream()
                        .anyMatch(ps -> ps.getAddress().equals(p.getAddress())))
                .collect(Collectors.toList());

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(m -> personsList.stream()
                        .anyMatch(p -> p.getFirstName().equals(m.getFirstName()) && p.getLastName().equals(m.getLastName())))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForMedical.getAndIncrement(), Function.identity()));



                persons.forEach((key, person) -> {
            if (medicalRecordsList.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecordsList.get(key);
                list.add(new PersonWithMedicalRecords(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getCity(),
                        person.getZip(),
                        person.getPhone(),
                        person.getEmail(),
                        getAge(medicalRecord.getBirthdate()),
                        medicalRecord.getBirthdate(),
                        medicalRecord.getMedications(),
                        medicalRecord.getAllergies()));
            }
        });

               return list.stream().collect(groupingBy(PersonWithMedicalRecords::getAddress));

    }

    public List<Object> personInfo(String firstName, String lastName)  throws IOException {
        /* Retrieve persons from given address */
        Map<String, Person> personMap = readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))
                .collect(Collectors.toMap(person -> person.getFirstName() + person.getLastName() + counterForPersons.getAndIncrement(), Function.identity()));

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsMap = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .filter(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName() + counterForMedical.getAndIncrement(), Function.identity()));


        personMap.forEach((key, person) -> {
            if (medicalRecordsMap.containsKey(key)) {
                MedicalRecords medicalRecord = medicalRecordsMap.get(key);
                persons.add(Map.of(
                        "lastName", person.getLastName(),
                        "firstName", person.getFirstName(),
                        "address", person.getAddress(),
                        "age", getAge(medicalRecord.getBirthdate()),
                        "email", person.getEmail(),
                        "medicalRecords", Map.of(
                                "medications", medicalRecord.getMedications(),
                                "allergies", medicalRecord.getAllergies()
                        )
                ));
            }
        });

        return persons;
    }


    public List<String> communityEmail(String city) throws IOException {

        /* Retrieve persons from given city */

        return readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getCity().equals(city))
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }
}
