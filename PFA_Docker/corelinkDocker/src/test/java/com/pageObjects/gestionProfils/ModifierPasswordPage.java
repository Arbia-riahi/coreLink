package com.pageObjects.gestionProfils;

import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.actions.ElementActions;
import com.dataReader.PropertyReader;
import com.pageObjects.BasePage;
import com.utils.Validations;

public class ModifierPasswordPage extends BasePage {
	public PropertyReader config;
	private final ElementActions su;

	public ModifierPasswordPage(WebDriver driver) {

		super(driver);
		this.config = new PropertyReader();
		su = new ElementActions(); // SeleniumUtils utilise SetUp.getDriver() en interne
	}

	@FindBy(how = How.ID, using = "oldPassword")
	public WebElement ancienPassword;

	@FindBy(how = How.ID, using = "newPassword")
	public WebElement nvPassword;

	@FindBy(how = How.ID, using = "confirmPassword")
	public WebElement confirmPassword;
	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Mon profil')]")
	public WebElement btnMonProfil;

	@FindBy(how = How.XPATH, using = "//nav[@class='tabs']/a[@class='tabs__item']")
	public WebElement btnUpdatePassword;

	@FindBy(how = How.CSS, using = " .btn.btn-gradient.w-100.py-2.rounded-pill")
	public WebElement btnConfirmUpdatePassword;

	@FindBy(how = How.XPATH, using = "//a[@id='userDropdown']/div[@class='user-container']")
	public WebElement btnmenuUser;

	public void saisirAncienPassword(String ap) {
		su.writeText(ancienPassword, Validations.nullToEmpty(ap));

	}

	public void saisirNvPassword(String np) {
		su.writeText(nvPassword, Validations.nullToEmpty(np));

	}

	public void saisirConfirmPassword(String cp) {
		su.writeText(confirmPassword, Validations.nullToEmpty(cp));

	}

	public void clickByLabel(String rawLabel) {
		ElementActions.waitFor(5);
		String label = Validations.nullToEmpty(rawLabel).trim();
		if (label.isEmpty() || "[empty]".equalsIgnoreCase(label))
			return;
		WebElement target = switch (label) {
		case "Mon Profile" -> btnMonProfil;
		case "Changer mot de passe" -> btnUpdatePassword;
		default -> throw new IllegalArgumentException("Bouton non géré: " + label);
		};
		su.click(target);

	}

	public void clickMenuUser() {
		su.click(btnmenuUser);

	}

	public void fillFormUpdatePassword(Map<String, String> m) {
		if (m == null)
			return;
		if (m.containsKey("ancien password"))
			saisirAncienPassword(m.get("ancien password"));
		if (m.containsKey("nouveau mot de passe"))
			saisirNvPassword(m.get("nouveau mot de passe"));
		if (m.containsKey("confirm nouveau mot de passe"))
      		saisirConfirmPassword(m.get("confirm nouveau mot de passe"));
	}

}
