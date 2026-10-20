package com.stepDefinition.Authentification;

import org.junit.jupiter.api.Assertions;
import com.pageObjects.authentification.AuthorizationPage;
import com.pageObjects.authentification.LoginPage;
import com.utils.dataReader.PropertyReader;
import com.facadeComponent.SweetAlerts;
import com.hooks.*;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AuthorizationSteps {

	private AuthorizationPage authorizationPage = new AuthorizationPage(SetUp.getDriver());
	private final PropertyReader config = new PropertyReader();
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final SweetAlerts sw = new SweetAlerts(SetUp.getDriver());

	@Given("je suis authentifié en tant que {string}")
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

	@And("je clique sur le bouton pour revenir a ala page home")
	@And("je clique sur le bouton pour revenir a mon espace")
	public void cliquerBouton() {
		authorizationPage.revenirArriere();
	}

	@Then("une SweetAlerte d erreur s affiche avec le titre {string} et le message {string}")
	public void VerifAlerteErreur(String titreAttendu, String msgAttendu) {
		sw.assertAlert(titreAttendu, msgAttendu, true);
	}

	@When("ce {string} accède à l URL {string}")
	public void JACCEDEALURLREFUSEE(String role, String url) {
		switch (role) {
		case "ADMIN" -> {
			authorizationPage.UrlRefuseeAdmin(url);
		}
		case "MANAGER" -> {
			authorizationPage.UrlRefuseeManager(url);
		}
		case "MEMBER" -> {
			authorizationPage.UrlRefuseeMember(url);
		}
		default -> throw new IllegalArgumentException("Rôle inconnu: " + role);
		}
	}

	@Then("une alerte s affiche avec le titre {string} et le message {string}")
	public void Alerte404(String titreAttendu, String msgAttendu) {
		sw.assertAlert(titreAttendu, msgAttendu, true);
	}

	@And("je navigue vers la page {string}")
	@Given("j ouvre le navigateur sur la page {string}")
	public void JOUVRELAPAGE(String urlKey) {
		switch (urlKey) {
		case "url_CoreLink" -> {
			authorizationPage.loginURLHome(urlKey);
			break;
		}
		case "url_login" -> {
			authorizationPage.loginURLLogin(urlKey);
			break;
		}

		case "url_ADMIN" -> {
			authorizationPage.loginURLAdmin(urlKey);
			break;
		}
		case "url_MANAGER" -> {
			authorizationPage.loginURLManager(urlKey);
			break;
		}
		case "url_MEMBER" -> {
			authorizationPage.loginURLMember(urlKey);
			break;
		}
		default -> throw new IllegalArgumentException("Clé inconnue: " + urlKey);
		}
	}

	@When("je navigue vers une URL inconnue {string}")
	public void urlIncorrecte(String url) {
		authorizationPage.UrlInvalid(url);

	}

	@Then("je suis redirigé vers la page {string}")
	public void urlNotFound(String expectedUrl) {
		expectedUrl = config.getProperties("url_NotFound");
		authorizationPage.urlNotFound404(expectedUrl);

		String currectUrl = SetUp.getDriver().getCurrentUrl();
		Assertions.assertEquals(expectedUrl, currectUrl);
	}

}
