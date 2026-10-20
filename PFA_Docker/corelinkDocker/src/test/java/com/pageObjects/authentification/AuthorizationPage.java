package com.pageObjects.authentification;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.actions.*;
import com.dataReader.PropertyReader;
import com.hooks.*;
import com.pageObjects.BasePage;
import com.utils.*;


public class AuthorizationPage extends BasePage {
	public PropertyReader config;
	private final Wait w;
	private final ElementActions su;
	private final BrowserActions browserActions;

	public AuthorizationPage(WebDriver driver) {
		super(driver);
		config = new PropertyReader();
		w = new Wait(SetUp.getDriver());
		su = new ElementActions();
		browserActions= new BrowserActions(driver);
	}

	@FindBy(how = How.XPATH, using = "//ul[@class='site-menu main-menu js-clone-nav d-flex list-unstyled m-0']/li[4]/a[@class='nav-link']")
	public WebElement loginBtnHome;

	@FindBy(how = How.XPATH, using = "//button[contains(text(),'Retour')]")
	public WebElement btn404Revenir;

	public void loginURLHome(String url) {
		url = config.getProperties("url_CoreLink");
		browserActions.navigateTo(url);
		ElementActions.waitFor(5);
	}

	public void loginURLLogin(String url) {
		url = config.getProperties("url_login");
		browserActions.navigateTo(url);
		ElementActions.waitFor(5);
	}

	public void loginURLAdmin(String url) {
		url = config.getProperties("url_ADMIN");
		browserActions.navigateTo(url);
		ElementActions.waitFor(5);
	}

	public void loginURLManager(String url) {
		url = config.getProperties("url_MANAGER");
		browserActions.navigateTo(url);

	}

	public void loginURLMember(String url) {
		url = config.getProperties("url_MEMBER");
		browserActions.navigateTo(url);

	}

	public void UrlRefuseeAdmin(String key) {
		String prop;
		switch (key) {
		case "url_manager_refusee" -> prop = "url_manager_refusee";
		case "url_member_refusee" -> prop = "url_member_refusee";
		default -> throw new IllegalArgumentException("Clé inconnue: " + key);
		}
		String target = config.getProperties(prop);
		browserActions.navigateTo(target);
		ElementActions.waitFor(5);

	}

	public void UrlRefuseeManager(String key) {
		String prop;
		switch (key) {
		case "url_admin_refusee" -> prop = "url_admin_refusee";
		case "url_member_refusee" -> prop = "url_member_refusee";
		default -> throw new IllegalArgumentException("Clé inconnue: " + key);
		}
		String target = config.getProperties(prop);
		browserActions.navigateTo(target);
		ElementActions.waitFor(5);

	}

	public void UrlRefuseeMember(String key) {
		String prop;
		switch (key) {
		case "url_admin_refusee" -> prop = "url_admin_refusee";
		case "url_manager_refusee" -> prop = "url_manager_refusee";
		default -> throw new IllegalArgumentException("Clé inconnue: " + key);
		}
		String target = config.getProperties(prop);
		browserActions.navigateTo(target);
		ElementActions.waitFor(5);

	}

	public void UrlInvalid(String key) {
		String prop;
		switch (key) {
		case "url_inconnue1" -> prop = "url_inconnue1";
		case "url_inconnue2" -> prop = "url_inconnue2";
		case "url_inconnue3" -> prop = "url_inconnue3";
		case "url_inconnue4" -> prop = "url_inconnue4";
		case "url_inconnue5" -> prop = "url_inconnue5";
		case "url_inconnue6" -> prop = "url_inconnue6";
		default -> throw new IllegalArgumentException("Clé inconnue: " + key);
		}
		String target = config.getProperties(prop);
		browserActions.navigateTo(target);

	}

	public void urlNotFound404(String url) {
		w.urlToBe(url);
	}

	public void revenirArriere() {
		su.click(btn404Revenir);
	}
	
	

}
