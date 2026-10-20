package com.actions;

import com.hooks.SetUp;
import com.logs.LogsManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import com.pageObjects.BasePage;

/**
 * 🔧 Boîte à outils Selenium centralisée : Actions, waits, scrolls, gestion des
 * fenêtres, encodage, etc. Chaque méthode est loggée via LogsManager (Log4j2
 * wrapper).
 */
public class ElementActions extends BasePage {

	protected Properties properties;
	protected FileInputStream configFis;
	private static WebDriver driver;

	public ElementActions() {
		super(driver);
		ElementActions.driver = SetUp.getDriver();
		LogsManager.info("SeleniumUtils initialisée avec le driver :", driver.toString());
	}

	// ========================================================================
	// 🔍 LOCATORS
	// ========================================================================
	public By locatorValue(String locatorType, String value) {
		LogsManager.debug("Recherche locator :", locatorType, "=", value);
		switch (locatorType.toLowerCase()) {
		case "id":
			return By.id(value);
		case "name":
			return By.name(value);
		case "xpath":
			return By.xpath(value);
		case "css":
			return By.cssSelector(value);
		case "linktext":
			return By.linkText(value);
		case "partiallinktext":
			return By.partialLinkText(value);
		default:
			LogsManager.error("Type de locator inconnu :", locatorType);
			return null;
		}
	}

	// ========================================================================
	// 🖱️ ACTIONS / CLICS
	// ========================================================================
	public void clickOnElementUsingActions(By element) {
		try {
			LogsManager.info("Click via Actions sur :", element.toString());
			new Actions(driver).moveToElement(driver.findElement(element)).click().perform();
		} catch (Exception e) {
			LogsManager.error("Échec du click via Actions :", e.getMessage());
		}
	}

	public void clickOnByElementUsingJs(By element) {
		try {
			WebElement el = driver.findElement(element);
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
			LogsManager.info("Click via JS réussi sur :", element.toString());
		} catch (Exception e) {
			LogsManager.error("Erreur JS click sur :", element.toString(), e.getMessage());
		}
	}

