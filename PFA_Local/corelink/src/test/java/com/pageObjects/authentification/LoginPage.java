package com.pageObjects.authentification;

import com.hooks.*;
import com.pageObjects.BasePage;
import com.utils.Wait;
import com.utils.actionsBot.ElementActions;
import com.utils.dataReader.PropertyReader;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class LoginPage extends BasePage {

	public PropertyReader config;
	public final Wait w;
	public final ElementActions su;

	public LoginPage(WebDriver driver) {

		super(driver);
		this.su = new ElementActions();
		config = new PropertyReader();
		this.w = new Wait(driver);
	}

	@FindBy(how = How.XPATH, using = "//ul[@class='site-menu main-menu js-clone-nav d-flex list-unstyled m-0']/li[3]/a[@class='nav-link']")
    public WebElement loginBtnHome;

	@FindBy(how = How.NAME, using = "username")
    public WebElement matricule;// nhotouha fi wost un webElment

	@FindBy(how = How.NAME, using = "password")
	public WebElement password;

	@FindBy(how = How.XPATH, using = "//div[@class='container']/div[2]/form/button")
	public WebElement seConnecter;
	
	@FindBy(how = How.XPATH, using = "//div[@class='form-group']//i")
	public WebElement iconMasquage;

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Espace Administrateur')]")
	public WebElement espaceAdmin;
	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Espace Chef de Projet')]")
	public WebElement espaceManager;
	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Espace collaborateurs')]")
	public WebElement espaceMember;

	public void loginURLHome(String url) {
		url = config.getProperties("url_CoreLink");
		SetUp.getDriver().get(url);
		w.urlToBe(url);
	}

	public void loginURLLogin(String url) {
		url = config.getProperties("url_login");
	}

	public void login(String url) {
		url = config.getProperties("url_login");
		SetUp.getDriver().get(url);
		w.urlToBe(url);
		

	}

	public void verifURLContenu(String url) {
		w.urlContains(url);
		
	}

	public void saisirMatricule(String id) {
		w.forElementToBeDisplayed(Duration.ofSeconds(6), matricule, id);
		su.clearField(matricule);
		su.writeText(matricule, id);
	}

	public void saisirPassword(String pwd) { 
        w.forElementToBeDisplayed(Duration.ofSeconds(5), password, pwd);
		su.clearField(password);
		su.writeText(password, pwd);

	}

	public void clickBtn() { su.click(seConnecter);}
	
	public void clickBtnHome() {su.click(loginBtnHome);}
	
	public void verifDashTitleAfterLogin(WebElement element) {
		w.forElementToBeDisplayed(Duration.ofSeconds(6), element, "dashboard");
		
	}
	

	public void connexion(String matriculeArgs, String passwordArgs) {
		SetUp.getDriver().get(config.getProperties("url_login"));
		
		w.forElementToBeDisplayed(Duration.ofSeconds(5), matricule, matriculeArgs);
		su.writeText(matricule, matriculeArgs);
		
		su.writeText(password, passwordArgs);
		
	    clickBtn();
	    
	}
	
	public void clickBtnMasquage() {
		su.click(iconMasquage);
	}

	
	public String readTextPassword() {
	    return su.readTextWithValue(password);
	}

	public String getPasswordAttribute() {
	    return password.getAttribute("type");
	}


}
