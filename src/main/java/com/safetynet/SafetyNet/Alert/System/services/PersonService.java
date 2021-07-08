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

    public Person savePerson(Person person) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();
        dataModel.getPersons().add(person);
        this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        return person;
    }

    public Person updatePerson(Person person, String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))) {
            dataModel.getPersons().add(person);
            return this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }

        return null;


    }

    public List<Person> getPersons() throws IOException {
        return readJsonFile.readFile().getPersons();
    }

    public Person deletePerson(String firstName, String lastName) throws IOException {
        DataModel dataModel = this.readJsonFile.readFile();

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))) {
            return this.writeJsonFile.writeFilePerson(dataModel.getPersons());
        }

        return null;
    }

    /*public List<Person> getChildren(String address) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -18);
        Map<String, Object> persons = new HashMap<>();

        // Retrieve all persons from address
        List<Person> childrenList =  readJsonFile.readFile().getPersons().stream().filter(a -> a.getAddress().equals(address)).collect(Collectors.toList());

        // Retrieve medical records
        List<MedicalRecords> medicalrecords = readJsonFile.readFile().getMedicalrecords();


        // filter persons by by name in medical records
        List<MedicalRecords> medList = medicalrecords.stream().filter(f -> childrenList.stream()
                .map(Person::getLastName)
                .anyMatch(n ->n.equals(f.getLastName())))
                .collect(Collectors.toList());

        // Find Persons <= 18 y/o
        List<MedicalRecords> children = medList.stream().filter(b -> {
            try {
                return b.getBirthdate().after(Date.from(cal.toInstant()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());

        persons.put("Children", children);

        return persons;
    }*/

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
     * @param address
     * @return
     * @throws IOException
     */
    public List<Object> getPersonsFromFire(String address) throws IOException {
        /*Retrieve persons from given address*/
        Map<String, Person> personsList = readJsonFile.readFile().getPersons().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName(), Function.identity()));

        /*Retrive firestation from given address*/
        List<FireStation> fireStations = readJsonFile.readFile().getFirestations().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toList());

        /*Retrieve Medical records for persons*/
        Map<String, MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords()
                .stream()
                .collect(Collectors.toMap(o -> o.getFirstName() + o.getLastName(), Function.identity()));

        List<Object> list = new ArrayList<>();

        personsList.entrySet().forEach(o -> {
            if (medicalRecordsList.containsKey(o.getKey())) {
                MedicalRecords medicalRecord = medicalRecordsList.get(o.getKey());
                Person person = o.getValue();
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

        return list;
    }

        public static int getAge (LocalDate dateOfBirth){
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
    }
