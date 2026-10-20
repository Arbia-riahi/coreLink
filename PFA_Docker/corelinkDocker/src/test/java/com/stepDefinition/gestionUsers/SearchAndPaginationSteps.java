package com.stepDefinition.gestionUsers;

import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionUsers.UserPage;
import com.hooks.SetUp;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import java.util.*;

public class SearchAndPaginationSteps {

	private final UserPage userPage = new UserPage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final Map<String, String> memo = new HashMap<>();

	@Given("je login en tant que administrateur")
	public void connexionAdmin() {
		String matricule = userPage.config.getProperties("admin.matricule");
		String password = userPage.config.getProperties("admin.password");
		loginPage.connexion(matricule, password);

		String urlToBe = userPage.config.getProperties("url_ADMIN");
		loginPage.verifURLContenu(urlToBe);
	}

	@And("je valide la bouton {string}")
	public void simpleClick(String label) {
		userPage.clickByLabel(label);
	}

	@Then("je vois la liste des utilisateurs {string}")
	public void listUsers(String expectedTitle) {
		userPage.verifVisibilityOfTitleListeUser();
		String obtenu = userPage.titlePageCreateUser.getText();
		Assertions.assertEquals(expectedTitle, obtenu);
	}

	@When("j applique le filtre {string}")
	public String applyFilter(String query) {
		userPage.appliquerFiltre(query);
		return query;
	}

	@Then("un message {string} est visible")
	public void messageVisible(String expected) {
		userPage.verifVisibilityOfTableUser();
		String actualMessage = userPage.msgNotFundUserForSearch.getText();
		Assertions.assertTrue(actualMessage.contains(expected),
				"Le message attendu n'est pas affiché. Attendu : " + expected + " | Reçu : " + actualMessage);
	}

	@And("les contrôles de pagination sont désactivés")
	public void paginationDisabledOrHidden() {
		userPage.verifyIfPrecdentButtonNotClickable();
		userPage.verifyIfSuivantButtonNotClickable();
	}

	@Then("tous les résultats {string} qui corresponds a ce filtre {string} s affichent dans le tableau")
	public void resultsShown(String resultatObtenu, String resultAttendu) {

		Assertions.assertEquals(this.applyFilter(resultatObtenu.toLowerCase()), resultAttendu.toLowerCase());

	}

	// pagination
	@Then("je vois exactement {int} lignes dans le tableau")
	public void assertRowCount(int n) {
		Assertions.assertEquals(n, userPage.rowCount());
	}

	@And("le bouton {string} est non cliquable")
	public void assertBtnNotEnabled(String label) {
		switch (label) {
		case "Précédent" -> userPage.verifyIfPrecdentButtonNotClickable();
		case "Suivant" -> userPage.verifyIfSuivantButtonNotClickable();
		default -> System.out.println("not found");
		}
	}

	@And("le bouton {string} est cliquable")
	public void assertBtnEnabled(String label) {
		switch (label) {
		case "Précédent" -> userPage.verifyIfPrecdentButtonClickable();
		case "Suivant" -> userPage.verifyIfSuivantButtonClickable();
		default -> System.out.println("not found");
		}
	}

	@And("je mémorise le matricule de la 1ère ligne comme {string}")
	public void memorizeFirstRow(String alias) {
		memo.put(alias, userPage.firstRowMatricule());
	}

	@Then("le matricule de la 1 ère ligne est différent de {string}")
	public void assertFirstRowDifferentFrom(String alias) {
		Assertions.assertNotEquals(memo.get(alias), userPage.firstRowMatricule());
	}

	@Then("le matricule de la 1 ère ligne est égal à {string}")
	public void assertFirstRowEqualTo(String alias) {
		Assertions.assertEquals(memo.get(alias), userPage.firstRowMatricule());
	}

	@And("je vais à la première page")
	public void goFirstPage() {
		userPage.clickPrecedent();
	}

	@When("je vais à la dernière page")
	public void goLastPage() {
		userPage.clickSuivant();
	}
}
