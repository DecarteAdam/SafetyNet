package com.safetynet.SafetyNet.Alert.System;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNet.Alert.System.model.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class SafetyNetAlertSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertSystemApplication.class, args);
	}


}
