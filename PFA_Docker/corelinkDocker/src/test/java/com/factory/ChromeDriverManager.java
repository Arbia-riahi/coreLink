package com.factory;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

public class ChromeDriverManager implements DriverManager {

    private ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();

        // ✅ Mode headless (stable à partir de Chrome 109+)
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage"); // évite les crashs sous Linux/Windows
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        // ✅ Stratégie de chargement plus rapide
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        // ✅ Accepte les certificats non valides
        options.setAcceptInsecureCerts(true);

        // ✅ (Facultatif) Empêche les boîtes de téléchargement et messages “Chrome is being controlled by automated test software”
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        return options;
    }

    @Override
    public WebDriver create() {
        return new ChromeDriver(getOptions());
    }
}
