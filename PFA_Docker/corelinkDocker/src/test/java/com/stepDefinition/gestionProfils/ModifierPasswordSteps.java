package com.stepDefinition.gestionProfils;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;

import com.dataReader.PropertyReader;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionProfils.ModifierPasswordPage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class ModifierPasswordSteps {

	private final ModifierPasswordPage page = new ModifierPasswordPage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final PropertyReader config = new PropertyReader();

	@Given("je suis authentifiée tant que {string}")
	public void jeSuisAuthentifiéeTantQue(String role) {
		switch (role) {
		case "ADMIN" -> {
			String matricule = config.getProperties("admin.matricule");
			String password = config.getProperties("admin.password");
			loginPage.connexion(matricule, password);

			String urlToBe = loginPage.config.getProperties("url_ADMIN");
			loginPage.verifURLContenu(urlToBe);
		}
		case "MANAGER" -> {
			String matricule = config.getProperties("manager.matricule").trim();
			String password = config.getProperties("manager.password").trim();
			loginPage.connexion(matricule, password);

			String urlToBe = loginPage.config.getProperties("url_MANAGER");
			loginPage.verifURLContenu(urlToBe);
		}
		case "MEMBER" -> {
			String matricule = config.getProperties("member.matricule");
			String password = config.getProperties("member.password");
			loginPage.connexion(matricule, password);
			String urlToBe = loginPage.config.getProperties("url_MEMBER");
			loginPage.verifURLContenu(urlToBe);

		}
		default -> throw new IllegalArgumentException("Rôle inconnu: " + role);
		}

	}

	@Then("je souhaite consulter mon profile")
	public void jeCliqueSurLeMenuUtilsateurPourConsulterMonProfile() {
		page.clickMenuUser();
	}

	@Then("je souhaite actionner sur {string}")
	public void jeCliqueSur(String label) {
		page.clickByLabel(label);
	}

	@And("je saisi un ancien mot de passe incorrecte {string}")
	@And("je saisi l ancien mot de passe {string}")
	public void remplirAncienPassword(String ap) {

		switch (ap) {
		case "admin.Ancienpassword":
			ap = config.getProperties("admin.Ancienpassword").trim();
			break;

		case "manager.Ancienpassword":
			ap = config.getProperties("manager.Ancienpassword").trim();
			break;
		case "member.Ancienpassword":
			ap = config.getProperties("member.Ancienpassword").trim();
			break;
		default:
			System.out.println("password incorrecte");
		}
		page.saisirAncienPassword(ap);

	}

	@And("je saisi le nouveau mot de passe {string}")
	public void remplirNvPassword(String np) {
		switch (np) {
		case "admin.Nouveaupassword":
			np = config.getProperties("admin.Nouveaupassword").trim();
			break;

		case "manager.Nouveaupassword":
			np = config.getProperties("manager.Nouveaupassword").trim();
			break;
		case "member.Nouveaupassword":
			np = config.getProperties("member.Nouveaupassword").trim();
			break;
		default:
			System.out.println("password incorrecte");
		}
		page.saisirNvPassword(np);
	}

	@And("je confirme le nouveau mot de passe {string}")
	public void remplirConfirmPassword(String cp) {
		switch (cp) {
		case "admin.Confirmpassword":
			cp = config.getProperties("admin.Confirmpassword").trim();
			break;

		case "manager.Confirmpassword":
			cp = config.getProperties("manager.Confirmpassword").trim();
			break;
		case "member.Confirmpassword":
			cp = config.getProperties("member.Confirmpassword").trim();
			break;

		default:
			System.out.println("password incorrecte");
		}
		page.saisirConfirmPassword(cp);
	}

	@And("le localStorage sera vide")
	public void sessionVide() {
		boolean vide = (Boolean) ((JavascriptExecutor) SetUp.getDriver())
				.executeScript("return localStorage.length===0 && sessionStorage.length===0;");
		Assertions.assertTrue(vide, "Storage non vide après déconnexion");
	}

	@And("je serai envoyé vers la page de connexion pour se reconnecter {string}")
	public void jeSeraiEnvoyéVersLaPageDeConnexionPourSeReconnecter(String currrentUrl) {
		String expectedUrl = config.getProperties("url_login");
		currrentUrl = SetUp.getDriver().getCurrentUrl();
		Assertions.assertEquals(expectedUrl, currrentUrl);

	}

	@Then("je suis redigé vers {string} pour saisir un nouveau mot de passe")
	public void updatePasswordFirstLgin(String currentUrlKey) {

		String expectedUrl;
		switch (currentUrlKey) {
		case "url_update_password_admin":
			expectedUrl = config.getProperties("url_update_password_admin").trim();
			break;
		case "url_update_password_manager":
			expectedUrl = config.getProperties("url_update_password_manager").trim();
			break;
		case "url_update_password_member":
			expectedUrl = config.getProperties("url_update_password_member").trim();
			break;
		default:
			Assertions.fail("Clé inconnue: " + currentUrlKey);
			return; // pour le compilateur
		}

		String actualUrl = SetUp.getDriver().getCurrentUrl();
		Assertions.assertEquals(expectedUrl, actualUrl);
	}

	@Given("je suis authentifiée tant que {string} pour la premiere fois")
	public void jeSuisAuthentifiéeTantQueRolePremierFois(String role) {
		switch (role) {
		case "ADMIN" -> {
			String matricule = page.config.getProperties("admin.matricule");
			String password = page.config.getProperties("admin.password");
			loginPage.connexion(matricule, password);

			String urlToBe = loginPage.config.getProperties("url_update_password_admin");
			loginPage.verifURLContenu(urlToBe);

		}
		case "MANAGER" -> {
			String matricule = page.config.getProperties("managerFL.matricule").trim();
			String password = page.config.getProperties("managerFL.password").trim();
			loginPage.connexion(matricule, password);

			String urlToBe = loginPage.config.getProperties("url_update_password_manager");
			loginPage.verifURLContenu(urlToBe);

		}
		case "MEMBER" -> {
			String matricule = page.config.getProperties("member.matricule").trim();
			String password = page.config.getProperties("member.password").trim();
			loginPage.connexion(matricule, password);
			String urlToBe = loginPage.config.getProperties("url_update_password_member");
			loginPage.verifURLContenu(urlToBe);

		}
		default -> throw new IllegalArgumentException("Rôle inconnu: " + role);
		}

	}

	@And("je saisi les informations de modifications:")
	public void saisirInformation(DataTable t) {
		Map<String, String> input = t.asMap(String.class, String.class);
		page.fillFormUpdatePassword(input);

	}

}
