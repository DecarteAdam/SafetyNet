package com.safetynet.SafetyNet.Alert.System.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.SafetyNet.Alert.System.model.DataModel;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ReadJsonFile {

    public DataModel readFile() throws IOException {

        //create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("src/main/resources/person2.json");

        return objectMapper.readValue(file, DataModel.class);
    }


}
