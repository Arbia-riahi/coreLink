package com.pageObjects.gestionTaches;

import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.actions.*;
import com.hooks.SetUp;
import com.pageObjects.*;
import com.pageObjects.gestionProjets.CreateProjectPage;
import com.utils.*;

public class CreateTachePage extends BasePage {
	private final ElementActions su;
	private final Wait w;
	private final SelectFromListUtils selectMulti;
	private final CreateProjectPage projectPage;

	public CreateTachePage(WebDriver driver) {
		super(driver);
		this.su = new ElementActions();
		this.w = new Wait(driver);
		this.selectMulti = new SelectFromListUtils();
		this.projectPage = new CreateProjectPage(SetUp.getDriver());
	}

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Pilotage des projets')]")
	public WebElement pilotageBtn;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Liste')]")
	public WebElement listeBtn;

	@FindBy(how = How.ID, using = "titre")
	private WebElement titre;

	@FindBy(how = How.ID, using = "mat-select-0")
	private WebElement openListPriorite;

	@FindBy(how = How.ID, using = "mat-option-0")
	private WebElement optionBasse;

	@FindBy(how = How.ID, using = "mat-option-1")
	private WebElement optionMoyenne;

	@FindBy(how = How.ID, using = "mat-option-2")
	private WebElement optionHaute;

	@FindBy(how = How.ID, using = "mat-option-3")
	private WebElement optionCritique;

	@FindBy(how = How.ID, using = "etat")
	private WebElement dropdownMenuEtat;

	@FindBy(how = How.ID, using = "dateFinDate")
	private WebElement dateFin;

	@FindBy(how = How.ID, using = "dateFinTime")
	private WebElement endTime;

	@FindBy(how = How.CSS, using = ".btn.btn-outline-secondary.btn-sm.ms-2")
	private WebElement btnVueKanban;

	@FindBy(how = How.CSS, using = ".btn.btn-outline-secondary.btn-sm.ms-2")
	private WebElement btnVueTableau;

	@FindBy(how = How.CSS, using = ".btn.btn-success.btn-sm.ms-2")
	private WebElement btnCreerNvTache;

	@FindBy(how = How.CSS, using = ".btn.btn-success")
	private WebElement btnTacheCree;

	@FindBy(how = How.CSS, using = ".btn.btn-secondary.me-2")
	private WebElement btnAnnulerTache;

	@FindBy(how = How.ID, using = "filterEtat")
	private WebElement filterEtat;

	@FindBy(how = How.CSS, using = ".kanban-column-header.header-afaire")
	private WebElement cardAfaire;

	@FindBy(how = How.CSS, using = ".kanban-column-header.header-encours")
	private WebElement cardEnCours;

	@FindBy(how = How.CSS, using = ".kanban-column-header.header-enrevision")
	private WebElement cardEnRevision;

	@FindBy(how = How.CSS, using = ".kanban-column-header.header-termine")
	private WebElement cardTermine;

	@FindBy(how = How.CSS, using = ".kanban-column-header.header-bloque")
	private WebElement cardBloque;

	@FindBy(how = How.CSS, using = ".kanban-column-header.header-annule")
	private WebElement cardAnnule;

	@FindBy(how = How.ID, using = "searchTitre")
	private WebElement searchInput;

	public void saisirTitre(String v) {
		w.forElementToBeDisplayed(Duration.ofSeconds(15), titre, "titre d 'une tache");
		su.writeText(titre, Validations.nullToEmpty(v));
	}

	public void saisirDateFin(String v) {
		su.writeText(dateFin, Validations.nullToEmpty(v));
	}

	public void selectPriorite(String label) {
		openListPriorite.click();

		switch (label.trim().toLowerCase()) {
		case "basse" -> optionBasse.click();
		case "moyenne" -> optionMoyenne.click();
		case "haute" -> optionHaute.click();
		case "critique" -> optionCritique.click();
		default -> throw new IllegalArgumentException("Valeur inattendue : " + label);
		}
	}

	public void selectEtat(String value) {
		selectMulti.selectDropDownListByValue(dropdownMenuEtat, value);
	}

