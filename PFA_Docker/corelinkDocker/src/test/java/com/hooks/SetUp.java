package com.hooks;

import com.factory.DriverManager;
import com.factory.DriverManagerFactory;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import java.io.File;

public class SetUp {
    // ✅ Un driver par thread
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    @Before
    public void setUp() {
    	
        // ✅ Étape 1 : Créer le dossier des logs (idempotent)
        String logPath = System.getProperty("user.dir") + "/test-outputs/Logs";
        File logDir = new File(logPath);
        if (!logDir.exists()) {
            logDir.mkdirs();
            System.out.println("✅ Dossier des logs créé : " + logDir.getAbsolutePath());
        }

        // ✅ Étape 2 : Initialiser le driver
        String browser = System.getProperty("browser", "chrome");
        DriverManager manager = DriverManagerFactory.getDriverManager(browser);
        WebDriver driver = manager.create();

        driverThread.set(driver); // chaque thread a son driver

    }

    
}
