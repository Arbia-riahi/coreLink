package com.pageObjects.gestionProjets;

import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
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
import com.utils.actionsBot.ElementActions;
import com.pageObjects.BasePage;

public class CreateProjectPage extends BasePage {
	private final ElementActions su;
	private final Wait w;

	public CreateProjectPage(WebDriver driver) {
		super(driver);
		this.su = new ElementActions();
		this.w = new Wait(driver);
	}

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Pilotage des projets')]")
	public WebElement pilotageBtn;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Ajouter')]")
	public WebElement ajouterBtn;

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

	@FindBy(how = How.XPATH, using = "//input[@placeholder='Rechercher par titre, description, chef, statut...']")
	public WebElement searchZone;

	@FindBy(how = How.XPATH, using = "//div[@class='user-container']/span")
	public WebElement nomResponsableExcpectedInTable;

	@FindBy(how = How.XPATH, using = "//p[@class='text-muted mb-0']")
	public WebElement nomResponsableExcpectedInDetails;

	@FindBy(how = How.CSS, using = ".text-danger.small.mt-1")
	public WebElement msgErreurDateDebut;

	@FindBy(how = How.CSS, using = ".text-danger.small.mt-1")
	public WebElement msgErreurDateFin;

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

	public void selectMemberByLabel(String label) {
		String xp = "//div[@class='mb-1' and normalize-space(.)='" + label + "']/input[@type='checkbox']";
		su.findElement(By.xpath(xp)).click();
	}

	public void selectMembers(String csv) {
		if (csv == null || csv.isBlank())
			return;
		Arrays.stream(csv.split(",")).map(String::trim).filter(v -> !v.isEmpty()).forEach(this::selectMemberByLabel);
	}

	public void clickPilotageProjets() {
		su.click(pilotageBtn);
	}

	public void clickAjouterProjet() {
		su.click(ajouterBtn);
	}

	public void clickLabel(String rawLabel) {
		String label = Validations.nullToEmpty(rawLabel).trim();
		if (label.isEmpty() || "[empty]".equalsIgnoreCase(label))
			return;

		WebElement target = switch (label) {
		case "Pilotage des projets" -> pilotageBtn;
		case "Ajouter" -> ajouterBtn;
		case "En attente" -> enAttenteBtn;
		case "En cours" -> enCoursBtn;
		case "Terminé" -> termineBtn;
		case "Annulé" -> annuleBtn;
		case "Créer Projet" -> btnCreerProjet;
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

	public void fillForm(Map<String, String> m) {
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
		if (m.containsKey("membres"))
			selectMembers(m.get("membres"));
		if (m.containsKey("photos"))
			choisirMultipleFichiers(m.get("photos"));
	}

	public void replaceIfPlaceholder(Map<String, String> map, String key, String actual) {
		if (map.containsKey(key) && GenerateRandomDataUsingFaker.isPlaceholder(map.get(key)))
			map.put(key, actual);
	}

	public WebElement getBoutonActionParTitle(String titre, String action) {
		String cls = switch (action) {
		case "details" -> "btn btn-outline-info btn-sm rounded-circle";
		case "modifier" -> "btn btn-outline-warning btn-sm rounded-circle";
		case "archiver" -> "btn btn-outline-danger btn-sm rounded-circle";
		default -> throw new IllegalArgumentException("Action inconnue : " + action);
		};
		String xpath = "//tr[td[text()='" + titre + "']]//button[contains(@class,'" + cls + "')]";
		By by = By.xpath(xpath);
		WebElement bouton = su.findElement(by);
		su.scrollIntoView(bouton);
		return bouton;
	}

	public boolean checkIfCheckboxIsNotSelected(List<WebElement> values) {
		for (WebElement n : values)
			if (!Validations.isElementSelected(n))
				return true;
		return false;
	}

	public void assertRowContains(String titre, Map<String, String> expected) {
		By rowBy = By.xpath("//tr[td[normalize-space(text())='" + titre + "']]");

		// 1. Récupération d’un élément stable, visible, non-stale
		WebElement row = w.forVisibleElement(Duration.ofSeconds(15), rowBy);

		// 2. Lecture du texte directement sur CET élément (pas un autre)
		String txt = row.getText().toLowerCase();

		// 3. Vérification
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
	}

	public boolean trySelectByFilter(String titre) {

		filter(titre);
		By xp = By.xpath("//tr[td[normalize-space(text())='" + titre + "']]");
		w.forPresenceOfElements(Duration.ofSeconds(15), xp, "Résultat filtre");
		return true;

	}

	public void assertResponsableInTable(String titre, String nom) {

		// 1. Scroll horizontal
		((JavascriptExecutor) SetUp.getDriver())
				.executeScript("document.querySelector('div.table-responsive').scrollLeft=9999;");
		ElementActions.waitFor(7);
		// 2. XPath de la cellule cible
		By cellBy = By
				.xpath("//tr[td[normalize-space(text())='" + titre + "']]//td[normalize-space(text())='" + nom + "']");

		// 3. Attendre l'élément visible ET stable
		WebElement cell = w.forVisibleElement(Duration.ofSeconds(10), cellBy);

		// 4. Lire le texte du même élément (pas un autre)
		String actual = cell.getText().trim();

		// 5. Assert
		Assertions.assertEquals(nom.trim(), actual);
	}

	public void assertResponsableInDetails(String obtained) {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), nomResponsableExcpectedInDetails, "Responsable (détails)"); // <<<
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
		case "today+30d" -> LocalDate.now().plusDays(30);
		case "today+60d" -> LocalDate.now().plusDays(60);
		default -> LocalDate.now();
		};
	}

	public boolean verifBtnNonCliquable() {
		if (!Validations.isElementEnabled(ajouterBtn))
			return true;
		return false;

	}

	public void waitOfVesibilityMsgErreurDateDebut() {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), msgErreurDateDebut, "erreur date début");
	}

	public void waitOfVesibilityMsgErreurDateFin() {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), msgErreurDateFin, "erreur date Fin");
	}

	public boolean VerifvalTitreIsBlank() {
		return titre.getAttribute("value").isBlank();
	}

	public boolean VerifvalDescriptionIsBlank() {
		return description.getAttribute("value").isBlank();
	}

	public boolean VerifvalDateDebutIsBlank() {
		return dateDebut.getAttribute("value").isBlank();
	}

	public boolean VerifvalDateFinIsBlank() {
		return dateFin.getAttribute("value").isBlank();
	}

	public boolean VerifUploadFileIsBlank() {
		return dateFin.getText().isBlank();
	}

}
