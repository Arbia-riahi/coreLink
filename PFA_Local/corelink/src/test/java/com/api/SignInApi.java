package com.api;


import com.utils.logs.LogsManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;


public class SignInApi {

	RequestSpecification requestSpecification;
	Response response;

	public SignInApi() {
		// Constructor
		requestSpecification = RestAssured.given();
	}

	private static final String SIGNIN_ENDPOINT = "/api/auth/signin";
	private String token;

	public SignInApi signIn(String matricule, String motDePasse) {
		Map<String, String> body = new HashMap<>();
		body.put("matricule", matricule);
		body.put("motDePasse", motDePasse);

		response = requestSpecification.spec(Builder.authRequestSpecification(body)).post(SIGNIN_ENDPOINT);

		LogsManager.info(response.asPrettyString());

		// EXTRACTION DU TOKEN
		token = response.jsonPath().getString("token");
		LogsManager.info("TOKEN RÉCUPÉRÉ : " + token);

		return this;
	}

   public String getToken() { return token; }
}