	public void clickOnElementUsingJs(WebElement element) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
			LogsManager.info("Click via JS réussi sur un WebElement direct.");
		} catch (Exception e) {
			LogsManager.error("Erreur JS click WebElement :", e.getMessage());
		}
	}

	// ✅ Pour cliquer via un WebElement déjà trouvé
	public void click(WebElement element) {
		try {
			waitForElementToBeClickable(element);
			element.click();
			LogsManager.info("Click réussi sur l'élément :", safeText(element));
		} catch (Exception e) {
			LogsManager.error("Échec du click sur WebElement :", e.getMessage());
		}
	}

	// ✅ Pour cliquer via un locator directement
	public void click(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			waitForElementToBeClickable(locator);
			element.click();
			LogsManager.info("Click réussi sur le locator :", locator.toString());
		} catch (Exception e) {
			LogsManager.error("Échec du click sur locator :", locator.toString(), e.getMessage());
		}
	}

	// ========================================================================
	// 🎢 SCROLLS
	// ========================================================================
	public void scrollIntoView(WebElement element) {
		try {
			WebElement stable = new WebDriverWait(driver, Duration.ofSeconds(10))
					.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({inline:'end'});", stable);
			LogsManager.info("ScrollIntoView exécuté avec succès.");
		} catch (Exception e) {
			LogsManager.warn("Impossible de scroller jusqu’à l’élément :", e.getMessage());
		}
	}

	public void scrollBy(int x, int y) {
		((JavascriptExecutor) driver).executeScript("document.scrollingElement.scrollBy(arguments[0], arguments[1]);",
				x, y);
		LogsManager.debug("ScrollBy effectué de", x + "px horizontalement et", y + "px verticalement");
	}

	// ========================================================================
	// 🧭 INFOS PAGE
	// ========================================================================
	public String getTitle() {
		String title = driver.getTitle();
		LogsManager.info("Titre de la page :", title);
		return title;
	}

	public String getPageSource() {
		LogsManager.debug("Récupération du source HTML complet.");
		return driver.getPageSource();
	}

	// ========================================================================
	// 🔎 FINDERS
	// ========================================================================
	public WebElement findElement(By locator) {
		try {
			WebElement el = driver.findElement(locator);
			LogsManager.info("Élément trouvé :", locator.toString());
			return el;
		} catch (NoSuchElementException e) {
			LogsManager.error("Élément introuvable :", locator.toString());
			throw e;
		}
	}

	public List<WebElement> findElements(By locator) {
		try {
			List<WebElement> list = driver.findElements(locator);
			LogsManager.info(list.size() + " éléments trouvés pour :", locator.toString());
			return list;
		} catch (NoSuchElementException e) {
			LogsManager.error("Aucun élément trouvé pour :", locator.toString());
			throw e;
		}
	}

	// ========================================================================
	// ⏱️ WAITS
	// ========================================================================
	public void waitForElementToBeClickable(By locator) {
		LogsManager.debug("Attente de clic possible pour :", locator.toString());
		new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(locator));
	}

	public void waitForElementToBeClickable(WebElement locator) {
		LogsManager.debug("Attente de clic possible pour WebElement...");
		new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.elementToBeClickable(locator));
	}

	// ========================================================================
	// ✏️ CHAMPS TEXTE
	// ========================================================================
	public void clearField(WebElement element) {
		try {
			element.clear();
			LogsManager.debug("Champ effacé :", safeText(element));
		} catch (Exception e) {
			LogsManager.warn("Impossible d’effacer le champ :", e.getMessage());
		}
	}

	private String safeText(WebElement el) {
		try {
			return el.getText();
		} catch (Exception ignored) {
			return "<unreadable>";
		}
	}

	// ========================================================================
	// 📥 SAISIE ET CLIC SIMPLE
	// ========================================================================
	public void click(String locatorType, String value) {
		try {
			By locator = locatorValue(locatorType, value);
			WebElement element = driver.findElement(locator);
			waitForElementToBeClickable(locator);
			element.click();
			LogsManager.info("Click réussi sur :", locatorType, "=", value);
		} catch (Exception e) {
			LogsManager.error("Erreur click :", e.getMessage());
		}
	}

	// ✅ Pour un WebElement déjà trouvé
	public void writeText(WebElement element, String text) {
		try {
			waitForElementToBeClickable(element);
			clearField(element);
			element.sendKeys(text);
			LogsManager.info("Texte saisi sur WebElement :", safeText(element));
		} catch (Exception e) {
			LogsManager.error("Erreur lors de la saisie sur WebElement :", e.getMessage());
		}
	}

	// ✅ Pour un locator (By)
	public void writeText(By locator, String text) {
		try {
			WebElement element = driver.findElement(locator);
			waitForElementToBeClickable(locator);
			clearField(element);
			element.sendKeys(text);
			LogsManager.info("Texte saisi sur le locator :", locator.toString());
		} catch (Exception e) {
			LogsManager.error("Erreur lors de la saisie sur locator :", locator.toString(), e.getMessage());
		}
	}

	// ✅ Pour un type de locator et sa valeur (String, String)
	public void writeText(String locatorType, String value, String text) {
		try {
			By locator = locatorValue(locatorType, value);
			writeText(locator, text); // réutilise la méthode ci-dessus
		} catch (Exception e) {
			LogsManager.error("Erreur lors de la saisie via type+value :", locatorType, "=", value, e.getMessage());
		}
	}

	public String readText(WebElement elementAttr) {
		String txt = elementAttr.getText();
		LogsManager.debug("Texte lu :", txt);
		return txt;
	}
	
	public String readTextWithValue(WebElement elementAttr) {
	    // Essaye d'abord value, sinon ng-reflect-model (Angular)
	    String txt = elementAttr.getAttribute("value");
	    if (txt == null || txt.isEmpty()) {
	        txt = elementAttr.getAttribute("ng-reflect-model");
	    }

	    LogsManager.debug("Texte lu :", txt);
	    return txt;
	}



	// ========================================================================
	// 🧠 UTILITAIRES
	// ========================================================================
	public static void waitFor(int seconds) {
		try {
			LogsManager.debug("Attente de " + seconds + " secondes...");
			Thread.sleep(seconds * 1000L);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			LogsManager.error("Interruption pendant waitFor :", e.getMessage());
		}
	}

	public static void clickWithTimeOut(WebElement element, int timeout) {
		for (int i = 0; i < timeout; i++) {
			try {
				element.click();
				LogsManager.info("ClickWithTimeout réussi après", i + " essais");
				return;
			} catch (WebDriverException e) {
				waitFor(1);
			}
		}
		LogsManager.error("ClickWithTimeout échoué après " + timeout + " secondes");
	}

	// ========================================================================
	// 🔐 ENCODAGE
	// ========================================================================
	public static String encoderString(String realStr) {
		String encoded = Base64.getEncoder().encodeToString(realStr.getBytes());
		LogsManager.debug("Chaîne encodée avec succès");
		return encoded;
	}

	public static String decoderString(String encodedStr) {
		String decoded = new String(Base64.getDecoder().decode(encodedStr));
		LogsManager.debug("Chaîne décodée avec succès");
		return decoded;
	}

	// ========================================================================
	// 📄 LECTURE JSON
	// ========================================================================
	public JSONObject JsonData(int i) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("./src/test/resources/configs/waitConfig.json"));
			JSONArray array = (JSONArray) obj;
			LogsManager.info("Lecture JSON index", String.valueOf(i));
			return (JSONObject) array.get(i);
		} catch (Exception e) {
			LogsManager.error("Erreur lecture JSON :", e.getMessage());
			return null;
		}
	}

	/** Radio. */
	public void checkRadio(WebDriver driver, By by) {
		WebElement inputElement = driver.findElement(by);
		inputElement.click();
	}

	/** Hover. */
	public void mouseHover(WebElement elementAttr) {
		new Actions(driver).moveToElement(elementAttr).perform();
	}

	
}
