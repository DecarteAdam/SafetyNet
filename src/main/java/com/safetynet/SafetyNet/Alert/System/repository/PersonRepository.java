package com.safetynet.SafetyNet.Alert.System.repository;

import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {

}
