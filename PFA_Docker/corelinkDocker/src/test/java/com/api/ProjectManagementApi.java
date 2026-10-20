package com.api;

import com.logs.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

public class ProjectManagementApi {

	
        RequestSpecification requestSpecification;
		Response response;
		

		public ProjectManagementApi() {
			// Constructor
			requestSpecification = RestAssured.given();
		}

		// endpoints
		private static final String CREATE_PROJECT_ENDPOINT = "/api/projets/create/ala";

		// endpoint : Create a new project
		public ProjectManagementApi createProject(String titre, String description, String dateDebut, String dateFin, String status,
				String membres, String fichiers) {

			Map<String, String> queryParams = new HashMap<>();
			queryParams.put("titre", titre);
			queryParams.put("description", description);
			queryParams.put("dateDebut", dateDebut);
			queryParams.put("dateFin", dateFin);
			queryParams.put("status", status);
			queryParams.put("membres", membres);
			queryParams.put("fichiers", fichiers);

			List<File> images = new ArrayList<>();
			for (String f : fichiers.split(",")) 
				images.add(new File("src/test/resources/uploads/" + f.trim()));



			response = requestSpecification
					.spec(Builder.getProjectManagementMultipartSpec(queryParams, images))
					.post(CREATE_PROJECT_ENDPOINT);
			LogsManager.info(response.asPrettyString());
			return this;
		}

		//Verify that user is created successfully 
		public ProjectManagementApi verifyProjectCreatedSuccessfully() {
			Assertions.assertNotNull(response.jsonPath().getString("titre"));
			return this;
		}
		
		
		

	}

