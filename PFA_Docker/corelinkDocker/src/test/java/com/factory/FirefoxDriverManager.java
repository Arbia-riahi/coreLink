package com.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager implements DriverManager {

    private FirefoxOptions getOptions() {
        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--start-maximized");

        // ✅ Toujours en mode headless (local + CI)
        options.addArguments("--headless");

        // Autoriser les certificats non sécurisés
        options.setAcceptInsecureCerts(true);

        return options;
    }

    @Override
    public WebDriver create() {
        // ✅ WebDriverManager gère tout, aucun téléchargement manuel nécessaire
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(getOptions());
    }
}
