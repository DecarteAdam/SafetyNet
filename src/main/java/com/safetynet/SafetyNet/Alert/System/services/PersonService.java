package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import com.safetynet.SafetyNet.Alert.System.model.FireStation;
import com.safetynet.SafetyNet.Alert.System.model.MedicalRecords;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
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

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))){
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

        if (dataModel.getPersons().removeIf(f -> f.getFirstName().equals(firstName) && f.getLastName().equals(lastName))){
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
        List<String> phoneNumbers =  readJsonFile.readFile().getPersons().stream().map(Person::getPhone).collect(Collectors.toList());
        /*Return duplicate filtered user phone numbers*/
        return phoneNumbers.stream().distinct().collect(Collectors.toList());
    }

    /**
     *
     * @param address
     * @return
     * @throws IOException
     */
    public List<Map<String, Object>> getPersonsFromFire(String address) throws IOException {
        /*Retrieve persons from given address*/
        List<Person> personsList = readJsonFile.readFile().getPersons().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toList());

        /*Retrive firestation from given address*/ // FIXME: 07/07/2021 One or multiple stations?
        List<FireStation> fireStations = readJsonFile.readFile().getFirestations().stream().filter(f -> f.getAddress().equals(address))
                .collect(Collectors.toList());

        /*Retrieve Medical records for persons*/
        List<MedicalRecords> medicalRecordsList = readJsonFile.readFile().getMedicalrecords().stream().filter(f -> personsList.stream()
                .map(Person::getLastName)
                .anyMatch(n -> n.equals(f.getLastName())))
                .collect(Collectors.toList());

        List<Map<String, Object>> persons =  personsList.stream()
                .flatMap(person -> medicalRecordsList.stream()
                        .map(medicalRecords ->  {
            try {
                return Map.of(
                        "lastName", person.getLastName(),
                        "firstName", person.getFirstName(),
                        "phoneNumber", person.getPhone(),
                        "age", getAge(medicalRecords.getBirthdate()),
                        "medicalRecords", Map.of(
                                "medications", medicalRecords.getMedications(),
                                "allergies", medicalRecords.getAllergies()
                        )
                );
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        })).distinct().collect(Collectors.toList());



        return persons;
    }

    public static int getAge(Date dateOfBirth) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("You don't exist yet");
        }
        int todayYear = today.get(Calendar.YEAR);
        int birthDateYear = birthDate.get(Calendar.YEAR);
        int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int birthDateDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int birthDateMonth = birthDate.get(Calendar.MONTH);
        int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        int birthDateDayOfMonth = birthDate.get(Calendar.DAY_OF_MONTH);
        int age = todayYear - birthDateYear;

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ((birthDateDayOfYear - todayDayOfYear > 3) || (birthDateMonth > todayMonth)){
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        } else if ((birthDateMonth == todayMonth) && (birthDateDayOfMonth > todayDayOfMonth)){
            age--;
        }
        return age;
    }
}
