package com.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import com.logs.LogsManager;

public class TearDown {

    @After(order = 1)
    public void dumpBrowserLogs(Scenario scenario) {
        WebDriver driver = SetUp.getDriver();
        if (driver == null) return;

        try {
            var entries = driver.manage().logs().get(LogType.BROWSER);
            if (entries == null) return;

            StringBuilder sb = new StringBuilder();
            for (LogEntry entry : entries) {
                String line = String.format("[BROWSER LOG] %s %s", entry.getLevel(), entry.getMessage());
                LogsManager.warn(line); 
                sb.append(line).append(System.lineSeparator());
            }

            if (sb.isEmpty()) {
                scenario.attach(sb.toString().getBytes(StandardCharsets.UTF_8), "text/plain", "browser-console");
            }
        } catch (Exception e) {
            LogsManager.debug("Aucun log navigateur récupéré (OK pour Firefox ou drivers non Chromium):", e.getMessage());
        }
    }

    @After(order = 0)
    public void attachOnFailAndQuit(Scenario scenario) {
        WebDriver driver = SetUp.getDriver();

        try {
            if (scenario.isFailed() && driver instanceof TakesScreenshot ts) {
                byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "screenshot");
                LogsManager.error("Le scénario a échoué :", scenario.getName());
            } else {
                LogsManager.info("Scénario terminé avec succès :", scenario.getName());
            }
        } catch (Exception e) {
            LogsManager.warn("Impossible de prendre une capture :", e.getMessage());
        }

        try {
            if (driver != null) {
                driver.quit();
                LogsManager.info("Driver fermé correctement pour le scénario :", scenario.getName());
            }
        } catch (Exception e) {
            LogsManager.warn("Erreur lors de la fermeture du driver :", e.getMessage());
        } finally {
            ThreadContext.clearAll();
            LogsManager.debug("ThreadContext nettoyé après exécution du scénario :", scenario.getName());
        }
    }
}
