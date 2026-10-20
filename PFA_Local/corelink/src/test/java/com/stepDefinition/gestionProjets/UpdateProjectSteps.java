package com.stepDefinition.gestionProjets;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;

import com.api.ProjectManagementApi;
import com.facadeComponent.SweetAlerts;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionProjets.UpdateProjectPage;
import com.utils.*;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UpdateProjectSteps {
	
	 private final UpdateProjectPage updateProjectPage = new UpdateProjectPage(SetUp.getDriver());
	 private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	 private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());
	    
	 private String currentTitre, currentDescription, currentDateDebut, currentDateFin, currentStatus, currentMembers, currentFiles;
	 private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	 private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	  
	 
	 @Given("je souhaite accéder a la plateforme en tant que manager")
	    public void jeSuisConnectéEnTantQueManager() {
	        String matricule = loginPage.config.getProperties("manager.matricule");
	        String password  = loginPage.config.getProperties("manager.password");
	        loginPage.connexion(matricule, password);
	        
	        String urlToBe = loginPage.config.getProperties("url_MANAGER");
			loginPage.verifURLContenu(urlToBe);
	       }
	    
	    @When("je souhaite cliquer sur {string}")
	    public void jAppuieSurLaTouche(String label) { 
	    	updateProjectPage.clickLabel(label); 
	    }
	    
	    @Given("un projet est auto crée")
	    public void seedAutoProjet() {
	        Map<String,String> in = new LinkedHashMap<>();
	        in.put("titre", "<auto>");
	        in.put("description", "<auto>");
	        in.put("dateDebut", "2026-12-20");
	        in.put("dateFin", "2050-12-20");
	        in.put("statut", "EN_COURS");
	        in.put("membres", "salma, chiraz, fahed, pouka, sameh");
	        in.put("fichiers","image 1.jpeg, BDD.pdf");

	        Map<String,String> m = GenerateRandomDataUsingFaker.apply(in);
	        new ProjectManagementApi()
	            .createProject(
	                m.get("titre"),
	                m.get("description"),
	                m.get("dateDebut"),
	                m.get("dateFin"),
	                m.get("statut"),
	                m.get("membres"),
	                m.get("fichiers")
	            )
	            .verifyProjectCreatedSuccessfully();

	        	
	        currentTitre       = m.get("titre");
	        currentDescription = m.get("description");
	        currentDateDebut   = m.get("dateDebut");
	        currentDateFin     = m.get("dateFin");
	        currentStatus      = m.get("statut");
	        currentMembers     = m.get("membres");
	        currentFiles       = m.get("fichiers");
	    }

	    @And("je sélectione un et je clique sur icon {string}")
	    public void chercherAndSelectionner(String btn) {
	    	
	    	updateProjectPage.clickPilotageProjets();
	    	updateProjectPage.clickListProjet();
		    updateProjectPage.trySelectByFilter(currentTitre);
	    	updateProjectPage.getBoutonActionParTitle(currentTitre, btn).click();
	    }

	    @Then("un formulaire déja pré-rempli sera ouvert")
	    public void verifierFormulairePreRempli() {
	        Assertions.assertFalse(currentTitre.isEmpty());
	        Assertions.assertFalse(currentDescription.isEmpty());
	        Assertions.assertFalse(currentDateDebut.isEmpty());
	        Assertions.assertFalse(currentDateFin.isEmpty());
	        Assertions.assertFalse(currentStatus.isEmpty());
	        Assertions.assertFalse(currentMembers.isEmpty());
	        Assertions.assertFalse(currentFiles.isEmpty());
	    }

	    @And("je souhaite modifier tous les champs :")
	    public void updateProject(DataTable dataTable) {
	        Map<String, String> m = GenerateRandomDataUsingFaker.apply(dataTable.asMap(String.class, String.class));
	        updateProjectPage.fillFormUpdate(m);
	        currentTitre       = m.get("titre");
	        currentDescription = m.get("description");
	        currentDateDebut   = m.get("dateDebut");
	        currentDateFin     = m.get("dateFin");
	        currentStatus      = m.get("statut");
	        currentMembers     = m.get("membres");
	        currentFiles       = m.get("images");
	    }
	    @Then("pop-up de {string} avec un message {string} sera visible")
	 		public void assertSucces(String titre, String message) {
	 			sweetAlert.assertAlert(titre, message, true);
	 	}
	    @Then("je suis réenvoyé vers la liste des projets et je souhaite voir un projet avec :")
		public void jeSuisEnvoyéVersLaListeDesProjetsEtJeVoisUnProjetAvec(DataTable dataTable) {
			Map<String, String> expected = new HashMap<>(dataTable.asMap(String.class, String.class));
			updateProjectPage.trySelectByFilter(currentTitre);
			updateProjectPage.replaceIfPlaceholder(expected, "titre", currentTitre);
			updateProjectPage.replaceIfPlaceholder(expected, "dateDebut", LocalDate.parse(currentDateDebut, FR).format(ISO));
			updateProjectPage.replaceIfPlaceholder(expected, "dateFin",  LocalDate.parse(currentDateFin, FR).format(ISO));
			updateProjectPage.replaceIfPlaceholder(expected, "statut", currentStatus);
			updateProjectPage.assertResponsableInTable(currentTitre,
			updateProjectPage.nomResponsableExcpectedInTable.getText());
			updateProjectPage.assertRowContains(currentTitre, expected);
		}
	    

	    @And("je suis dirigé vers la liste des projets et je souhaite voir:")
		public void jeSuisDirigéVersLaListeDesProjetsEtJeVoir(DataTable dataTable) {
			Map<String, String> expected = new HashMap<>(dataTable.asMap(String.class, String.class));
			updateProjectPage.trySelectByFilter(currentTitre);
			updateProjectPage.replaceIfPlaceholder(expected, "titre", currentTitre);
			updateProjectPage.replaceIfPlaceholder(expected, "dateDebut",currentDateDebut);
			updateProjectPage.replaceIfPlaceholder(expected, "dateFin", currentDateFin);
			updateProjectPage.replaceIfPlaceholder(expected, "statut", currentStatus);
			updateProjectPage.assertResponsableInTable(currentTitre,
			updateProjectPage.nomResponsableExcpectedInTable.getText());
			updateProjectPage.assertRowContains(currentTitre, expected);
		}
	    
	    @And("je clique sur {string} pour voir tous les informations de projet :")
		public void detailsCheck(String bouton, DataTable t) {
	    	updateProjectPage.getBoutonActionParTitle(currentTitre, "details").click();
	    	updateProjectPage.waitForVisbilityPageDeatils();
			((JavascriptExecutor) SetUp.getDriver()).executeScript("window.scrollBy(0,-800)");

			Map<String, String> expected = new HashMap<>(t.asMap(String.class, String.class));
			updateProjectPage.replaceIfPlaceholder(expected, "titre", currentTitre);
			updateProjectPage.replaceIfPlaceholder(expected, "description", currentDescription);
			updateProjectPage.replaceIfPlaceholder(expected, "dateDebut", currentDateDebut);
			updateProjectPage.replaceIfPlaceholder(expected, "dateFin", currentDateFin);
			updateProjectPage.replaceIfPlaceholder(expected, "statut", currentStatus);
			updateProjectPage.replaceIfPlaceholder(expected, "membres", currentMembers);
			updateProjectPage.replaceIfPlaceholder(expected, "images", currentFiles);

			updateProjectPage.assertResponsableInDetails(updateProjectPage.nomResponsableExcpectedInDetails.getText());

			Assertions.assertEquals(expected.get("titre"), currentTitre);
			Assertions.assertEquals(expected.get("description"), currentDescription);
			Assertions.assertEquals(expected.get("dateDebut"), currentDateDebut);
			Assertions.assertEquals(expected.get("dateFin"), currentDateFin);
			Assertions.assertEquals(expected.get("statut"), currentStatus);

			((JavascriptExecutor) SetUp.getDriver()).executeScript("window.scrollBy(0,300)");
			Assertions.assertEquals(expected.get("membres"), currentMembers);
			Assertions.assertEquals(expected.get("images"), currentFiles);
		}
	 	    @And("je souhaite saisir une date de debut valide {string}")
	 		@And("je souhaite saisir une date de debut antierieure a aujourd'hui {string}")
	 		public void remplirDateDebut(String dateInput) {
	 			LocalDate d = updateProjectPage.parseDate(dateInput);
	 			updateProjectPage.saisirDateDebut(d.format(FR));
	 		}
	 	   @And("je souhaite saisir une date de fin antierieure a date debut {string}")
	 		public void rempliDateFin(String dateInput) {
	 			LocalDate d = updateProjectPage.parseDate(dateInput);
	 			updateProjectPage.saisirDateFin(d.format(FR));
	 		}
	 	 
	    @Then("un message d’erreur s'affiche {string} pour la date de debut")
	    public void verifDateDebutForUpdate(String msg) {
	    	 updateProjectPage.waitOfVesibilityMsgErreurDateDebutForUpdate();
	         Assertions.assertEquals(msg, updateProjectPage.msgErreurDateDebutForUpdate.getText());
	    }
	    @Then("un message d’erreur s'affiche {string} pour la date de fin")
	    public void verifDateFinForUpdate(String msg) {
	    	updateProjectPage.waitOfVesibilityMsgErreurDateFinForUpdate();
	        Assertions.assertEquals(msg, updateProjectPage.msgErreurDateFinForUpdate.getText());
	    }
	    
	 
	  


}
