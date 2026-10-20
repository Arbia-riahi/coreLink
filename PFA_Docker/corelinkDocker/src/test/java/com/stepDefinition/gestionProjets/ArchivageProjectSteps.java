package com.stepDefinition.gestionProjets;

import java.util.*;
import org.junit.jupiter.api.Assertions;

import com.api.ProjectManagementApi;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionProjets.ArchivageProjectPage;
import com.utils.GenerateRandomDataUsingFaker;
import com.pageObjects.components.SweetAlerts;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;


public class ArchivageProjectSteps {

	private final ArchivageProjectPage archivageProjectPage = new ArchivageProjectPage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());

	private String currentTitre;

	@Given("je suis identifié en tant que manager")
	public void jeSuisConnectéEnTantQueManager() {
		String matricule = loginPage.config.getProperties("manager.matricule");
		String password = loginPage.config.getProperties("manager.password");
		loginPage.connexion(matricule, password);

		String urlToBe = loginPage.config.getProperties("url_MANAGER");
		loginPage.verifURLContenu(urlToBe);
	}

	@Given("je souhaite un projet auto crée")
	public void seedAutoProjet() {
		Map<String, String> in = new LinkedHashMap<>();
		in.put("titre", "<auto>");
		in.put("description", "<auto>");
		in.put("dateDebut", "2026-12-20");
		in.put("dateFin", "2050-12-20");
		in.put("statut", "EN_COURS");
		in.put("membres", "salma, chiraz, fahed, pouka, sameh");
		in.put("fichiers", "image 1.jpeg, BDD.pdf");

		Map<String, String> m = GenerateRandomDataUsingFaker.apply(in);
		new ProjectManagementApi().createProject(m.get("titre"), m.get("description"), m.get("dateDebut"),
				m.get("dateFin"), m.get("statut"), m.get("membres"), m.get("fichiers"))
				.verifyProjectCreatedSuccessfully();

		currentTitre = m.get("titre");
	}

	@And("je le souhaite sélectionner en cliquant sur {string}")
	public void chercherAndSelectionner(String btn) {
		archivageProjectPage.clickPilotageProjets();
		archivageProjectPage.clickListProjet();

		archivageProjectPage.trySelectByFilter(currentTitre);
		archivageProjectPage.getBoutonActionParTitle(currentTitre, btn).click();
	}

	@Then("une pop up de confirmation s ouvre avec un titre {string} et un message {string}")
	public void assertConfirmation(String titre, String message) {
		sweetAlert.assertAlert(titre, message, false);
	}

	@And("je valide l archivage")
	public void validerPopup() {
		sweetAlert.valider();
	}

	@And("j annule l archivage")
	public void annulerPopup() {
		sweetAlert.annuler();
	}

	@Then("je souhaite chercher le projet")
	public void chercherCurrentProjetAbsent() {
		archivageProjectPage.filter(currentTitre);
	}

	@Then("un message d erreur s affiche {string}")
	public void assertNoResults(String msg) {
		Assertions.assertEquals(archivageProjectPage.msgErreurAucunFiltreResult.getText(), msg.trim());
	}

	@Then("je souhaite voir que le projet persiste encore dans la liste")
	public void assertCurrentPresent() {
		archivageProjectPage.trySelectByFilter(currentTitre);
	}
}