	public void selectMembersByLabel(String label) {
		String xp = "//div[@class='checkbox-item' and .//label[normalize-space(text())='" + label
				+ "']]//input[@type='checkbox']";
		su.findElement(By.xpath(xp)).click();
	}

	public void selectMembers(String csv) {
		if (csv == null || csv.isBlank())
			return;
		Arrays.stream(csv.split(",")).map(String::trim).filter(v -> !v.isEmpty()).forEach(this::selectMembersByLabel);
	}

	public void clickLabel(String label) {
		WebElement target = switch (label.trim()) {
		case "Pilotage des projets" -> pilotageBtn;
		case "Liste" -> listeBtn;
		case "Nouvelle tâche" -> btnCreerNvTache;
		case "Créer" -> btnTacheCree;
		case "Annuler" -> btnAnnulerTache;
		case "Vue Kanban" -> btnVueKanban;
		case "Vue Tableau" -> btnVueTableau;
		default -> throw new IllegalArgumentException("Label inconnu : " + label);
		};
		su.click(target);
	}

	public WebElement getBoutonActionParTitleAndClick(String titre) {
		String xpath = "//tr[td[text()='" + titre
				+ "']]//button[contains(@class,'btn btn-outline-primary btn-sm ms-2 shadow-sm rounded-pill')]";
		By by = By.xpath(xpath);
		WebElement bouton = su.findElement(by);
		su.scrollIntoView(bouton);
		su.click(bouton);
		return bouton;
	}

	public void filtrerByEtat(String value) {
		selectMulti.selectDropDownListByValue(filterEtat, value);
	}

	public void filterByTitle(String q) {
		searchInput.click();
		su.writeText(searchInput, q == null ? "" : q.toLowerCase());
		su.scrollBy(0, 100);
	}

	public void fillForm(Map<String, String> m) {
		if (m == null)
			return;
		if (m.containsKey("titre"))
			saisirTitre(m.get("titre"));
		if (m.containsKey("priorité"))
			selectPriorite(m.get("priorité"));
		if (m.containsKey("etat"))
			selectEtat(m.get("etat"));
		if (m.containsKey("dateFin"))
			saisirDateFin(m.get("dateFin"));
		if (m.containsKey("membres"))
			selectMembers(m.get("membres"));
	}

	public void replaceIfPlaceholder(Map<String, String> map, String key, String actual) {
		if (map.containsKey(key) && GenerateRandomDataUsingFaker.isPlaceholder(map.get(key)))
			map.put(key, actual);
	}

	public WebElement getColorFlagPriority(String color) {
		String cls = switch (color.toLowerCase()) {
		case "bleu" -> "me-1 fa-flag fas text-primary";
		case "jaune" -> "me-1 fa-flag fas text-warning";
		case "gris" -> "me-1 fa-flag fas text-secondary";
		case "rouge" -> "me-1 fa-flag fas text-danger";
		default -> throw new IllegalArgumentException("Couleur de drapeau inconnue : " + color);
		};

		String xpath = "//tr[1]//td//i[contains(@class,'" + cls + "')]";
		return su.findElement(By.xpath(xpath));
	}

	public WebElement getColorFlagPriorityForKanban(String color) {
		String cls = switch (color.toLowerCase()) {
		case "bleu" -> "me-1 fa-flag fas text-primary";
		case "jaune" -> "me-1 fa-flag fas text-warning";
		case "gris" -> "me-1 fa-flag fas text-secondary";
		case "rouge" -> "me-1 fa-flag fas text-danger";
		default -> throw new IllegalArgumentException("Couleur de drapeau inconnue : " + color);
		};

		String xpath = "//div[@class='fw-bold']//i[contains(@class,'" + cls + "')]";
		return su.findElement(By.xpath(xpath));
	}

	public void assertFlags(String prioriteTexte) {
		ElementActions.waitFor(5);
		String expectedColor = switch (prioriteTexte.trim().toLowerCase()) {
		case "basse" -> "gris";
		case "moyenne" -> "bleu";
		case "haute" -> "jaune";
		case "critique" -> "rouge";
		default -> throw new IllegalArgumentException("Priorité inconnue : " + prioriteTexte);
		};

		WebElement flag = getColorFlagPriority(expectedColor);
		Assertions.assertTrue(flag.isDisplayed(), "Le drapeau de couleur " + expectedColor + " n’a pas été trouvé !");
	}

