package com.utils.dataReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
	
     private Properties properties;
	 private String propertyFilePath = "src/test/resources/config/configFile.properties";
	   
	 public PropertyReader() {

		    try (BufferedReader reader = new BufferedReader(new FileReader(propertyFilePath))) {

		        properties = new Properties();
		        properties.load(reader);

		    } catch (FileNotFoundException e) {
		        System.out.println("Exception: " + e);
		        throw new RuntimeException("Configuration.properties not found at " + propertyFilePath);

		    } catch (IOException e) {
		        System.out.println("Exception: " + e);
		    }
		}

	    
	    public String getProperties(String prop) {
	        String param = properties.getProperty(prop);
	        if (param != null) {
	            return param;
	        } else {
	            throw new RuntimeException(prop + " not specified in the configuration file!");
	        }
	    }
}