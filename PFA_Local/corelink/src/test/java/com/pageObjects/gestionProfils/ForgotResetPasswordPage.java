package com.pageObjects.gestionProfils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.pageObjects.BasePage;
import com.utils.*;
import com.utils.actionsBot.ElementActions;
import com.utils.dataReader.PropertyReader;

public class ForgotResetPasswordPage extends BasePage {
		public PropertyReader config;
		private final ElementActions su;

		public ForgotResetPasswordPage(WebDriver driver) {
			super(driver);
			this.config = new PropertyReader();
			su = new ElementActions();
		}
		
		@FindBy(how = How.XPATH, using = "//div[@class='text-center mt-3']//a//span[contains(text(), 'Mot de passe oublié')]")
		public WebElement linkForgotPassword;
		
		@FindBy(how = How.ID, using = "email")
		public WebElement email;
		
		@FindBy(how = How.CSS, using = ".btn.btn-lg.btn-cta")
		public WebElement sendMailBtn;
		
		@FindBy(how = How.CSS, using = ".btn.btn-warning.rounded-pill")
		public WebElement resetMailBtn;
		
		@FindBy(how = How.NAME, using = "password")
		public WebElement nvPassword;
		
		@FindBy(how = How.NAME, using = "confirm")
		public WebElement confirmNvPassword;
		
		
		public void saisirEmail(String e) {
			su.writeText(email, Validations.nullToEmpty(e));
		}
		public void saisirNvPassword(String p) {
			su.writeText(nvPassword, Validations.nullToEmpty(p));
		}
		public void saisirConfirmNvPassword(String p) {
			su.writeText(confirmNvPassword, Validations.nullToEmpty(p));
		}
		
		
		
		public void clickByLabel(String rawLabel) {
			String label = Validations.nullToEmpty(rawLabel).trim();
			if (label.isEmpty() || "[empty]".equalsIgnoreCase(label))
				return;
			switch (label) {
			case "Mot de passe oublié ?" -> {su.click(linkForgotPassword); ElementActions.waitFor(20);}
			case "Envoyer le lien" -> {su.click(sendMailBtn);ElementActions.waitFor(50);}
			case "Réinitialiser" -> {su.click(resetMailBtn);ElementActions.waitFor(50);}
			default -> throw new IllegalArgumentException("Bouton non géré: " + label);
			};	
		}

}
