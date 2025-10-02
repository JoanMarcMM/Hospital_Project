package com.example.hospital_project;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DaoJson  {
	
	public ArrayList<Nurse> readFile() throws IllegalArgumentException, IOException {
		
		InputStream filePath = getClass().getResourceAsStream("/nurse_list.json");
	
		ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(filePath);
        JsonNode nurseArray = root.get("nurse_list");
        List<Nurse> nursesList = Arrays.asList(mapper.treeToValue(nurseArray, Nurse[].class));

        return new ArrayList<>(nursesList);
	     
		
		
		
	}

}