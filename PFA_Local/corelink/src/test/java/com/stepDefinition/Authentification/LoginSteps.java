package com.stepDefinition.Authentification;

import org.junit.jupiter.api.Assertions;
import com.pageObjects.authentification.LoginPage;
import com.facadeComponent.SweetAlerts;
import com.hooks.*;
import com.utils.dataReader.PropertyReader;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;

public class LoginSteps {

	private LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private PropertyReader config = new PropertyReader();
	private SweetAlerts sw = new SweetAlerts(SetUp.getDriver());

	@Then("je suis redirigé vers la page d’accueil {string}")
	@Given("j ouvre le navigateur sur {string}")
	public void ouvrirLienHome(String urlHome) {
		loginPage.loginURLHome(urlHome);

	}

	@And("je clique sur le bouton {string}")
	public void cliquerBouton(String bouton) {
		switch (bouton) {
		case "Se connecter" -> loginPage.clickBtn();
		case "Connexion" -> loginPage.clickBtnHome();
		default -> throw new IllegalArgumentException("Bouton inconnu : " + bouton);
		}
	}

	@Then("je suis redirigé vers la page de connexion {string}")
	public void login(String urlLogin) {
		loginPage.loginURLLogin(urlLogin);
	}

	@And("je saisis le matricule {string}")
	public void jeSaisiLeMatricule(String matriculeKey) {

		// Si la valeur correspond à une clé dans le fichier properties
		if (matriculeKey.endsWith(".matricule")) {
			matriculeKey = config.getProperties(matriculeKey).trim();
		}

		loginPage.saisirMatricule(matriculeKey);
	}

	@And("je saisis le mot de passe {string}")
	public void jeSaisiLeMotDePasseDeConnexion(String passwordKey) {

		// Si la valeur correspond à une clé dans le fichier properties
		if (passwordKey.endsWith(".password")) {
			passwordKey = config.getProperties(passwordKey).trim();
		}

		loginPage.saisirPassword(passwordKey);
	}

	@Then("une alerte de succès s affiche avec le titre {string} et le message {string}")
	public void VerifAlerteSuccées(String titreAttendu, String msgAttendu) {
		sw.assertAlert(titreAttendu, msgAttendu, false);
	}

	@Then("une alerte d erreur s affiche avec le titre {string} et le message {string}")
	public void VerifAlerteErreur(String titreAttendu, String msgAttendu) {
		sw.assertAlert(titreAttendu, msgAttendu, true);
	}

	@And("je reste sur la page d’accueil du rôle {string}")
	@Then("je suis redirigé vers la page d accueil du rôle {string}")
	public void jeMeRedirigeVersLaPageDAcceuil(String role) {
		String expected = loginPage.config.getProperties("url_" + role);
		loginPage.verifURLContenu(expected);
		String currentUrl = SetUp.getDriver().getCurrentUrl();
		boolean currentUrlContains = SetUp.getDriver().getCurrentUrl().contains(expected);
		Assertions.assertTrue(currentUrlContains,
				"L'URL actuelle ne correspond pas : attendue contenant " + expected + " mais obtenue " + currentUrl);
	}

	@And("l’espace affiché pour l’utilisateur authentifié est {string}")
	public void verifAfterLogin(String espace) {
		switch (espace) {
		case "Espace Administrateur" -> {
			loginPage.verifDashTitleAfterLogin(loginPage.espaceAdmin);
			Assertions.assertEquals(espace, loginPage.espaceAdmin.getText().trim());
		}
		case "Espace Chef de Projet" -> {
			loginPage.verifDashTitleAfterLogin(loginPage.espaceManager);
			Assertions.assertEquals(espace, loginPage.espaceManager.getText().trim());
		}
		case "Espace collaborateurs" -> {
			loginPage.verifDashTitleAfterLogin(loginPage.espaceMember);
			Assertions.assertEquals(espace, loginPage.espaceMember.getText().trim());
		}
		default -> throw new IllegalArgumentException("Rôle inattendu: " + espace);
		}
	}

	@When("Je clique à nouveau sur l'icône œil")
	@And("Je clique sur l'icône œil pour visualiser le mot de passe")
	public void clickBtnMasquage() {
		loginPage.clickBtnMasquage();
	}

	@Then("Le {string} est affiché en clair dans le champ")
	public void assertPasswordText(String expectedPasswordKey) {
		// Valeur attendue depuis ton fichier .properties
		String expected = config.getProperties(expectedPasswordKey);

		// Valeur affichée (UI)
		String actual = loginPage.readTextPassword();

		System.out.println("Expected = " + expected);
		System.out.println("Actual = " + actual);

		Assertions.assertEquals(expected, actual, "Le mot de passe affiché ne correspond pas à celui attendu !");
	}

	// Je m’assure que le champ est bien revenu en mode ‘password’.”
	@Then("Le mot de passe est à nouveau masqué")
	public void leMotDePasseEstDeNouveauMasque() {
		String actualType = loginPage.getPasswordAttribute();
		String expectedType = "password";

		Assertions.assertEquals(expectedType, actualType, "Le champ n’est pas revenu en mode masqué !");
	}

}
