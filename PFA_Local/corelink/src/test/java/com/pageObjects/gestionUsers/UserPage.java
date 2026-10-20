package com.pageObjects.gestionUsers;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;
import com.hooks.SetUp;
import com.utils.*;
import com.utils.actionsBot.ElementActions;
import com.utils.dataReader.PropertyReader;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import com.pageObjects.BasePage;

public class UserPage extends BasePage {

	public final PropertyReader config = new PropertyReader();
	private final ElementActions su = new ElementActions();
	private final Wait w = new Wait(SetUp.getDriver());

	public UserPage(WebDriver driver) {
		super(driver);
	}

	/* ===== Champs formulaire===== */
	@FindBy(how = How.NAME, using = "matricule")
	public WebElement matricule;
	@FindBy(how = How.NAME, using = "nom")
	public WebElement nom;
	@FindBy(how = How.NAME, using = "prenom")
	public WebElement prenom;
	@FindBy(how = How.NAME, using = "email")
	public WebElement email;
	@FindBy(how = How.NAME, using = "role")
	public WebElement role;
	@FindBy(how = How.NAME, using = "image")
	public WebElement uploadFile;
	@FindBy(how = How.XPATH, using = "//div[@class='input-group']/input[@class='form-control']")
	public WebElement uploadFileUpdate;

	/* ===== Boutons / éléments ===== */
	@FindBy(how = How.XPATH, using = "//ul[@id='accordionSidebar']//li[@class='nav-item']//span[contains(text(),'Gérer les Comptes')]")
	public WebElement gererCompteButton;

	@FindBy(how = How.XPATH, using = "//div[@class='bg-white py-2 collapse-inner rounded']//a[contains(text(),'Ajouter')]")
	public WebElement ajouterButton;

	@FindBy(how = How.CSS, using = ".btn.btn-primary.w-100")
	public WebElement creerUserButton;

	@FindBy(how = How.CSS, using = ".table.table-striped.table-hover.table-bordered.align-middle")
	public WebElement tableListUsers;

	@FindBy(how = How.XPATH, using = "//tr/td[1]")
	public WebElement msgNotFundUserForSearch;

	@FindBy(how = How.CSS, using = ".h3.mb-4.text-primary.fw-bold")
	public WebElement titlePageCreateUser;

	@FindBy(how = How.CSS, using = ".text-center.text-primary.fw-bold.mb-4")
	public WebElement titleCreatePage;

	@FindBy(how = How.CSS, using = ".text-center.text-primary.fw-bold.mb-4")
	public WebElement titlePageUpdateUser;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Liste')]")
	public WebElement listeButton;
	@FindBy(how = How.CSS, using = ".btn.btn-warning.px-4")
	public WebElement updateButton;
	@FindBy(how = How.CSS, using = ".btn.btn-secondary.px-4")
	public WebElement btnResetList;

	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Précédent')]")
	public WebElement btnPaginationPrecedent;
	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Suivant')]")
	public WebElement btnPaginationSuivant;
	@FindBy(how = How.XPATH, using = "//div[@class='d-flex justify-content-between align-items-center mt-3']/span")
	public WebElement numPageActuellePagination;

	@FindBy(how = How.XPATH, using = "//input[@placeholder='Rechercher par matricule, nom, prénom, email, rôle…']")
	public WebElement champsRecherche;

	/* ===== Saisies ===== */
	public void saisirMatricule(String v) {
		if (Validations.isEditable(matricule) && Validations.notBlank(v)) {
			su.writeText(matricule, Validations.nullToEmpty(v));
		}
	}

	public void saisirNom(String v) {
		su.writeText(nom, Validations.nullToEmpty(v));
	}

	public void saisirPrenom(String v) {
		su.writeText(prenom, Validations.nullToEmpty(v));
	}

	public void saisirEmail(String v) {
		su.writeText(email, Validations.nullToEmpty(v));
	}

	public void selectionnerRole(String v) {
		if (Validations.notBlank(v))
			new Select(role).selectByValue(v.toUpperCase());
	}

