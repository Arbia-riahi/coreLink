package com.pageObjects.gestionProjets;

import java.nio.file.Paths;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.hooks.SetUp;
import com.utils.*;
import com.dataReader.PropertyReader;
import com.pageObjects.BasePage;
import com.actions.*;

public class UpdateProjectPage extends BasePage {

	public PropertyReader config;
	private final ElementActions su;
	private final Wait w;

	public UpdateProjectPage(WebDriver driver) {
		super(driver);
		this.config = new PropertyReader();
		this.su = new ElementActions();
		this.w = new Wait(driver);
	}

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Pilotage des projets')]")
	public WebElement pilotageBtn;
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Ajouter')]")
	public WebElement ajouterBtn;
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Liste')]")
	public WebElement listeBtn;
	
	@FindBy(how = How.NAME, using = "titre")
	public WebElement titre;
	@FindBy(how = How.NAME, using = "description")
	public WebElement description;
	@FindBy(how = How.NAME, using = "dateDebut")
	public WebElement dateDebut;
	@FindBy(how = How.NAME, using = "dateFin")
	public WebElement dateFin;

	@FindBy(how = How.CSS, using = ".badge.px-3.py-2.status-chip.chip-waiting")
	public WebElement enAttenteBtn;
	@FindBy(how = How.CSS, using = ".badge.px-3.py-2.status-chip.chip-waiting")
	public WebElement enAttenteBtnForUpdateProject;
	@FindBy(how = How.CSS, using = ".badge.px-3.py-2.status-chip.chip-progress")
	public WebElement enCoursBtn;
	@FindBy(how = How.CSS, using = ".badge.px-3.py-2.status-chip.chip-done")
	public WebElement termineBtn;
	@FindBy(how = How.CSS, using = ".badge.px-3.py-2.status-chip.chip-cancel")
	public WebElement annuleBtn;

	@FindBy(xpath = "//input[@type='checkbox']")
	public List<WebElement> checkboxes;

	
	@FindBy(how = How.CSS, using = ".hidden-input")
	public WebElement uploadFiles;
	@FindBy(how = How.CSS, using = ".btn.btn-primary.btn-lg.shadow-sm") 
	public WebElement btnCreerProjet;
	@FindBy(how = How.CSS, using = ".btn.btn-warning.w-100.shadow-sm")
	public WebElement btnModifierProjet;
    
	@FindBy(how = How.XPATH, using = "//input[@placeholder='Rechercher par titre, description, chef, statut...']")
	public WebElement searchZone;

	@FindBy(how = How.XPATH, using = "//div[@class='user-container']/span")
	public WebElement nomResponsableExcpectedInTable;
	@FindBy(how = How.XPATH, using = "//p[@class='text-muted mb-0']")
	public WebElement nomResponsableExcpectedInDetails;

	@FindBy(how = How.CSS, using = ".form-text.text-danger.mt-2")
	public WebElement msgErreurDateDebutForUpdate;
	@FindBy(how = How.CSS, using = ".form-text.text-danger.mt-1")
	public WebElement msgErreurDateFinForUpdate;

	@FindBy(how = How.CSS, using = ".text-center.text-muted.py-4")
	public WebElement msgErreurAucunFiltreResult;
	
	@FindBy(how = How.CSS, using = ".project-title.m-0")
	public WebElement titleOfProjectDetailsPage;
	

