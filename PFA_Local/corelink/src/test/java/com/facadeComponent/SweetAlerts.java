package com.facadeComponent;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import com.utils.dataReader.PropertyReader;

public class SweetAlerts {


    private final WebDriver driver;
    private final WebDriverWait wait;
    private final PropertyReader config;

    private final By titre   = By.id("swal2-title");
    private final By message = By.id("swal2-html-container");
    private final By ok      = By.cssSelector(".swal2-confirm.swal2-styled");
    private final By annuler = By.cssSelector(".swal2-cancel.swal2-styled");

    public SweetAlerts(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(8));
        this.config = new PropertyReader();
    }
    
    public void waitSweetAlertClosed() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".swal2-container")));
        } catch (TimeoutException | NoSuchElementException ignored) {
        	System.out.println("time out");
        }
    }

    public void assertAlert(String expectedTitle, String expectedMsg, boolean clickOkIfPresent) {
        WebElement t = wait.until(ExpectedConditions.visibilityOfElementLocated(titre));
        WebElement m = wait.until(ExpectedConditions.visibilityOfElementLocated(message));

        Assertions.assertEquals(expectedTitle.trim(), t.getText().trim());
        Assertions.assertEquals(expectedMsg.trim(), m.getText().trim());

        List<WebElement> okButtons = driver.findElements(ok);
        if (clickOkIfPresent && !okButtons.isEmpty()) {
            okButtons.get(0).click();
        }
        waitSweetAlertClosed();
    }
    
    public void asserttitle(String expectedTitle) {
    	WebElement t = wait.until(ExpectedConditions.visibilityOfElementLocated(titre));
        Assertions.assertEquals(expectedTitle.trim(), t.getText().trim());
    }
    
   
    public void valider() {wait.until(ExpectedConditions.elementToBeClickable(ok)).click();}
    public void annuler() {wait.until(ExpectedConditions.elementToBeClickable(this.annuler)).click();}
    
    //hedhy pour wa9et yebda fema mathaln " vous etes sur de supprimer arbia riahi?"
    //gestion user-archivage
    public void assertAlertTemplate(String expectedTitle, String expectedTemplate) {
        WebElement t = wait.until(ExpectedConditions.visibilityOfElementLocated(titre));
        WebElement m = wait.until(ExpectedConditions.visibilityOfElementLocated(message));
        
        String uiMsg = m.getText();
        String matricule = uiMsg.replaceAll(".*utilisateur\\s+([^\\s?]+).*", "$1");
        String expectedMsg = expectedTemplate.replace("{matricule}", matricule);
        Assertions.assertEquals(expectedTitle.trim(), t.getText().trim());
        Assertions.assertEquals(expectedMsg.trim(), uiMsg.trim());
        waitSweetAlertClosed();
    }
    
    
    //forgotResetPassword
    public void assertAlertTemplateForEmail(String expectedTitle, String expectedTemplate) {
        // attendre la visibilité des éléments
        WebElement t = wait.until(ExpectedConditions.visibilityOfElementLocated(titre));
        WebElement m = wait.until(ExpectedConditions.visibilityOfElementLocated(message));

        // lire les valeurs affichées
        String actualTitle = t.getText().trim();
        String actualMessage = m.getText().trim();

        // récupérer l'email depuis le fichier config
        String testEmail = config.getProperties("mailsac.test.email");

        // remplacer {email} dans le template attendu
        String expectedMessage = expectedTemplate.replace("{email}", testEmail);

        // comparer les deux
        Assertions.assertEquals(expectedTitle, actualTitle);
        Assertions.assertEquals(expectedMessage, actualMessage);

        // valider (fermer popup ou continuer le flux)
        valider();
    }


}