	public void choisirFichier(String nomImage) {
		if (!Validations.notBlank(nomImage))
			return;
		String chemin = Paths.get("src", "test", "resources", "uploads", nomImage).toAbsolutePath().toString();
		try {
			if (uploadFile.isEnabled()) {
				uploadFile.sendKeys(chemin);
				return;
			}
		} catch (Exception ignored) {
			System.out.println("ignored");
		}
		try {
			if (uploadFileUpdate.isEnabled()) {
				uploadFileUpdate.sendKeys(chemin);
				return;
			}
		} catch (Exception ignored) {
			System.out.println("ignored");
		}

	}

	/* ===== Formulaire ===== */
	public void fillForm(Map<String, String> m) {
		if (m == null)
			return;
		if (m.containsKey("matricule"))
			saisirMatricule(m.get("matricule"));
		if (m.containsKey("nom"))
			saisirNom(m.get("nom"));
		if (m.containsKey("prenom"))
			saisirPrenom(m.get("prenom"));
		if (m.containsKey("email"))
			saisirEmail(m.get("email"));

		String pass = Validations.firstNonNull(m.get("password"), m.get("motdepasse"));
		if (Validations.notBlank(pass)) {
			List<WebElement> mdpFields = SetUp.getDriver().findElements(By.name("motDePasse"));
			if (!mdpFields.isEmpty()) {
				WebElement mdp = mdpFields.get(0);
				if (Validations.isEditable(mdp)) {
					mdp.clear();
					mdp.sendKeys(pass);
				}
			}
		}

		if (m.containsKey("role"))
			selectionnerRole(m.get("role"));
		if (m.containsKey("image"))
			choisirFichier(m.get("image"));
	}

	/* ===== Clics ===== */
	public void clickAjouter() {
		su.click(ajouterButton);
	}

	public void clickGererComptes() {
		su.click(gererCompteButton);
	}

	public void clickCreerUser() {
		su.scrollIntoView(creerUserButton);
		su.clickOnElementUsingJs(creerUserButton);
	}

	public void clickListe() {
		su.click(listeButton);
	}

	public void clickUpdate() {
		su.scrollIntoView(updateButton);
		su.clickOnElementUsingJs(updateButton);
	}

	public void clickPrecedent() {
		su.click(btnPaginationPrecedent);
	}

	public void clickSuivant() {
		su.click(btnPaginationSuivant);
	}

	public void clickRevenirList() {
		su.click(btnResetList);
	}

	public void clickByLabel(String label) {
		String l = Validations.norm(label);
		if (l.contains("gerer les comptes")) {
			clickGererComptes();
			return;
		}
		if (l.contains("ajouter")) {
			clickAjouter();
			return;
		}
		if (l.contains("creer utilisateur") || l.equals("creer")) {
			clickCreerUser();
			return;
		}
		if (l.contains("liste")) {
			clickListe();
			return;
		}
		if (l.contains("mettre a jour") || l.contains("modifier")) {
			clickUpdate();
			return;
		}
		if (l.contains("precedent")) {
			clickPrecedent();
			return;
		}
		if (l.contains("suivant")) {
			clickSuivant();
			return;
		}
		ElementActions.waitFor(5);
		throw new IllegalArgumentException("Bouton non géré: " + label);
	}

	/* ===== Tableau & pagination ===== */
	public int rowCount() {
		return tableListUsers.findElements(By.cssSelector("tbody tr")).size();
	}

	/* ===== Utilitaires ===== */
	public WebElement getBoutonActionParMatricule(String matricule, String action) {
		String classSelector;
		switch (action) {
		case "modifier":
			classSelector = "btn btn-outline-warning btn-sm";
			break;
		case "supprimer":
			classSelector = "btn btn-outline-danger btn-sm ms-2";
			break;
		default:
			throw new IllegalArgumentException("Action inconnue : " + action);
		}
		String xpath = "//tr[td[normalize-space(text())='" + matricule + "']]//button[contains(@class, '"
				+ classSelector + "')]";
		return SetUp.getDriver().findElement(By.xpath(xpath));
	}

