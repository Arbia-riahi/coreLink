package com.stepDefinition.gestionUsers;

import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionUsers.UserPage;
import com.utils.GenerateRandomDataUsingFaker;
import com.pageObjects.components.SweetAlerts;
import com.hooks.SetUp;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import java.util.HashMap;
import java.util.Map;

public class CreateUserSteps {

    private final UserPage userPage = new UserPage(SetUp.getDriver());
    private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
    private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());
    

    private String currentMatricule, currentEmail, currentNom, currentPrenom, currentImage;

    @Given("je me connecte en tant que admin")
    public void connexionAdmin() {
        String matricule = userPage.config.getProperties("admin.matricule");
        String password  = userPage.config.getProperties("admin.password");
        loginPage.connexion(matricule, password);
        
        String urlToBe = userPage.config.getProperties("url_ADMIN");
		loginPage.verifURLContenu(urlToBe);
       
    }

    @When("j appui sur {string}")
    public void JeCliqueSur(String label) {
    	userPage.clickByLabel(label);
    }

    @And("le formulaire de création s ouvre avec le titre {string}")
    public void formCreationOpen(String expectedTitle) { 
    	userPage.verifVisibilityOfTitleCreatePage();
    	String obtenuTitle=userPage.titleCreatePage.getText();
    	Assertions.assertEquals(obtenuTitle, expectedTitle);
    }

    @And("je saisis les informations d utilisateur :")
    public void saisirInfos(DataTable t) {
    	Map<String, String> input = t.asMap(String.class, String.class);
    	Map<String, String> m = GenerateRandomDataUsingFaker.apply(input);

    	userPage.fillForm(m);

    	// mémorise pour assertions
    	currentMatricule = m.get("matricule");
    	currentEmail     = m.get("email");
    	currentNom       = m.get("nom");
    	currentPrenom    = m.get("prenom");

    }

    @Then("SweetAlert de succès avec le titre {string} et le message {string} s affiche sur la page de création de l utilisateur")
    public void assertSucces(String titre, String message) {
       sweetAlert.assertAlert(titre, message, false);  
      }

    @Then("je suis envoyé vers la liste des utilisateurs et je vois utilisateur avec :")
    public void assertUserInList(DataTable t) {
    	userPage.verifVisibilityOfTableUser();
        Map<String, String> expected = new HashMap<>(t.asMap(String.class, String.class));

        replaceIfPlaceholder(expected, "matricule", currentMatricule);
        replaceIfPlaceholder(expected, "email",     currentEmail);
        replaceIfPlaceholder(expected, "nom",       currentNom);
        replaceIfPlaceholder(expected, "prenom",    currentPrenom);
        replaceIfPlaceholder(expected, "image",     currentImage); // <-- NEW

        userPage.assertRowContains(currentMatricule, expected);
    }

    @Then("SweetAlert d erreur avec le titre {string} et le message {string} s affiche sur la page de création de l utilisateur")
    public void assertErreur(String titre, String message) {
    	 sweetAlert.assertAlert(titre, message, true); 
    }

    @Then("le bouton {string} est désactivé sur le formulaire de création")
    public void boutonDisabled(String label) {
        Assertions.assertFalse(userPage.creerUserButton.isEnabled());
    }

    // --- helper court ---
    private void replaceIfPlaceholder(Map<String, String> map, String key, String actual) {
        if (!map.containsKey(key)) return;
        if (GenerateRandomDataUsingFaker.isPlaceholder(map.get(key))) map.put(key, actual);
    }

   

}
