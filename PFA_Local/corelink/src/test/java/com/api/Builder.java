package com.api;

import java.io.File;
import java.util.List;
import java.util.Map;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import com.utils.dataReader.PropertyReader;

public class Builder {
	private static final PropertyReader config=new PropertyReader();
	private  static String baseURI =config.getProperties("baseUrl");
    private Builder() {
        // Private constructor to prevent instantiation
    }
    
    public static RequestSpecification authRequestSpecification(Object jsonBody)
   {
        return new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setContentType(ContentType.JSON) 
                .setBody(jsonBody)               
                .build();
    }
    
    //build request specification
    public static RequestSpecification getUserManagementMultipartSpec(
            Map<String, ?> queryParams,
            File imageFile
    ) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setContentType("multipart/form-data")
                .addQueryParams(queryParams);

        builder.addMultiPart("image", imageFile);

        return builder.build();
    }
    
    
    public static RequestSpecification getProjectManagementMultipartSpec(
            Map<String, ?> queryParams,
            List<File> imageFiles
    ) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setContentType("multipart/form-data")
                .addQueryParams(queryParams);

        for (File f : imageFiles) {
        	builder.addMultiPart("fichiers", f);

        }

        return builder.build();
    }
    
    public static RequestSpecification getActualityManagementMultipartSpec(
            Map<String, ?> queryParams,
            List<File> imageFiles
    ) {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .setContentType("multipart/form-data")
                .addQueryParams(queryParams);

        // NOM EXACT attendu par ton controller → "imageFiles"
        for (File f : imageFiles) {
            builder.addMultiPart("imageFiles", f);
        }

        return builder.build();
    }






  

   
}
