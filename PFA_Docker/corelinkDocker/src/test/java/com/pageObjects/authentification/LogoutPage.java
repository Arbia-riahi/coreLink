package com.pageObjects.authentification;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.actions.*;
import com.pageObjects.BasePage;
import com.dataReader.PropertyReader;


public class LogoutPage extends BasePage {
	public final PropertyReader config;
	private final ElementActions su;
	private final BrowserActions browserActions;

	public LogoutPage(WebDriver driver) {
		super(driver);
		config = new PropertyReader();
		su = new ElementActions();
		browserActions= new BrowserActions(driver);
		

	}

	@FindBy(how = How.XPATH, using = "//a[@id='userDropdown']/div[@class='user-container']")
	public WebElement menuUser;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Déconnexion')]")
	public WebElement deconnexion;

	public void menuUserClick() {
		su.click(menuUser);
	}

	public void logout() {
		su.click(deconnexion);
	}

	public void UrlRestriced(String url) {
		String prop;
		switch (url) {
		case "espaceAdministrateur" -> prop = "url_ADMIN";
		case "espaceManager" -> prop = "url_MANAGER";
		case "espaceMember" -> prop = "url_MEMBER";
		default -> throw new IllegalArgumentException();
		}
		String target = config.getProperties(prop);
		browserActions.navigateTo(target);

	}
}
