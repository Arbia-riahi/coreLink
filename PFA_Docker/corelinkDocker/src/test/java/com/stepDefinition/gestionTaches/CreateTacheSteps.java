package com.stepDefinition.gestionTaches;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import com.hooks.SetUp;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.components.SweetAlerts;
import com.pageObjects.gestionTaches.CreateTachePage;
import com.utils.GenerateRandomDataUsingFaker;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateTacheSteps {

	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final CreateTachePage createTachePage = new CreateTachePage(SetUp.getDriver());
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());

	private String currentTitre, currentPriority, currentEtat, currentMembers;

	@Given("je suis un manager")
	public void jeSuisConnectéEnTantQueManager() {
		String matricule = loginPage.config.getProperties("manager.matricule");
		String password = loginPage.config.getProperties("manager.password");
		loginPage.connexion(matricule, password);

		String urlToBe = loginPage.config.getProperties("url_MANAGER");
		loginPage.verifURLContenu(urlToBe);
	}

	@When("je valide {string}")
	public void jAppuieSurLaTouche(String label) {
		createTachePage.clickLabel(label);
	}

	@And("je clique sur icon de gestion de taches pour le projet {string}")
	public void jeChercheUneTache(String label) {
		createTachePage.filter(label);
		createTachePage.getBoutonActionParTitleAndClick(label);
	}

	@And("j exécute la fonctionnalité {string}")
	public void clickBtns(String label) {
		createTachePage.clickLabel(label);
	}

	@When("je clique sur l’icône de création dans la colonne {string}")
	public void clickAjoutKanban(String etat) {
		createTachePage.clickAddKanban(etat);

	}

	@And("je saisis les informations de la tâche:")
	public void remplirForm(DataTable dataTable) {
		Map<String, String> m = GenerateRandomDataUsingFaker.apply(dataTable.asMap(String.class, String.class));
		createTachePage.fillForm(m);

		currentTitre = m.getOrDefault("titre", "");
		currentPriority = m.getOrDefault("priorité", "");
		currentEtat = m.getOrDefault("etat", "");
		currentMembers = m.getOrDefault("membres", "");

	}

	@Then("un message {string} s’affiche avec le texte {string}")
	public void assertSucces(String titre, String message) {
		sweetAlert.assertAlert(titre, message, true);
	}

	@Then("je suis envoyé vers la liste des taches et je vois la tache crée avec:")
	public void ListeTableTaches(DataTable dataTable) {
		Map<String, String> expected = new HashMap<>(dataTable.asMap(String.class, String.class));

		// Remplacer les placeholders
		createTachePage.replaceIfPlaceholder(expected, "titre", currentTitre);
		createTachePage.replaceIfPlaceholder(expected, "etat", currentEtat);

		// Filtrer la tâche par titre
		createTachePage.filterByTitle(currentTitre);

		// Vérifier la couleur du drapeau (c'est ici qu'on teste la couleur)
		createTachePage.assertFlags(currentPriority);

		// ❗️Retirer "flag-color" car il n’est pas affiché en texte dans la table HTML
		expected.remove("flag-color");

		// Vérifier les autres colonnes visibles (titre, état, date, membres)
		createTachePage.assertRowContains(currentTitre, expected);
	}

	@And("je filtre l'etat {string} correspondant à la tâche crée")
	public void jeChercheUneEtat(String label) {
		createTachePage.filtrerByEtat(label);
	}

	@Then("je vois la même tâche affichée avec :")
	public void assertAvecKanban(DataTable dataTable) {
		Map<String, String> expected = new HashMap<>(dataTable.asMap(String.class, String.class));

		// Remplacer les placeholders
		createTachePage.replaceIfPlaceholder(expected, "titre", currentTitre);

		// Vérifier la couleur du drapeau (c'est ici qu'on teste la couleur)
		createTachePage.assertFlagsForKanban(currentPriority);

		// ❗️Retirer "flag-color" car il n’est pas affiché en texte dans la table HTML
		expected.remove("flag-color");

		createTachePage.assertKanbanContains(currentEtat, expected);
	}

	@Then("je suis redirigé vers la vue Kanban des tâches dont la bouton {string} est visible")
	public void verifVueKanbanDisabled(String btn) {
		String expected = createTachePage.getBtnVueTableau().getText();
		Assertions.assertEquals(btn, expected);

	}

}