	public void assertFlagsForKanban(String prioriteTexte) {
		ElementActions.waitFor(5);
		String expectedColor = switch (prioriteTexte.trim().toLowerCase()) {
		case "basse" -> "gris";
		case "moyenne" -> "bleu";
		case "haute" -> "jaune";
		case "critique" -> "rouge";
		default -> throw new IllegalArgumentException("Priorité inconnue : " + prioriteTexte);
		};

		WebElement flag = getColorFlagPriorityForKanban(expectedColor);
		Assertions.assertTrue(flag.isDisplayed(), "Le drapeau de couleur " + expectedColor + " n’a pas été trouvé !");
	}

	public void filter(String q) {
		projectPage.searchZone.click();
		su.writeText(projectPage.searchZone, q == null ? "" : q.toLowerCase());
		su.scrollBy(0, 100);
		ElementActions.waitFor(10);
	}

	public void assertRowContains(String titre, Map<String, String> expected) {
		WebElement row = su.findElement(By.xpath("//tr[td[normalize-space(text())='" + titre + "']]"));
		String txt = row.getText().toLowerCase();

		// Vérifier les membres (avatars)
		if (expected.containsKey("membres")) {
			String membres = expected.get("membres");
			for (String nom : membres.split(",")) {
				nom = nom.trim();
				List<WebElement> imgs = row.findElements(By.xpath(".//img[contains(@title,'" + nom + "')]"));
				Assertions.assertTrue(!imgs.isEmpty(), "❌ Membre non trouvé: " + nom);
			}
		}

		// Vérifier le reste
		for (Map.Entry<String, String> e : expected.entrySet()) {
			if ("membres".equalsIgnoreCase(e.getKey()))
				continue;
			String v = e.getValue();
			if (v == null || v.isEmpty())
				continue;
			Assertions.assertTrue(txt.contains(v.toLowerCase()), "❌ '" + v + "' non trouvé dans la ligne");
		}
	}

	public void assertKanbanContains(String cardName, Map<String, String> expected) {
		ElementActions.waitFor(10);
		// Trouver la carte Kanban où le membre principal (ou titre) est visible
		WebElement card = su.findElement(By.xpath(
				"//div[contains(@class, 'cdk-drop-list kanban-column')][ .//div[contains(@class, 'kanban-column-header') and contains(normalize-space(.), '"
						+ cardName + "')] ]"));
		String txt = card.getText().toLowerCase();

		// Vérifier les membres (avatars)
		if (expected.containsKey("membres")) {
			String membres = expected.get("membres");
			for (String nom : membres.split(",")) {
				nom = nom.trim();
				List<WebElement> imgs = card.findElements(By.xpath("//img[contains(@title,'" + nom + "')]"));

				Assertions.assertTrue(!imgs.isEmpty(), "❌ Membre non trouvé: " + nom);
			}
		}

		// Vérifier le reste
		for (Map.Entry<String, String> e : expected.entrySet()) {
			if ("membres".equalsIgnoreCase(e.getKey()))
				continue;
			String v = e.getValue();
			if (v == null || v.isEmpty())
				continue;
			Assertions.assertTrue(txt.contains(v.toLowerCase()), "❌ '" + v + "' non trouvé dans la ligne");
		}
	}

	public WebElement getBtnVueTableau() {
		return btnVueTableau;

	}

	public void clickAddKanban(String etat) {
	  ElementActions.waitFor(5);
	  By findEtat = By.xpath("//div[contains(@class,'kanban-column')][.//*[text()=' " + etat + " ']]");
	  su.scrollIntoView(su.findElement(findEtat));
	
	
	  By addCard = By.xpath("//div[contains(@class,'kanban-column')][.//*[normalize-space()='" + etat + "']]//button[contains(@class,'add-card')]");
	  su.scrollIntoView(su.findElement(addCard));
	  su.click(addCard);
	}

}
