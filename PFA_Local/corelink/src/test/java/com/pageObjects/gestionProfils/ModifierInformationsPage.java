package com.pageObjects.gestionProfils;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.pageObjects.BasePage;
import com.utils.Validations;
import com.utils.Wait;
import com.utils.actionsBot.ElementActions;
import com.utils.dataReader.PropertyReader;

public class ModifierInformationsPage extends BasePage {
	public PropertyReader config;
	private final ElementActions su;
	private final Wait w;

	public ModifierInformationsPage(WebDriver driver) {
        super(driver);
		this.config = new PropertyReader();
		su = new ElementActions();
		this.w = new Wait(driver);
	}

	/* ===== Champs formulaire ===== */
	@FindBy(how = How.CSS, using = ".form-control.form-control-readonly")
	public WebElement matricule;

	@FindBy(how = How.NAME, using = "nom")
	public WebElement nom;

	@FindBy(how = How.NAME, using = "prenom")
	public WebElement prenom;

	@FindBy(how = How.NAME, using = "email")
	public WebElement email;
	
	@FindBy(how = How.XPATH, using = "//div[@class='avatar position-relative d-inline-block mb-3']/button[@class='avatar__btn']")
	public WebElement uploadFile;

	@FindBy(how = How.XPATH, using = "//input[@class='d-none']")
	public WebElement inputUploadFile;

	/* ===== Boutons ===== */
	@FindBy(how = How.CSS, using = ".btn.btn-primary.btn-lg")
	public WebElement btnModifierProfile;

	@FindBy(how = How.XPATH, using = "//a[@id='userDropdown']/div[@class='user-container']")
	public WebElement btnmenuUser;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Mon profil')]")
	public WebElement btnMonProfil;
	
	@FindBy(how = How.XPATH, using = "//h3[contains(text(),'Mon profil ')]")
	public WebElement monProfilText;



	public void saisirNom(String n) {
		su.writeText(nom, Validations.nullToEmpty(n));
	}

	public void saisirPrenom(String p) {
		su.writeText(prenom, Validations.nullToEmpty(p));

	}

	public void saisirEmail(String e) {
		su.writeText(email, Validations.nullToEmpty(e));
	}

	public void choisirFichier(String nomImage) {
		String chemin = Paths.get("src", "test", "resources", "uploads", nomImage).toAbsolutePath().toString();
		inputUploadFile.sendKeys(chemin);

	}

	public void fillForm(Map<String, String> m) {
		if (m == null)
			return;
		if (m.containsKey("nom"))
			saisirNom(m.get("nom"));
		if (m.containsKey("prenom"))
			saisirPrenom(m.get("prenom"));
		if (m.containsKey("email"))
			saisirEmail(m.get("email"));
		if (m.containsKey("image"))
			choisirFichier(m.get("image"));

	}

	public void clickMenuUser() {su.click(btnmenuUser);}

	public void clickByLabel(String rawLabel) {
		String label = Validations.nullToEmpty(rawLabel).trim();
		if (label.isEmpty() || "[empty]".equalsIgnoreCase(label))
			return;
		switch (label) {
		case "Mon Profile" -> {su.click(btnMonProfil); ElementActions.waitFor(20);}
		case "Mettre à jour le profil" -> su.click(btnModifierProfile);
		default -> throw new IllegalArgumentException("Bouton non géré: " + label);
		};
		
	}
	
	public void visibilityOfPageProfile() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), monProfilText, "text");
	}
	public void waitingForvisibiltyAfterChange() {
		ElementActions.waitFor(15);
	}
	
	
	
	
}
