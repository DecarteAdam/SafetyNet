package com.safetynet.SafetyNet.Alert.System.services;

import com.safetynet.SafetyNet.Alert.System.model.Person;
import com.safetynet.SafetyNet.Alert.System.model.PersonWithMedicalRecords;
import com.safetynet.SafetyNet.Alert.System.util.ReadJsonFile;
import com.safetynet.SafetyNet.Alert.System.util.WriteJsonFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class PersonServiceTest {

    @InjectMocks
    PersonService personService;

    @Autowired
    ReadJsonFile readJsonFile;

    @Autowired
    WriteJsonFile writeJsonFile;

    File testWrite = new File("src/main/resources/data-test.json");
    File testRead = new File("src/main/resources/data-test.json");

    @BeforeEach
    private void setUpPerTest() {
        try {
            personService = new PersonService(readJsonFile, writeJsonFile);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }



    @Test
    @DisplayName("Should return NULL because person already exist in JSON file")
    public void saveIfExist() throws IOException {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setZip("97451");
        person.setCity("Culver");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");

        Person actualPerson = personService.savePerson(person);

        Assertions.assertNull(actualPerson);

    }

    @Test
    @DisplayName("Should return Person")
    public void savePerson() throws IOException {

        Person person = new Person();
        person.setFirstName("Tester");
        person.setLastName("Developer");
        person.setAddress("1509 Culver St");
        person.setZip("97451");
        person.setCity("Culver");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");

        Person actualPerson = personService.savePerson(person);

        Person addedPerson = this.readJsonFile.readFile().getPersons().get(this.readJsonFile.readFile().getPersons().size() -1);

        Assertions.assertEquals(addedPerson, actualPerson);

        /* Delete*/
        this.personService.deletePerson(person.getFirstName(), person.getLastName());

    }

    @Test
    @DisplayName("Should return updated person")
    public void updatePerson() throws IOException {

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Boyd");
        person.setAddress("1509 Culver St");
        person.setZip("97451");
        person.setCity("Strasbourg");
        person.setEmail("jaboyd@email.com");
        person.setPhone("841-874-6512");

        Person personPrev = new Person();
        personPrev.setFirstName("John");
        personPrev.setLastName("Boyd");
        personPrev.setAddress("1509 Culver St");
        personPrev.setZip("97451");
        personPrev.setCity("Culver");
        personPrev.setEmail("jaboyd@email.com");
        personPrev.setPhone("841-874-6512");

        Person actualPerson = personService.updatePerson(person, person.getFirstName(), person.getLastName());

        List<Person> expectedPerson = this.readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getFirstName().equals(person.getFirstName())
                        &&
                        f.getLastName().equals(person.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedPerson.get(0), actualPerson);

        personService.updatePerson(personPrev, personPrev.getFirstName(), personPrev.getLastName());
    }

    @Test
    @DisplayName("Should return empty if the person doesn't exist")
    public void ShouldReturnEmpty() throws IOException {

        Person person = new Person();
        person.setFirstName("Adam");
        person.setLastName("Decarte");
        person.setAddress("5 rue Lauth");
        person.setZip("67000");
        person.setCity("Strasbourg");
        person.setEmail("test@gmail.com");
        person.setPhone("+6 00 00 00 00");

        Person actualPerson = personService.updatePerson(person, "Théo", "D");

        Assertions.assertNull(actualPerson);
    }


    @Test
    @DisplayName("Should return empty because person was deleted")
    public void deletePersonShouldReturnNull() throws IOException {

        Person person = new Person();
        person.setFirstName("Adam");
        person.setLastName("Decarte");
        person.setAddress("5 rue Lauth");
        person.setZip("67000");
        person.setCity("Strasbourg");
        person.setEmail("test@gmail.com");
        person.setPhone("+6 00 00 00 00");

        List<Person> personList = new ArrayList<>();
        personList.add(person);

        personService.savePerson(person);

        List<Person> exist = this.readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getFirstName().equals(person.getFirstName())
                        &&
                        f.getLastName().equals(person.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(personList.get(0), exist.get(0));

        personService.deletePerson(person.getFirstName(), person.getLastName());

        List<Person> actual = this.readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getFirstName().equals(person.getFirstName())
                        &&
                        f.getLastName().equals(person.getLastName()))
                .collect(Collectors.toList());

        Assertions.assertEquals(0,  actual.size());
    }

    @Test
    @DisplayName("Should return null because person doesn't exist")
    public void deletePersonShouldMatchNull() throws IOException {

        personService.deletePerson("Théo", "D");

        List<Person> actual = this.readJsonFile.readFile().getPersons()
                .stream()
                .filter(f -> f.getFirstName().equals("Théo")
                        &&
                        f.getLastName().equals("D"))
                .collect(Collectors.toList());

        Assertions.assertEquals(0,  actual.size());
    }

    @Test
    @DisplayName("Should return list of all persons")
    public void shouldReturnListOfPersons() throws IOException {

        List<Person> actualPerson = personService.getPersons();

        List<Person> expected = this.readJsonFile.readFile().getPersons();

        Assertions.assertEquals(expected.size(), actualPerson.size());
    }

    @Test
    @DisplayName("Should return a list of children (18 years or less) living at the given address")
    public void getChildren() throws IOException {
        List<Object> actualFireStation = personService.getChildren("947 E. Rose Dr");

        Assertions.assertEquals(1, actualFireStation.size());
    }

    @Test
    @DisplayName("Should return a list of phones of persons living at the given address")
    public void getPhones() throws IOException {
        List<String> numbers = personService.getPhones("2");

        Assertions.assertEquals(3, numbers.size());
    }

    @Test
    @DisplayName("Should return a list of persons living at the given address")
    public void getPersonsFromFire() throws IOException {
        List<Object> numbers = personService.getPersonsFromFire("112 Steppes Pl");

        Assertions.assertEquals(4, numbers.size());
    }

    @Test
    @DisplayName("Should return a list of foyers living at the area covered by given fire station")
    public void getPersonsFromFlood() throws IOException {
        List<String> fireStation = new ArrayList<>();
        fireStation.add("4");
        Map<String, List<PersonWithMedicalRecords>> foyers = personService.getPersonsFromFlood(fireStation);

        Assertions.assertEquals(2, foyers.size());
    }

    @Test
    @DisplayName("Should return persons")
    public void getPersonInfo() throws IOException {
        List<Object> persons = personService.personInfo("Eric", "Cadigan");

        Assertions.assertEquals(2, persons.size());
    }

    @Test
    @DisplayName("Should return a list of emails of persons living from given city")
    public void getCommunityEmails() throws IOException {
        List<String> persons = personService.communityEmail("Culver");

        Assertions.assertEquals(23, persons.size());
    }
}
