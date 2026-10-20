package com.ProjectManagement;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectManagementApplication {

	public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

	public static void main(String[] args) {
		new File(uploadDirectory).mkdir();
		// initRoles();
		SpringApplication.run(ProjectManagementApplication.class, args);
	}

}