	public void saisirTitre(String v) {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), titre, "titre de projet");
		su.writeText(titre, Validations.nullToEmpty(v));
	}

	public void saisirDescription(String v) {
		su.writeText(description, Validations.nullToEmpty(v));
	}

	public void saisirDateDebut(String v) {

		su.writeText(dateDebut, Validations.nullToEmpty(v));
	}

	public void saisirDateFin(String v) {
    su.writeText(dateFin, Validations.nullToEmpty(v));
	}

	public void selectMemberByLabelForUpdate(String label) {
		String xp = "//div[@class='form-check' and normalize-space(.)='" + label + "']/input[@type='checkbox']";
		su.findElement(By.xpath(xp)).click();
	}


	public void selectMembersForUpdate(String csv) {
		if (csv == null || csv.isBlank())
			return;
		Arrays.stream(csv.split(",")).map(String::trim).filter(v -> !v.isEmpty())
				.forEach(this::selectMemberByLabelForUpdate);
	}

	private void uncheckAllMembers() {
		for (WebElement cb : checkboxes)
			if (cb.isSelected())
				cb.click();
	}

	public void deleteFileBeforeUpdate(String texte) {
		String xpath = "//li[span[text()='" + texte + "']]//button";
		su.findElement(By.xpath(xpath)).click();
	}

	public void deleteFilesBeforeUpdate(String csv) {
		if (csv == null || csv.isBlank())
			return;
		Arrays.stream(csv.split(",")).map(String::trim).filter(v -> !v.isEmpty()).forEach(this::deleteFileBeforeUpdate);
	}

	public void clickPilotageProjets() {
		su.click(pilotageBtn);
	}

	public void clickAjouterProjet() {
		su.click(ajouterBtn);
	}
	public void clickListProjet() {
		su.click(listeBtn);
	}

	public void clickLabel(String rawLabel) {
		String label = Validations.nullToEmpty(rawLabel).trim();
		if (label.isEmpty() || "[empty]".equalsIgnoreCase(label))
			return;

		WebElement target = switch (label) {
		case "Pilotage des projets" -> pilotageBtn;
		case "Ajouter" -> ajouterBtn;
		case "Modifier Projet" -> btnModifierProjet;
		case "Créer Projet"   -> btnCreerProjet;
		case "En attente" -> enAttenteBtn;
		case "En cours" -> enCoursBtn;
		case "Terminé" -> termineBtn;
		case "Annulé" -> annuleBtn;
		case "En Attente" -> enAttenteBtnForUpdateProject;
		default -> throw new IllegalArgumentException("Label inconnu : " + label);
		};
		su.click(target);
	}

	public void choisirMultipleFichiers(String nomsFichiers) {
		if (nomsFichiers == null || nomsFichiers.trim().isEmpty() || "[empty]".equalsIgnoreCase(nomsFichiers.trim()))
			return;
		uploadFiles.sendKeys(Arrays.stream(nomsFichiers.split(","))
				.map(n -> Paths.get("src", "test", "resources", "uploads", n.trim()).toAbsolutePath().toString())
				.collect(Collectors.joining("\n")));
	}
	
	public void fillFormUpdate(Map<String, String> m) {
		if (m == null)
			return;
		if (m.containsKey("titre"))
			saisirTitre(m.get("titre"));
		if (m.containsKey("description"))
			saisirDescription(m.get("description"));
		if (m.containsKey("dateDebut"))
			saisirDateDebut(m.get("dateDebut"));
		if (m.containsKey("dateFin"))
			saisirDateFin(m.get("dateFin"));
		if (m.containsKey("statut"))
			clickLabel(m.get("statut"));
		if (m.containsKey("membres")) {
			uncheckAllMembers();
			selectMembersForUpdate(m.get("membres"));
		}
		if (m.containsKey("images")) {
			deleteFilesBeforeUpdate(m.get("fichiers"));
			choisirMultipleFichiers(m.get("images"));
		}
	}

	public void replaceIfPlaceholder(Map<String, String> map, String key, String actual) {
		if (map.containsKey(key) && GenerateRandomDataUsingFaker.isPlaceholder(map.get(key)))
			map.put(key, actual);
	}

	public WebElement getBoutonActionParTitle(String titre, String action) {
		String cls = switch (action) {
		case "details" -> "btn btn-outline-info btn-sm rounded-circle me-2";
		case "modifier" -> "btn btn-outline-warning btn-sm rounded-circle";
		case "archiver" -> "btn btn-outline-danger btn-sm rounded-circle";
		default -> throw new IllegalArgumentException("Action inconnue : " + action);
		};
		String xpath = "//tr[td[text()='" + titre + "']]//button[contains(@class,'" + cls + "')]";
		By by = By.xpath(xpath);
		w.forVisibleElement(Duration.ofSeconds(7), by);
		WebElement bouton = su.findElement(by);
		su.scrollIntoView(bouton);
		return bouton;
	}
	public void waitForVisbilityPageDeatils() {
		w.forElementToBeDisplayed(Duration.ofSeconds(5), titleOfProjectDetailsPage, "bouton details projet");
	}

	public boolean checkIfCheckboxIsNotSelected(List<WebElement> values) {
		for (WebElement n : values)
			if (!Validations.isElementSelected(n))
				return true;
		return false;
	}

	public void assertRowContains(String titre, Map<String, String> expected) {
		By rowBy = By.xpath("//tr[td[normalize-space(text())='" + titre + "']]");
		w.forPresenceOfElements(Duration.ofSeconds(30), rowBy, "Ligne projet");
		String txt = su.findElement(rowBy).getText().toLowerCase();
		for (Map.Entry<String, String> e : expected.entrySet()) {
			String v = e.getValue();
			if (v == null || v.isEmpty())
				continue;
			Assertions.assertTrue(txt.contains(v.toLowerCase().trim()), "Attendu '" + v + "' non trouvé...");
		}
	}

	public void filter(String q) {
		searchZone.click();
		su.writeText(searchZone, q == null ? "" : q.toLowerCase());
		ElementActions.waitFor(5);
	}

	public boolean trySelectByFilter(String titre) {
		filter(titre);
		return true;
	}

	public void assertResponsableInTable(String titre, String nom) {
		((JavascriptExecutor) SetUp.getDriver())
				.executeScript("document.querySelector('div.table-responsive').scrollLeft=9999;");
		String xpath = "//tr[td[text()='" + titre + "']]//td[text()='" + nom + "']";
		String actual = su.findElement(By.xpath(xpath)).getText(); // <<< SeleniumUtils
		Assertions.assertEquals(actual, nom);
	}

	public void assertResponsableInDetails(String obtained) {
		w.forElementToBeDisplayed(Duration.ofSeconds(15), nomResponsableExcpectedInDetails, "Responsable (détails)"); // <<<
																														// Wait
		String expected = nomResponsableExcpectedInDetails.getText();
		Assertions.assertEquals(expected, obtained);
	}

	public LocalDate parseDate(String keyword) {
		return switch (keyword) {
		case "today" -> LocalDate.now();
		case "yesterday", "today-1d" -> LocalDate.now().minusDays(1);
		case "today-7d" -> LocalDate.now().minusDays(7);
		case "lastWeek" -> LocalDate.now().minusWeeks(1);
		case "lastYear" -> LocalDate.now().minusYears(1);
		case "today-20" -> LocalDate.now().minusDays(20);
		default -> LocalDate.now();
		};
	}

	public boolean verifBtnNonCliquable() {
		if (!Validations.isElementEnabled(ajouterBtn))
			return true;
		return false;

	}

	public void waitOfVesibilityMsgErreurDateDebutForUpdate() {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), msgErreurDateDebutForUpdate, "erreur date début");
	}

	public void waitOfVesibilityMsgErreurDateFinForUpdate() {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), msgErreurDateFinForUpdate, "erreur date Fin");
	}

}
