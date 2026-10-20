package com.pageObjects.gestionProjets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import com.pageObjects.BasePage;
import com.actions.*;
import com.dataReader.PropertyReader;

public class ArchivageProjectPage extends BasePage {

	public PropertyReader config;
	private final ElementActions su;
	
    public ArchivageProjectPage(WebDriver driver) {
		super(driver);
		this.config = new PropertyReader();
		this.su = new ElementActions();
	}

	@FindBy(how = How.XPATH, using = "//span[contains(text(),'Pilotage des projets')]")
	public WebElement pilotageBtn;

	@FindBy(how = How.XPATH, using = "//a[contains(text(),'Liste')]")
	public WebElement listeBtn;

	@FindBy(how = How.XPATH, using = "//input[@placeholder='Rechercher par titre, description, chef, statut...']")
	public WebElement searchZone;

	@FindBy(how = How.CSS, using = ".text-center.text-muted.py-4")
	public WebElement msgErreurAucunFiltreResult;

	public void clickPilotageProjets() {
		su.click(pilotageBtn);
	}

	public void clickListProjet() {
		su.click(listeBtn);
	}

	public WebElement getBoutonActionParTitle(String titre, String action) {
		String cls = switch (action) {
		case "archiver" -> "btn btn-outline-danger btn-sm rounded-circle";
		default -> throw new IllegalArgumentException("Action inconnue : " + action);
		};
		String xpath = "//tr[td[text()='" + titre + "']]//button[contains(@class,'" + cls + "')]";
		By by = By.xpath(xpath);
		WebElement bouton = su.findElement(by);
		su.scrollIntoView(bouton);
		return bouton;
	}

	public void filter(String q) {
		searchZone.click();
		su.writeText(searchZone, q == null ? "" : q.toLowerCase());
		ElementActions.waitFor(5);
	}

	public boolean trySelectByFilter(String titre) {
        filter(titre);
		return true;

	}

}
