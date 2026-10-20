package com.api;

import com.dataReader.PropertyReader;
import com.logs.LogsManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.util.*;

import org.junit.jupiter.api.Assertions;

public class ActualityMnagementApi {

    RequestSpecification requestSpecification;
    Response response;
    private static final PropertyReader config = new PropertyReader();
    private final SignInApi signInApi = new SignInApi();

    public ActualityMnagementApi() {
        requestSpecification = RestAssured.given();
    }

    private static final String CREATE_ACTUALITY_ENDPOINT = "/actualites";

    public ActualityMnagementApi createActuality(String titre, String description, String imageFiles) {

        // Authentification
        String mat = config.getProperties("admin.matricule");
        String password = config.getProperties("admin.password");
        signInApi.signIn(mat, password);

        // Params NON multipart
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("titre", titre);
        queryParams.put("description", description);

        // Liste des fichiers images
        List<File> photos = new ArrayList<>();
        for (String f : imageFiles.split(",")) {
            photos.add(new File("src/test/resources/uploads/" + f.trim()));
        }

        // Appel API
        response = requestSpecification
                .spec(Builder.getActualityManagementMultipartSpec(queryParams, photos))
                .post(CREATE_ACTUALITY_ENDPOINT);

        LogsManager.info(response.asPrettyString());
        return this;
    }

    public ActualityMnagementApi verifyActualityCreatedSuccessfully() {
        Assertions.assertNotNull(response.jsonPath().getString("titre"));
        return this;
    }
}
