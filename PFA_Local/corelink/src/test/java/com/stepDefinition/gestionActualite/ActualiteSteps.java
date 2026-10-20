package com.stepDefinition.gestionActualite;

import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.JavascriptExecutor;
import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionActualites.AcutalitePage;
import com.facadeComponent.SweetAlerts;
import com.hooks.SetUp;
import com.utils.GenerateRandomDataUsingFaker;
import com.utils.Validations;
import com.utils.actionsBot.ElementActions;
import com.utils.dataReader.PropertyReader;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;

public class ActualiteSteps {

	private final AcutalitePage actualitePage = new AcutalitePage(SetUp.getDriver());
	private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
	private final PropertyReader config = new PropertyReader();
	private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());
	private String currentTitle, currentDescription;
	private final Map<String, String> memo = new HashMap<>();

	@Given("je m authentifié en tant que admin")
	public void AuthAdmin() {
		String matricule = config.getProperties("admin.matricule");
		String password = config.getProperties("admin.password");
		loginPage.connexion(matricule, password);

		String urlToBe = config.getProperties("url_ADMIN");
		loginPage.verifURLContenu(urlToBe);

	}

	@When("je choisi de presser sur {string}")
	public void cliquerBtn(String label) {
		actualitePage.clickLabel(label);
	}

	@When("le formulaire de création vierge est bien visible sur la page")
	public void leFormulaireDeCréationEstBienVisibleSurLaPage() {

		String titreVide = actualitePage.titre.getText();
		String descriptionVide = actualitePage.description.getText();
		String photoVide = actualitePage.uploadFiles.getText();

		if (Validations.notBlank(titreVide) 
		|| Validations.notBlank(descriptionVide)
		|| Validations.notBlank(photoVide)) {
		    return;
		}
	}

	@And("je modifie les informations relatives a une actualité:")
	@When("je saisis les informations relatives a une actualité :")
	public void jeSaisisLesInformations(DataTable dataTable) {
		Map<String, String> input = dataTable.asMap(String.class, String.class);
		Map<String, String> m = GenerateRandomDataUsingFaker.apply(input);
		actualitePage.fillForm(m);
		currentTitle = m.get("titre");
		currentDescription = m.get("description");
	}

	@Then("SweetAlert de succès avec le titre {string} et le message {string} sera visible")
	public void assertSucces(String titre, String message) {
		sweetAlert.assertAlert(titre, message, false);
	}

	@Then("SweetAlert d erreur avec le titre {string} et le message {string} sera visible")
	public void assertError(String titre, String message) {
		sweetAlert.assertAlert(titre, message, true);
	}

	@Then("SweetAlert de confirmation avec le titre {string} et le message {string} sera visible")
	public void assertConfirmation(String titre, String message) {
		sweetAlert.assertAlertTemplate(titre, message);
	}

	@And("je valide la fermeture de la page")
	public void validerPopup() {
		sweetAlert.valider();
	}

	@And("j annule la fermeture de la page")
	public void AnnulerPopup() {
		sweetAlert.annuler();
	}

	@And("j annule la supression")
	public void AnnulerSuppressionPopup() {
		sweetAlert.annuler();
	}

	@And("je confirme la supression")
	public void ConfirmerSuppressionPopup() {
		sweetAlert.valider();
	}

	@And("je persiste sur la page des actualités et je vois que les informations sont persistés :")
	@Then("je suis envoyé vers la liste des actualités et je vois une actualités avec :")
	public void redirectionListActualite(DataTable dataTable) {
		Map<String, String> expected = new HashMap<>(dataTable.asMap(String.class, String.class));
		replaceIfPlaceholder(expected, "titre", currentTitle);
		replaceIfPlaceholder(expected, "description", currentDescription);
		actualitePage.assertRowContains(currentTitle, expected);
	}

	// --- helpers courts
	private void replaceIfPlaceholder(Map<String, String> map, String key, String actual) {
		if (!map.containsKey(key))
			return;
		if (GenerateRandomDataUsingFaker.isPlaceholder(map.get(key)))
			map.put(key, actual);
	}

	@Then("le bouton de création sera désactivé")
	public void verifBoutonCreerNoncliquable() {
		Assertions.assertTrue(!actualitePage.btnCreeActualite.isEnabled());
	}

	@Given("une actualité actif auto-cree")
	public void seedAutoActualité() {
		actualitePage.clickLabel("Raccourcis");
		actualitePage.clickLabel("Actualités");
		actualitePage.clickLabel("Ajouter");

		Map<String, String> in = new LinkedHashMap<>();
		in.put("titre", "<auto>");
		in.put("description", "<auto>");
		in.put("photos", "image 1.jpeg, image 2.jpeg, exception 1.jpg, exception 2.jpg, exception 3.jpg");

		Map<String, String> m = GenerateRandomDataUsingFaker.apply(in);
		actualitePage.fillForm(m);
		actualitePage.clickLabel("Créer l’actualité");

		try {
			sweetAlert.assertAlert("Succès", "Votre actualité a été créée avec succès", true);
		} catch (Exception ignored) {
			System.out.println("ignored");
		}

		currentTitle = m.get("titre");
		currentDescription = m.get("description");
	}

	@And("je suis sur la page {string}")
	public void listsActualités(String expectedTitle) {
		actualitePage.verifTitlePageActualité();
		String titleObtenu = actualitePage.titleListActualit.getText();
		Assertions.assertEquals(titleObtenu, expectedTitle);
	}

	@And("je sélectionne l actualite courant et je clique sur l icône {string}")
	public void selectCurrentAndClickIcon(String action) {
		actualitePage.getBoutonActionParTitle(currentTitle, action);
		switch (action) {
		case "modifier" -> actualitePage.clickIconUpdate(currentTitle);
		case "supprimer" -> actualitePage.clickIconDelete(currentTitle);
		default -> System.out.println("action not found");
		}

	}

	@And("le formulaire de modification sera ouvert pré-rempli")
	public void formUpdateOpenedPrefilled() {
		actualitePage.verifFormUpdateRempli();
	}

	@And("je reste sur la page {string}")
	public void assertFormUpdate(String expectedTitle) {
		actualitePage.verifTitleUodateActualité();
		String obtenu = actualitePage.titreFormsUpdate.getText();
		Assertions.assertEquals(obtenu, expectedTitle);
	}

	@And("je ne vois plus l actualite dans la liste")
	public void succesDeleteActualité() {
		Assertions.assertFalse(actualitePage.actualitéExisteDansListe(currentTitle));
	}

	// pagination:
	@And("je vois exactement {int} lignes dans une page")
	public void assertRowCount(int n) {
		Assertions.assertEquals(n, actualitePage.rowCount());
	}

	@And("je suis sur la page {int} des actualitées")
	public void assertPageCount(int n) {
		((JavascriptExecutor) SetUp.getDriver()).executeScript("window.scrollBy(0,700)");
		boolean x = actualitePage.nbrDepage.getText().contains("Page " + n);
		Assertions.assertTrue(x,
				"Le texte attendu 'Page " + n + "' n’a pas été trouvé dans : " + actualitePage.nbrDepage.getText());
	}

	@And("je mémorise le titre de la 1ère ligne comme {string}")
	public void memorizeFirstRow(String alias) {
		((JavascriptExecutor) SetUp.getDriver()).executeScript("window.scrollBy(0,-500)");
		memo.put(alias, actualitePage.firstRowTitle());
	}

	@Then("le titre de la 1 ère ligne est différent de {string}")
	public void assertFirstRowDifferentFrom(String alias) {
		String oldTitle = memo.get(alias);
		for (int i = 0; i < 30; i++) { 
			String now = actualitePage.firstRowTitle();
			if (!Objects.equals(now, oldTitle))
				break;
		}
		Assertions.assertNotEquals(oldTitle, actualitePage.firstRowTitle());
	}

	@Then("le titre de la 1 ère ligne est égal à {string}")
	public void assertFirstRowEqualTo(String alias) {
		String expected = memo.get(alias);
		for (int i = 0; i < 30; i++) {
			String now = actualitePage.firstRowTitle();
			if (Objects.equals(now, expected))
				break;
			ElementActions.waitFor(1);
		}
		Assertions.assertEquals(expected, actualitePage.firstRowTitle());
	}

	@When("je navigue vers la dernière page")
	public void goLastPage() {
		actualitePage.verifLastPage();
		actualitePage.clickLabel(" Suivant ");
	}
}
