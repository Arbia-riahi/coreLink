package com.stepDefinition.gestionProjets;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionProjets.CreateProjectPage;
import com.utils.*;
import com.pageObjects.components.SweetAlerts;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateProjectSteps {
	private final CreateProjectPage createProjectPage = new CreateProjectPage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());

	private String currentTitre, currentDescription, currentDateDebut, currentDateFin, currentStatus, currentMembers,
			currentFiles;
	private static final DateTimeFormatter FR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd");



	@Given("je suis connecté en tant que manager")
	public void jeSuisConnectéEnTantQueManager() {
		String matricule = loginPage.config.getProperties("manager.matricule");
		String password = loginPage.config.getProperties("manager.password");
		loginPage.connexion(matricule, password);

		String urlToBe = loginPage.config.getProperties("url_MANAGER");
		loginPage.verifURLContenu(urlToBe);
	}

	@When("j appuie sur la touche {string}")
	public void jAppuieSurLaTouche(String label) {
		createProjectPage.clickLabel(label);
	}

	@Then("un formulaire vierge est bien visible sur la page de création")
	public void unFormulaireViergeEstBienVisibleSurLaPageDeCréation() {
		createProjectPage.VerifvalTitreIsBlank();
		createProjectPage.VerifvalDescriptionIsBlank();
		createProjectPage.VerifvalDateDebutIsBlank();
		createProjectPage.VerifvalDateFinIsBlank();
		createProjectPage.checkIfCheckboxIsNotSelected(createProjectPage.checkboxes);
		createProjectPage.VerifUploadFileIsBlank(); // ok si span/label, sinon à adapter
	}

	@Then("je saisis les informations relatives a un projet :")
	public void jeSaisisLesInformationsRelativesAUnProjet(DataTable dataTable) {
		Map<String, String> m = GenerateRandomDataUsingFaker.apply(dataTable.asMap(String.class, String.class));
		createProjectPage.fillForm(m);
		currentTitre = m.getOrDefault("titre", "");
		currentDescription = m.getOrDefault("description", "");
		currentDateDebut = m.getOrDefault("dateDebut", "");
		currentDateFin = m.getOrDefault("dateFin", "");
		currentStatus = m.getOrDefault("statut", "");
		currentMembers = m.getOrDefault("membres", "");
		currentFiles = m.getOrDefault("photos", "");
	}

	@Then("je suis envoyé vers la liste des projets et je vois un projet avec :")
	public void jeSuisEnvoyeVersLaListeDesProjetsEtJeVoisUnProjetAvec(DataTable dataTable) {

	    Map<String, String> expected = new HashMap<>(dataTable.asMap(String.class, String.class));

	    createProjectPage.trySelectByFilter(currentTitre);

	    createProjectPage.replaceIfPlaceholder(expected, "titre", currentTitre);

	   
	    createProjectPage.replaceIfPlaceholder(expected, "dateDebut", LocalDate.parse(currentDateDebut, FR).format(ISO));
	    createProjectPage.replaceIfPlaceholder(expected, "dateFin",    LocalDate.parse(currentDateFin, FR).format(ISO));

	    createProjectPage.replaceIfPlaceholder(expected, "statut", currentStatus);

	    createProjectPage.assertResponsableInTable(currentTitre,
	            createProjectPage.nomResponsableExcpectedInTable.getText());

	    createProjectPage.assertRowContains(currentTitre, expected);
	}

	@Then("Alerte de {string} avec un message {string} sera visible")
	public void assertSucces(String titre, String message) {
		sweetAlert.assertAlert(titre, message, true);
	}

	@And("je clique sur icon {string} pour voir tous les informations de projet :")
	public void detailsCheck(String bouton, DataTable t) {
		createProjectPage.getBoutonActionParTitle(currentTitre, "details").click();
		((JavascriptExecutor) SetUp.getDriver()).executeScript("window.scrollBy(0,-800)");

		Map<String, String> expected = new HashMap<>(t.asMap(String.class, String.class));
		createProjectPage.replaceIfPlaceholder(expected, "titre", currentTitre);
		createProjectPage.replaceIfPlaceholder(expected, "description", currentDescription);
		createProjectPage.replaceIfPlaceholder(expected, "dateDebut", currentDateDebut);
		createProjectPage.replaceIfPlaceholder(expected, "dateFin", currentDateFin);
		createProjectPage.replaceIfPlaceholder(expected, "statut", currentStatus);
		createProjectPage.replaceIfPlaceholder(expected, "membres", currentMembers);
		createProjectPage.replaceIfPlaceholder(expected, "photos", currentFiles);

		createProjectPage.assertResponsableInDetails(createProjectPage.nomResponsableExcpectedInDetails.getText());

		Assertions.assertEquals(expected.get("titre"), currentTitre);
		Assertions.assertEquals(expected.get("description"), currentDescription);
		Assertions.assertEquals(expected.get("dateDebut"), currentDateDebut);
		Assertions.assertEquals(expected.get("dateFin"), currentDateFin);
		Assertions.assertEquals(expected.get("statut"), currentStatus);

		((JavascriptExecutor) SetUp.getDriver()).executeScript("window.scrollBy(0,300)");
		Assertions.assertEquals(expected.get("membres"), currentMembers);
		Assertions.assertEquals(expected.get("photos"), currentFiles);
	}

	@And("je saisis une date de debut valide {string}")
	@And("je saisis une date de debut antierieure a aujourd'hui {string}")
	public void remplirDateDebut(String dateInput) {
	    LocalDate d = createProjectPage.parseDate(dateInput);
	    createProjectPage.saisirDateDebut(d.format(FR)); 
	}


	@Then("un message d’erreur concernat la date de debut sera affiché au dessous {string}")
	public void verifDateDebutForCreate(String msg) {
		createProjectPage.waitOfVesibilityMsgErreurDateDebut();
		Assertions.assertEquals(msg, createProjectPage.msgErreurDateDebut.getText());
	}

	@Then("un message d’erreur concernat la date de fin sera affiché au dessous {string}")
	public void verifDateFinForCreate(String msg) {
		createProjectPage.waitOfVesibilityMsgErreurDateFin();
		Assertions.assertEquals(msg, createProjectPage.msgErreurDateFin.getText());
	}

	@And("je saisis une date de fin antierieure a date debut {string}")
	public void rempliDateFin(String dateInput) {
		LocalDate d = createProjectPage.parseDate(dateInput);
		createProjectPage.saisirDateFin(d.format(FR));
	}

	@Then("la bouton {string} est non cliquable")
	public void boutonNonCliquable(String btn) {
		createProjectPage.verifBtnNonCliquable();
	}

}
