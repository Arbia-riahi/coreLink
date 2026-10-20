package com.factory;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverManager implements DriverManager {
	
/**
	 * Configure et retourne les options personnalisées de Chrome.
	 */

	private EdgeOptions getOptions() {

		EdgeOptions options = new EdgeOptions();

		// Arguments pour optimiser et stabiliser les tests
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-notifications");
		options.addArguments("--disable-popup-blocking");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-gpu");
		options.addArguments("--start-maximized");

		// Autoriser les certificats non sécurisés
		options.setAcceptInsecureCerts(true);

		// Définir la stratégie de chargement des pages
		options.setPageLoadStrategy(PageLoadStrategy.EAGER);

		return options;
	}

	@Override
	public WebDriver create() {
		return new EdgeDriver(getOptions());

	}
}





