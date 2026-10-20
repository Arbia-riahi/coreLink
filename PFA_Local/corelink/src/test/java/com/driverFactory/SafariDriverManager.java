package com.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

/**
 * Implémentation du DriverManager pour le navigateur Safari.
 * Gère la configuration et la création d'une instance de SafariDriver.
 */
public class SafariDriverManager implements DriverManager {

    /**
     * Configure et retourne les options personnalisées pour Safari.
     */
    private SafariOptions getOptions() {
        SafariOptions options = new SafariOptions();

        // Autoriser les certificats non sécurisés
        options.setAcceptInsecureCerts(true);

        // Activer la technologie W3C (meilleure compatibilité avec Selenium 4+)
        options.setUseTechnologyPreview(false);

        return options;
    }

    /**
     * Crée et retourne une instance de SafariDriver avec les options configurées.
     */
    @Override
    public WebDriver create() {
        return new SafariDriver(getOptions());
    }
}