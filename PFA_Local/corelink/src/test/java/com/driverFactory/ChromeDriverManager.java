package com.driverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class ChromeDriverManager implements DriverManager {

    private ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();

        //options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-extensions");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");

        options.setAcceptInsecureCerts(true);
        return options;
    }

    @Override
    public WebDriver create() {
        boolean remote = Boolean.parseBoolean(System.getProperty("remote", "false"));

        try {
            if (remote) {
                System.out.println("🚀 WebDriver Chrome lancé via Selenium GRID");
                return new RemoteWebDriver(
                        new URL("http://localhost:4444/wd/hub"),  // URL du HUB
                        getOptions()
                );
            }

            System.out.println("🖥️ WebDriver Chrome lancé en mode local");
            return new ChromeDriver(getOptions());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lancement ChromeDriver: " + e.getMessage());
        }
    }
}
