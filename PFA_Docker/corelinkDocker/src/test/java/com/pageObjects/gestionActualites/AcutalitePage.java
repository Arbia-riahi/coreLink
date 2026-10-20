package com.pageObjects.gestionActualites;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.hooks.SetUp;
import com.actions.*;
import com.utils.*;
import com.dataReader.PropertyReader;
import com.pageObjects.BasePage;

public class AcutalitePage extends BasePage {

	public PropertyReader config;
	public final ElementActions su;
	public final Wait w;
	public final BrowserActions ba;

	public AcutalitePage(WebDriver driver) {
		super(driver);
		config = new PropertyReader();
		this.su = new ElementActions();
		this.w = new Wait(driver);
		this.ba = new BrowserActions(SetUp.getDriver());
	}

	@FindBy(how = How.XPATH, using = "//a[@class='nav-link collapsed']/span[contains(text(),'Raccourcis')]")
	public WebElement btnRaccourci;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),' Actualités ')]")
	public WebElement btnActualite;

	@FindBy(how = How.ID, using = "dataTable")
	public WebElement tableActualite;

	@FindBy(how = How.CSS, using = ".btn.btn-primary.btn-add")
	public WebElement btnAjouter;

	@FindBy(how = How.ID, using = "titre")
	public WebElement titre;

	@FindBy(how = How.ID, using = "description")
	public WebElement description;

	@FindBy(how = How.ID, using = "photos")
	public WebElement uploadFiles;

	@FindBy(how = How.XPATH, using = "//button[@class='btn btn-primary btn-lg px-4 w-100 w-md-auto']")
	public WebElement btnCreeActualite;

	@FindBy(how = How.CSS, using = ".btn.btn-primary.btn-user.btn-block")
	public WebElement btnUpdateActualite;

	@FindBy(how = How.CSS, using = ".h4.mb-0")
	public WebElement titleListActualit;

	@FindBy(how = How.CSS, using = ".btn.btn-warning.px-4")
	public WebElement updateButton;

	@FindBy(how = How.XPATH, using = "//button[contains(text(),' Suivant ')]")
	public WebElement suivantButton;

	@FindBy(how = How.XPATH, using = "//button[contains(text(),' Précédent ')]")
	public WebElement precdentButton;

	@FindBy(how = How.CSS, using = ".h4.text-gray-900.mb-12")
	public WebElement titreFormsUpdate;

	@FindBy(how = How.XPATH, using = "//span[contains(text(), 'Page ')]")
	public WebElement nbrDepage;

	public void verifTitlePageActualité() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), titleListActualit, "gestion des actualités");
	}

	public void verifTitleUodateActualité() {
		w.forElementToBeDisplayed(Duration.ofSeconds(10), titreFormsUpdate, "mise a jour des actualités");
	}

	public void verifFormUpdateRempli() {
		if (!titre.getAttribute("value").isBlank() || !description.getAttribute("value").isBlank()) {
			return;
		}
	}

	public void verifLastPage() {
		su.waitForElementToBeClickable(suivantButton);
	}

	public void clickLabel(String rawLabel) {
		// attendre que la page soit stable avant toute action
		w.forLoading(Duration.ofSeconds(20));

		String label = Validations.nullToEmpty(rawLabel).trim();
		if (label.isEmpty() || "[empty]".equalsIgnoreCase(label))
			return;

		WebElement target = switch (label) {
		case "Raccourcis" -> btnRaccourci;
		case "Actualités" -> btnActualite;
		case "Ajouter" -> btnAjouter;
		case "Créer l’actualité" -> btnCreeActualite;
		case "Mettre à jour l'actualité" -> btnUpdateActualite;

		case "Suivant" -> {
			su.scrollBy(0, 500);
			yield suivantButton;
		}
		case "Précédent" -> {
			su.scrollBy(0, 500);
			yield precdentButton;
		}
		default -> throw new IllegalArgumentException("Label inconnu : " + label);
		};
		ElementActions.waitFor(5);
		su.waitForElementToBeClickable(target);
		su.click(target);

	}

	public void saisirTitre(String t) {
		su.clearField(titre);
		su.writeText(titre, Validations.nullToEmpty(t));

	}

	public void saisirDescription(String d) {
		su.clearField(description);
		su.writeText(description, Validations.nullToEmpty(d));

	}

	public void choisirMultipleFichiers(String nomsFichiers) {
		if (nomsFichiers == null || nomsFichiers.trim().isEmpty() || "[empty]".equalsIgnoreCase(nomsFichiers.trim()))
			return;
		uploadFiles.sendKeys(Arrays.stream(nomsFichiers.split(","))
				.map(n -> Paths.get("src", "test", "resources", "uploads", n.trim()).toAbsolutePath().toString())
				.collect(Collectors.joining("\n")));
	}

	public void fillForm(Map<String, String> m) {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), titre, "champ titre");
		if (m == null)
			return;
		if (m.containsKey("titre"))
			saisirTitre(m.get("titre"));
		if (m.containsKey("description"))
			saisirDescription(m.get("description"));
		if (m.containsKey("photos"))
			choisirMultipleFichiers(m.get("photos"));
	}

	public boolean actualitéExisteDansListe(String titre) {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), titleListActualit, "titre liste actualités");
		String xpath = "//tr[td[text()=' " + titre + " ']]";
		return SetUp.getDriver().findElements(By.xpath(xpath)).size() > 0;
	}

	public WebElement getBoutonActionParTitle(String titre, String action) {
		ElementActions.waitFor(20);
		String classSelector;
		switch (action) {
		case "modifier" -> classSelector = "btn btn-soft-warning btn-icon";
		case "supprimer" -> classSelector = "btn btn-soft-danger btn-icon ms-2";
		default -> throw new IllegalArgumentException("Action inconnue : " + action);
		}

		String xpath = "//tr[td[text()=' " + titre + " ']]//button[contains(@class, '" + classSelector + "')]";
		By by = By.xpath(xpath);
		WebElement bouton = SetUp.getDriver().findElement(by);
		su.scrollIntoView(bouton);
		return bouton;

	}

	public void clickIconUpdate(String titre) {
		WebElement button = getBoutonActionParTitle(titre, "modifier");
		su.waitForElementToBeClickable(button);
		su.click(button);
	}

	public void clickIconDelete(String titre) {
		WebElement button = getBoutonActionParTitle(titre, "supprimer");
		su.waitForElementToBeClickable(button);
		su.click(button);
	}

	public void assertRowContains(String titre, Map<String, String> expected) {
		WebElement row = getBoutonActionParTitle(titre, "modifier").findElement(By.xpath("./ancestor::tr"));
		for (Map.Entry<String, String> e : expected.entrySet()) {
			String key = e.getKey().toLowerCase();
			String val = e.getValue();
			if (val == null || val.isEmpty())
				continue;
			if (key.contains("photo")) {
				row.findElements(By.xpath("//tr[td[text()=' " + titre + " ']]//button[contains(@class, 'photo-more')]"))
						.stream().findFirst().ifPresent(WebElement::click);
				String page = SetUp.getDriver().getPageSource();
				for (String p : val.split(",")) {
					String t = p.trim();
					Assertions.assertTrue(page.contains(t) || page.contains(t.replace(" ", "%20")),
							"Image attendue '" + t + "' non trouvée");
				}
			} else {
				Assertions.assertTrue(row.getText().contains(val),
						"Valeur attendue '" + val + "' non trouvée dans la ligne du tableau " + titre);
			}
		}
	}

	// pagination
	public int rowCount() {
		return tableActualite.findElements(By.cssSelector("tbody tr")).size();
	}

	public String firstRowTitle() {
		w.forElementToBeDisplayed(Duration.ofSeconds(30), tableActualite, "table actualité");
		WebElement cell = SetUp.getDriver().findElement(By.cssSelector("table tbody tr:first-child td:first-child"));
		return cell.getText().trim();
	}
}
