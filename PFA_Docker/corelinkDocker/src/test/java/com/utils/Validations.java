package com.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.hooks.SetUp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import com.pageObjects.*;


public final class Validations extends BasePage {

	/** testCaseStatus the status of the test case. */
	boolean testCaseStatus = true;

	/** test status. */
	boolean testStatus;

	/**
	 * Instanciation de assertions.
	 */
	public Validations() {
		super(SetUp.getDriver());
	}



	/**
	 * method verify whether element is present on screen.
	 *
	 * @param targetElement element to be present
	 * @return true if element is present else throws exception
	 */
	public static Boolean isElementPresent(By targetElement) {
		return SetUp.getDriver().findElements(targetElement).size() > 0;
	}

	/**
	 * methode Checks if is element displayed.
	 *
	 * @param element element web
	 * @return boolean
	 */
	public static Boolean isElementDisplayed(WebElement element) {
		return element.isDisplayed();
	}

   
    /**
	 * methode Checks if is element enabled.
	 *
	 * @param element
	 * @return boolean
	 */
	public static Boolean isElementEnabled(WebElement element) {
		return element.isEnabled();
	}

    /** Vrai si l’élément est selected. */
	/**
	 * methode Checks if is element selected
	 *
	 * @param element
	 * @return boolean
	 */
	public static Boolean isElementSelected(WebElement element) {
		return element.isSelected();
	}

    /** Présence d’au moins un élément pour le locator. */
    public static boolean isElementPresent(WebDriver driver, By by) {
        Objects.requireNonNull(driver, "driver");
        return driver.findElements(by).size() > 0;
    }
   

    /** Absence totale d’élément pour le locator. */
    public static boolean isElementNotPresent(WebDriver driver, By by) {
        Objects.requireNonNull(driver, "driver");
        return driver.findElements(by).isEmpty();
    }

    /**
     * Champ saisissable = visible + enabled + pas readonly/aria-disabled/contenteditable=false
     * + input non “non-saisissable”.
     */
    public static boolean isEditable(WebElement e) {
        if (!isElementEnabled(e)) return false;
        String ro = attr(e, "readonly");
        if ("true".equalsIgnoreCase(ro) || "readonly".equalsIgnoreCase(ro)) return false;
        if ("true".equalsIgnoreCase(attr(e, "aria-disabled"))) return false;
        if ("false".equalsIgnoreCase(attr(e, "contenteditable"))) return false;

        if ("input".equals(tag(e))) {
            String t = lower(attr(e, "type"));
            if (Arrays.asList("hidden", "button", "submit", "reset", "image", "radio", "checkbox").contains(t)) {
                return false;
            }
        }
        return true;
    }

    /* =========================
     *          STRINGS
     * ========================= */
    public static boolean notBlank(String s) { return s != null && !s.trim().isEmpty(); }
    
    //méthode utilitaire Java qui remplace les null par une chaîne vide.
    public static String nullToEmpty(String s){ return s == null ? "" : s; }
    /**
	 * methode Check field is empty.
	 * 
	 * @param elementAttr
	 */
    //une méthode qui vérifie si un champ WebElement est vide dans le navigateur,
	public void checkFieldIsEmpty(WebElement elementAttr) {
		WebElement inputText = elementAttr;
		String text = inputText.getAttribute("value");

		if (text.isEmpty()) {
			logger.info("input box is empty");
		}
	}

    /** Premier non null/non blank, sinon null. */
    public static String firstNonNull(String... vals) {
        if (vals != null) for (String v : vals) if (notBlank(v)) return v;
        return null;
    }

    /** Normalisation insensitive (sans accents, lowercase, trim). */
    public static String norm(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}+", "");
        return n.toLowerCase(Locale.ROOT).trim();
    }

  

    /* =========================
     *     FILE / CONTENT CHECKS
     * ========================= */

    /** Capture un screenshot et retourne le chemin final. Le dossier est créé si besoin. */
    public static Path screenShot(WebDriver driver, Path saveDir) {
        Objects.requireNonNull(driver, "driver");
        Objects.requireNonNull(saveDir, "saveDir");
        if (!(driver instanceof TakesScreenshot ts)) {
            throw new IllegalArgumentException("Driver does not support screenshots");
        }
        try {
            Files.createDirectories(saveDir);
            String name = "screenshot-" + new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.FRANCE).format(new Date()) + ".png";
            Path target = saveDir.resolve(name);
            byte[] data = ts.getScreenshotAs(OutputType.BYTES);
            Files.write(target, data);
            return target;
        } catch (IOException ioe) {
            throw new RuntimeException("Failed to save screenshot", ioe);
        }
    }

/** Retourne true si la colonne (0-based) contient la valeur cible dans un CSV. */
    public static boolean columnContainsValue(String pathToFile, int columnIndex, String targetValue) throws IOException {
        Objects.requireNonNull(pathToFile, "pathToFile");
        Objects.requireNonNull(targetValue, "targetValue");

        try (Reader in = new FileReader(pathToFile)) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
            for (CSVRecord r : records) {
                if (r.size() > columnIndex) {
                    String rowValue = r.get(columnIndex);
                    if (targetValue.equals(rowValue)) return true;
                }
            }
            return false;
        }
    }

    /* =========================
     *        INTERNAL HELPERS
     * ========================= */

    
    private static String attr(WebElement e, String name) {
        try { return e == null ? null : e.getAttribute(name); }
        catch (Exception ex) { return null; }
    }

    private static String tag(WebElement e) {
        try { return e == null ? "" : e.getTagName().toLowerCase(Locale.ROOT); }
        catch (Exception ex) { return ""; }
    }

    private static String lower(String s) {
        return s == null ? "" : s.toLowerCase(Locale.ROOT);
    }
}