	public boolean utilisateurExisteDansListe(String matricule) {
		String xpath = "//table//tr[td[normalize-space(text())='" + matricule + "']]";
		return SetUp.getDriver().findElements(By.xpath(xpath)).size() > 0;
	}

	// hedhi bch tchouf est ce que f tableau users , les informations mte3 user x
	// eli sna3tou/badaltou nafshom eli 7atythom ena ou non
	public void assertRowContains(String matricule, Map<String, String> expected) {
		WebElement row = getBoutonActionParMatricule(matricule, "modifier").findElement(By.xpath("./ancestor::tr"));

		for (Map.Entry<String, String> e : expected.entrySet()) {
			String key = e.getKey().toLowerCase(Locale.ROOT);
			String val = e.getValue();
			if (val == null || val.isEmpty())
				continue;

			if ("matricule".equals(key))
				continue; // déjà utilisé pour trouver la ligne

			if ("image".equals(key)) {
				// On cherche <img> dans la ligne et on compare src
				WebElement img = row.findElement(By.tagName("img"));
				String src = img.getAttribute("src");
				Assertions.assertTrue(src.contains(val.replace(" ", "%20")) // gérer espaces encodés
				);
			} else {
				// Vérification classique du texte de la ligne
				Assertions.assertTrue(row.getText().contains(val));
			}
		}
	}

	public void appliquerFiltre(String query) {
		champsRecherche.click();
		su.writeText(champsRecherche, query.toLowerCase());
	}

	public boolean trySelectByFilter(String matricule, String action) {
		try {
			appliquerFiltre(matricule);
			ElementActions.waitFor(10);
			su.scrollBy(0,110);
			getBoutonActionParMatricule(matricule, action).click();
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	public boolean trySelectByFilterV2(String matricule) {
		try {
			appliquerFiltre(matricule);
			return true;
		} catch (Exception ignored) {
			return false;
		}
	}

	public String firstRowMatricule() {
		verifVisibilityOfTableUser();
		WebElement cell = SetUp.getDriver().findElement(By.cssSelector("table tbody tr:first-child td:first-child"));
		return cell.getText().trim();
	}

	/** visibility **/

	// create user
	public void verifVisibilityOfTitleCreatePage() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), titleCreatePage, "titre creation user");

	}

	public void verifVisibilityOfTableUser() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), tableListUsers, "user table");
		ElementActions.waitFor(10);

	}

	// archivage user
	public void verifVisibilityOfButtonGererCompte() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), gererCompteButton, "gerre compte button");

	}

	public void verifVisibilityOfAddBuuton() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), ajouterButton, "ajouter button");

	}

	public void verifVisibilityOfTitleListeUser() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), titlePageCreateUser, "page liste user");

	}

	// search & pagination
	public boolean verifyIfPrecdentButtonNotClickable() {
		if (!btnPaginationPrecedent.isEnabled())
			return true;
		return false;
	}

	public boolean verifyIfSuivantButtonNotClickable() {
		if (!btnPaginationSuivant.isEnabled())
			return true;
		return false;
	}

	public boolean verifyIfPrecdentButtonClickable() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), btnPaginationPrecedent, "page liste user");
		if (btnPaginationPrecedent.isEnabled())
			return true;
		return false;
	}

	public boolean verifyIfSuivantButtonClickable() {
		if (btnPaginationSuivant.isEnabled())
			return true;
		return false;
	}

	public boolean verifIfPrecdentButtonAndSuivantBuutonIsClickable() {
		if (btnPaginationPrecedent.isEnabled() || btnPaginationSuivant.isEnabled())
			return true;
		return false;

	}

	// update user
	public void verifVisibilityOfMatricule() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), matricule, "matricule button");

	}

	public void verifVisibilityOfTitleUpdatePage() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), titlePageUpdateUser, "titre creation user");

	}

	// verif formulaire pré rempli
	public void verifFormUpdateRempli() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), email, "email");
		if (!email.getAttribute("value").isBlank()) {
			return;
		}
	}

}
