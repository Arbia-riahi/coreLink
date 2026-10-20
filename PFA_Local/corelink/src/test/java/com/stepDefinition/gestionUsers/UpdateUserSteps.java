package com.stepDefinition.gestionUsers;

import com.pageObjects.authentification.LoginPage;
import com.pageObjects.gestionUsers.UserPage;
import com.utils.GenerateRandomDataUsingFaker;
import com.utils.Validations;
import com.hooks.SetUp;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import java.util.*;

import com.api.*;
import com.facadeComponent.SweetAlerts;


public class UpdateUserSteps {

    private final UserPage userPage = new UserPage(SetUp.getDriver());
    private final LoginPage loginPage = new LoginPage(SetUp.getDriver());
    private final SweetAlerts sweetAlert = new SweetAlerts(SetUp.getDriver());
    private String currentMatricule, currentEmail, currentNom, currentPrenom, currentImage;

    @Given("je suis connecté en tant qu administrateur")
    public void connexionAdmin() {
    	String matricule = userPage.config.getProperties("admin.matricule");
        String password  = userPage.config.getProperties("admin.password");
        loginPage.connexion(matricule, password);
        
        String urlToBe = userPage.config.getProperties("url_ADMIN");
		loginPage.verifURLContenu(urlToBe);
    }

    @Given("un utilisateur actif auto-cree")
    
    public void seedAutoUser() {
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
         currentNom       = m.get("nom");
         currentPrenom    = m.get("prenom");
         
        
    }

   
	@And("je sélectionne l utilisateur courant et je clique sur l icône {string}")
    public void selectCurrentAndClickIcon(String action) {
		userPage.verifVisibilityOfButtonGererCompte();
	    userPage.clickGererComptes();
	    
		userPage.verifVisibilityOfAddBuuton();
		userPage.clickListe();
		
        if (userPage.trySelectByFilter(currentMatricule, action)) return;
        try { userPage.appliquerFiltre(""); } catch (Exception ignored) {
        	System.out.println("ignored");
        }
        Assertions.fail("Utilisateur '" + currentMatricule + "' introuvable pour action '" + action + "'.");
    }

    @And("je modifie les informations d utilisateur :")
    public void modifierInfos(DataTable t) {
        Map<String, String> input = t.asMap(String.class, String.class);
        Map<String, String> m = GenerateRandomDataUsingFaker.apply(input); 

        userPage.fillForm(m);

        // mettre à jour l'état courant selon les champs présents
        if (m.containsKey("email"))  currentEmail  = m.get("email");
        if (m.containsKey("nom"))    currentNom    = m.get("nom");
        if (m.containsKey("prenom")) currentPrenom = m.get("prenom");
    }

    @And("je confirme la modification")
    public void modifierClicK() { 
        sweetAlert.valider();
    }
    @And("je souhaite revenir a la liste")
    public void revenirListe() { 
    	userPage.clickRevenirList();    
    }
    
    @And("je souhaite quitter la page")
    public void QuitterPage() { 
        sweetAlert.valider();  
    }
    
    @And("je souhaite rester sur la page actuelle")
    public void resterPageActuelle() { 
        sweetAlert.annuler();  
    }
    
    
    
    @And("je confirme la modification en cliquant sur {string}")
    public void updateUser(String bouton) { 
    	userPage.clickByLabel("Mettre à jour");
    }
   

    @Then("Succes Alerte avec le titre {string} et le message {string} s affiche")
    public void assertSuccesUpdate(String titre, String message) {
    	sweetAlert.assertAlert(titre, message, false);
    }

    @Then("confirmation alerte avec le titre {string} et le message {string} s affiche")
    public void assertConfirmation(String titre, String message) {
    	sweetAlert.assertAlert(titre, message, false);
    }

    @Then("Error alerte avec le titre {string} et le message {string} s affiche")
    public void assertError(String titre, String message) {
    	sweetAlert.assertAlert(titre, message, true);
    }

    @Then("le champ {string} est désactivé et non modifiable")
    public void champNonModifiable(String champ) { 
    	userPage.verifVisibilityOfMatricule();
    	
    	//hné 7atina getAtt() car getText() ne lit rien, car il n’y a aucun texte entre les balises
    	champ=userPage.matricule.getDomProperty("value");
    	Assertions.assertEquals(currentMatricule, champ);
    	
        Assertions.assertFalse(Validations.isEditable(userPage.matricule));
    }

    @And("le formulaire de modification s ouvre pré-rempli")
    public void formUpdateOpenedPrefilled() {
        userPage.verifFormUpdateRempli();    }

    @And ("je suis redirigé vers la liste des utilisateurs")
    public void redirection() {
    	userPage.verifVisibilityOfTableUser();
    	
    }
    
    @Then ("je souhaite voir le titre de la page {string}")
    public void verifTitleOfPageUpdate(String expectedTitle) {
    	userPage.verifVisibilityOfTitleUpdatePage();
    	String obtenu =userPage.titlePageUpdateUser.getText();
    	Assertions.assertEquals(expectedTitle,obtenu);
    	
    }
    @And("je souhaite voir les memes informations :")
    public void verifyUserOnList(DataTable t) {
    	Map<String, String> expected = new HashMap<>(t.asMap(String.class, String.class));
        // si l’assertion contient <auto>, remplace par la vraie valeur courante
        replaceIfPlaceholder(expected, "matricule", currentMatricule);
        replaceIfPlaceholder(expected, "email",     currentEmail);
        replaceIfPlaceholder(expected, "nom",       currentNom);
        replaceIfPlaceholder(expected, "prenom",    currentPrenom);
        replaceIfPlaceholder(expected, "image",     currentImage);
        userPage.assertRowContains(currentMatricule, expected);
    }
    
   @And("je souhaite chercher l utilisateur")
   public void chercherUser() {
	   userPage.trySelectByFilterV2(currentMatricule);
	   
   }

    @Then("je vois l utilisateur courant dans la liste")
    public void seeCurrentInList() {
        Assertions.assertTrue(
            userPage.utilisateurExisteDansListe(currentMatricule)
        );
    }

    // --- helpers courts ---
    private void replaceIfPlaceholder(Map<String, String> map, String key, String actual) {
        if (!map.containsKey(key)) return;
        if (GenerateRandomDataUsingFaker.isPlaceholder(map.get(key))) map.put(key, actual);
    }
   
}
