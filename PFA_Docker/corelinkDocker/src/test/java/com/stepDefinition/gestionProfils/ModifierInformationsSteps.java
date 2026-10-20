package com.stepDefinition.gestionProfils;


import java.util.Map;
import org.junit.jupiter.api.Assertions;
import com.dataReader.PropertyReader;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.components.SweetAlerts;
import com.pageObjects.gestionProfils.ModifierInformationsPage;
import com.utils.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class ModifierInformationsSteps {
	private final ModifierInformationsPage page = new ModifierInformationsPage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());

	private final PropertyReader config = new PropertyReader();
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());

	private String currentEmail, currentNom, currentPrenom, currentImage;
	
	@Given("que j’ai ouvert une session en tant que {string}")
	public void jeSuisAuthentifiéeTantQue(String role) {
		switch (role) {
		case "ADMIN" -> {
			String matricule =config.getProperties("admin.matricule");
			String password =config.getProperties("admin.password");
			loginPage.connexion(matricule, password);
			
			String urlToBe = loginPage.config.getProperties("url_ADMIN");
			loginPage.verifURLContenu(urlToBe);	
		}
		case "MANAGER" -> {
			String matricule =config.getProperties("manager.matricule").trim();
			String password = config.getProperties("manager.password").trim();		
			loginPage.connexion(matricule, password);
			
			String urlToBe = loginPage.config.getProperties("url_MANAGER");
			loginPage.verifURLContenu(urlToBe);  
		}
		case "MEMBER" -> {
			String matricule =config.getProperties("member.matricule");
			String password =config.getProperties("member.password");
			loginPage.connexion(matricule, password);
			String urlToBe = loginPage.config.getProperties("url_MEMBER");
			loginPage.verifURLContenu(urlToBe);	
		
		}
		default -> throw new IllegalArgumentException("Rôle inconnu: " + role);
		}
		
	}

	@Then("je clique sur le menu utilsateur pour consulter mon profile")
	public void jeCliqueSurLeMenuUtilsateurPourConsulterMonProfile() {
		page.clickMenuUser();
	}

	@Then("je valide avec {string}")
	public void jeCliqueSur(String label) {
		page.clickByLabel(label);
	}

	@Then("un formulaire pré-rempli contenat mes informations personelles")
	public void unFormulairePréRempliContenatMesInformationsPersonelles() {
		String emailVal = page.email.getAttribute("value");
		Assertions.assertTrue(emailVal != null && !emailVal.isBlank(), "Le formulaire n'est pas pré-rempli (email vide).");
	}

	@Then("je modifie mes informations personelles:")
	public void jeJeModifieMesInformationsPersonelles(DataTable t) {
		Map<String, String> input = t.asMap(String.class, String.class);
		Map<String, String> m = GenerateRandomDataUsingFaker.apply(input); // support <auto>

		// Remplir les champs texte
		page.fillForm(m);
		if (m.containsKey("nom"))
			currentNom = m.get("nom");
		if (m.containsKey("prenom"))
			currentPrenom = m.get("prenom");
		if (m.containsKey("email"))
			currentEmail = m.get("email");

	}
	@Then("une alerte Succes dont le titre est {string} et le message est {string} s affiche")
	public void uneAlerteSuccesDontLeTitreEstEtLeMessageEstSAffiche(String titre, String message) {
		sweetAlert.assertAlert(titre, message, false);
	}

	@Then("je vois mes donnes changé avec :")
	public void jeVoisMesDonnesChangéAvec(DataTable t) {
		page.waitingForvisibiltyAfterChange();
		String actualNom = page.nom.getAttribute("value");
		String actualPrenom = page.prenom.getAttribute("value");
		String actualEmail = page.email.getAttribute("value");
		String actualImageSrc = page.uploadFile.getAttribute("src");

		if (currentNom != null)
			Assertions.assertEquals(currentNom, actualNom);
		if (currentPrenom != null)
			Assertions.assertEquals(currentPrenom, actualPrenom);
		if (currentEmail != null)
			Assertions.assertEquals(currentEmail, actualEmail);
		if (currentImage != null)
			Assertions.assertTrue(actualImageSrc != null && actualImageSrc.contains(currentImage),
					"L'avatar ne semble pas mis à jour (src: " + actualImageSrc + ")");
	}
	@Then("une alerte pour confirmer dont le titre est {string} et le message est {string} s affiche")
	public void confirmationAlerte(String title, String message) {
		sweetAlert.assertAlert(title, message, false);

	}
	@And("j annule la fermeture de la page courante")
	public void annulerSortirForUpdatePage() {
		sweetAlert.annuler();
		
	}
	
	@And("je souhaite voir le texte {string}")
	public void pageProfile(String text) {
		page.visibilityOfPageProfile();		
	}
	
	@Then("je suis redigé vers l espace {string} reservé pour chaque utilisateur")
	public void verifAfterLogin(String espace) {
		switch (espace) {
		case "Espace Administrateur" -> {
			Assertions.assertEquals(espace, loginPage.espaceAdmin.getText().trim());
		}
		case "Espace Chef de Projet" -> {
			
			Assertions.assertEquals(espace, loginPage.espaceManager.getText().trim());
		}
		case "Espace collaborateurs" -> {
			
			Assertions.assertEquals(espace, loginPage.espaceMember.getText().trim());
		}
		default -> throw new IllegalArgumentException("Rôle inattendu: " + espace);
		}
	}
	
	@Then("Error alerte avec {string} et {string} s affiche")
	public void errorAlerte(String title, String message) {
		sweetAlert.assertAlert(title, message, true);

	}

	@Then("le champ {string} est visible , non cliquable et non modifiable")
	public void verifChampMatriculeDisabled(String matricule) {
		
	     Assertions.assertTrue(
				page.matricule.isDisplayed() && !page.matricule.isEnabled() && !Validations.isEditable(page.matricule));
	}
	
	
	@And("je confirme la fermeture de la page courante")
	public void confirmerSortirForUpdatePage() {
		sweetAlert.valider();
	}
	

	
	
}
