package com.stepDefinition.Authentification;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.authentification.LogoutPage;
import com.hooks.*;
import com.dataReader.PropertyReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class LogoutSteps {
	private final LogoutPage logoutPage;
	private final LoginPage loginPage;
	private final PropertyReader config;

	public LogoutSteps() {
		logoutPage = new LogoutPage(SetUp.getDriver());
		loginPage = new LoginPage(SetUp.getDriver());
		config = new PropertyReader();
	}

	@Given("je suis authentifié en tant que un utilisateur avec le role {string}")
	public void jeSuisAuthentifie(String role) {
		switch (role) {
		case "ADMIN" -> {
			String matricule = config.getProperties("admin.matricule");
			String password = config.getProperties("admin.password");
			loginPage.connexion(matricule, password);

			String urlToBe = config.getProperties("url_ADMIN");
			loginPage.verifURLContenu(urlToBe);
		}
		case "MANAGER" -> {
			String matricule = config.getProperties("manager.matricule").trim();
			String password = config.getProperties("manager.password").trim();
			loginPage.connexion(matricule, password);

			String urlToBe = config.getProperties("url_MANAGER");
			loginPage.verifURLContenu(urlToBe);

		}
		case "MEMBER" -> {
			String matricule = config.getProperties("member.matricule");
			String password = config.getProperties("member.password");
			loginPage.connexion(matricule, password);

			String urlToBe = config.getProperties("url_MEMBER");
			loginPage.verifURLContenu(urlToBe);

		}
		default -> throw new IllegalArgumentException("Rôle inconnu: " + role);
		}

	}

	@When("j ouvre le menu utilisateur dans l’en-tête et le menu s affiche")
	public void jOuvreLeMenuUtilisateurDansLEnTête() {
		logoutPage.menuUserClick();
	}

	@And("je clique sur le bouton de deconnexion")
	public void cliquerBouton() {
		logoutPage.logout();
	}

	@Then("je suis renvoyé vers la page {string}")
	public void login(String urlLogin) {
		loginPage.loginURLLogin(urlLogin);

	}

	@And("je re accede vers les espaces reserves aux utilsateurs d application {string}")
	public void restricedAcces(String espace) {
		logoutPage.UrlRestriced(espace);

	}

	@Then("je suis redirigé vers la page home {string}")
	public void ouvrirLienHome(String urlHome) {
		loginPage.loginURLHome(urlHome);

	}

	@Then("le localStorage ne contient plus le jeton d’authentification")
	public void sessionVide() {
		boolean vide = (Boolean) ((JavascriptExecutor) SetUp.getDriver())
				.executeScript("return localStorage.length===0 && sessionStorage.length===0;");
		Assertions.assertTrue(vide, "Storage non vide après déconnexion");
	}

}
