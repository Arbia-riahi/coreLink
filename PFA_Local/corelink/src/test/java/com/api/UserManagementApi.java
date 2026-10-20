package com.api;


import com.utils.dataReader.PropertyReader;
import com.utils.logs.LogsManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;

public class UserManagementApi {

	RequestSpecification requestSpecification;
	Response response;
	private static final PropertyReader config = new PropertyReader();
	private final SignInApi signInApi = new SignInApi();

	public UserManagementApi() {
		// Constructor
		requestSpecification = RestAssured.given();
	}

	// endpoints
	private static final String CREATE_USER_ENDPOINT = "/users/admin/create";


	// endpoint : Create a new user account with full details
	public UserManagementApi createUser(String matricule, String nom, String prenom, String email, String motDePasse,
			String role, String image) {

		String mat = config.getProperties("admin.matricule");
		String password = config.getProperties("admin.password");
		signInApi.signIn(mat, password);

		// Query Parameters (matricule, nom, prenom, email, motDePasse,role)
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("matricule", matricule);
		queryParams.put("nom", nom);
		queryParams.put("prenom", prenom);
		queryParams.put("email", email);
		queryParams.put("motDePasse", motDePasse);
		queryParams.put("role", role);

		File img = new File("src/test/resources/uploads/" + image);

		response = requestSpecification.header("Authorization", "Bearer " + signInApi.getToken())
				.spec(Builder.getUserManagementMultipartSpec(queryParams, img)).post(CREATE_USER_ENDPOINT);
		LogsManager.info(response.asPrettyString());
		return this;
	}

	//Verify that user is created successfully 
	public UserManagementApi verifyUserCreatedSuccessfully() {
		Assertions.assertNotNull(response.jsonPath().getString("matricule"));
		return this;
	}
	
	
	

}
