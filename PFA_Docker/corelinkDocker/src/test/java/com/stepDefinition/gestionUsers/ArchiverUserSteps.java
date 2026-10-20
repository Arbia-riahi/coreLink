package com.stepDefinition.gestionUsers;

import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionUsers.UserPage;
import com.utils.GenerateRandomDataUsingFaker;
import com.api.UserManagementApi;
import com.hooks.SetUp;
import com.pageObjects.components.SweetAlerts;
import io.cucumber.java.en.*;
import java.util.*;
import org.junit.jupiter.api.Assertions;

public class ArchiverUserSteps {

	private final UserPage userPage = new UserPage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());
	private String currentMatricule , currentEmail;

	@Given("je me connecte en tant qu’administrateur")
	public void connexionAdmin() {

		String matricule = userPage.config.getProperties("admin.matricule");
		String password = userPage.config.getProperties("admin.password");
		loginPage.connexion(matricule, password);

		String urlToBe = userPage.config.getProperties("url_ADMIN");
		loginPage.verifURLContenu(urlToBe);
	}

	@When("je choisi de cliquer sur la bouton {string}")
	public void JeCliqueSur(String label) {
		userPage.clickByLabel(label);
	}

	@Given("un utilisateur actif auto-créé")
	public void seedAutoUser() {
	    	// Remplir avec <auto> puis générer via utilitaire
	        Map<String,String> in = new LinkedHashMap<>();
	        in.put("matricule", "<auto>");
	        in.put("nom", "<auto>");
	        in.put("prenom", "<auto>");
	        in.put("email", "<auto>");
	        in.put("password", "<auto>");
	        in.put("role", "MANAGER");
	        in.put("image", "image 1.jpeg");

	        Map<String,String> m = GenerateRandomDataUsingFaker.apply(in);
	    	
	        new UserManagementApi()
	        .createUser(
	            m.get("matricule"),
	            m.get("nom"),
	            m.get("prenom"),
	            m.get("email"),
	            m.get("password"),
	            m.get("role"),
	            m.get("image")
	        )
	        .verifyUserCreatedSuccessfully();

	    	// mémoriser l'état courant
	    	 currentMatricule = m.get("matricule");
	         currentEmail     = m.get("email");
	}

	@And("la page {string} qui contenant la liste des utilisateurs de l application s’affiche")
	public void listDisplayed(String expectedTitle) {
		
		userPage.verifVisibilityOfButtonGererCompte();
		userPage.clickGererComptes();

		userPage.verifVisibilityOfAddBuuton();
		userPage.clickListe();
		
		userPage.verifVisibilityOfTitleListeUser();
		String obtenu = userPage.titlePageCreateUser.getText();
		Assertions.assertEquals(expectedTitle, obtenu);
	}

	@And("je sélectionne l’utilisateur puis je clique sur l’icône {string}")
	public void selectCurrentAndClickIcon(String action) {
		// d'abord tenter le filtre (rapide)
		if (userPage.trySelectByFilter(currentMatricule, action))
			return;
		// sinon pagination
		try {
			userPage.appliquerFiltre("");
		} catch (Exception ignored) {
			System.out.println("ignored");
		}

		Assertions.fail("Utilisateur '" + currentMatricule + "' introuvable pour action '" + action + "'.");
	}

	@Then("alerte pour confirmer la suppression avec le titre {string} et le message {string} s’affiche")
	public void assertConfirmation(String titre, String message) {
		sweetAlert.assertAlertTemplate(titre, message);

	}

	@Then("une alerte succès avec le titre {string} et le message {string} s’affiche")
	public void assertSuccesUpdate(String titre, String message) {
		sweetAlert.assertAlert(titre, message, false);

	}

	@And("je souhaite chercher l utilisateur dans la liste")
	public void chercherUser() {
		userPage.trySelectByFilterV2(currentMatricule);

	}

	@Then("je vois l utilisateur dans la liste")
	public void assertCurrentPresent() {
		Assertions.assertTrue(userPage.utilisateurExisteDansListe(currentMatricule));
	}

	@Then("un message de non confirmité est visible")
	public void messageVisible() {
		userPage.verifVisibilityOfTableUser();
		Assertions.assertTrue(userPage.tableListUsers.getText().contains("Aucun résultat à afficher pour"));
	}

	@And("j annule la suppression")
	public void annulerSuppression() {
		sweetAlert.annuler();
	}

	@And("je confirme la suppression")
	public void confirmerSuppression() {
		sweetAlert.valider();
	}

}
