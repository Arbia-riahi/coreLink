package com.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

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
        boolean remote = Boolean.parseBoolean(System.getProperty("remote", "false"));

        try {
            if (remote) {
                System.out.println("🚀 WebDriver Firefox via GRID");
                return new RemoteWebDriver(
                        new URL("http://localhost:4444/wd/hub"),
                        getOptions()
                );
            }

            System.out.println("🖥️ WebDriver Firefox local");
            return new FirefoxDriver(getOptions());

        } catch (Exception e) {
            throw new RuntimeException("Erreur Firefox: " + e.getMessage());
        }
    }

}
