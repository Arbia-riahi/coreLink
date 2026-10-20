package com.utils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.*;
import com.dataReader.PropertyReader;

/**
 * Class Wait.
 */
public class Wait {

	/** driver. */
	private WebDriver driver;
	private PropertyReader config =new PropertyReader();

	/**
	 * Instanciation de wait.
	 *
	 * @param driver
	 */
	public Wait(WebDriver driver) {
		this.driver = driver;
	}

	/**
	 * methode Wait until condition.
	 *
	 * @param condition
	 * @param timeoutMessage
	 * @param timeout
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void waitUntilCondition(ExpectedCondition condition, String timeoutMessage, Duration timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		wait.withMessage(timeoutMessage);
		wait.until(condition);
	}

	 public FluentWait<WebDriver> fluentWait() {
	        return new FluentWait<>(driver)
	                .withTimeout(Duration.ofSeconds(Long.parseLong(config.getProperties("DEFAULT_WAIT"))))
	                .pollingEvery(Duration.ofMillis(100))
	                .ignoreAll(getExceptions());
	        
	    }


	    private ArrayList<Class<? extends Exception>> getExceptions() {
	        ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
	        exceptions.add(NoSuchElementException.class);
	        exceptions.add(StaleElementReferenceException.class);
	        exceptions.add(ElementNotInteractableException.class);
	        exceptions.add(ElementClickInterceptedException.class);
	        return exceptions;
	    }

	/**
	 * method to wait an element to be present.
	 *
	 * @param element to be present
	 */
	public void urlToBe(String expectedUrl) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    wait.until(ExpectedConditions.urlToBe(expectedUrl));
	}
	
	public void urlContains(String expectedUrl) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));
	    wait.until(ExpectedConditions.urlContains(expectedUrl));
	}
	



	/**
	 * method For loading.
	 *
	 * @param timeout
	 */
	public void forLoading(Duration timeout) {
		ExpectedCondition<Object> condition = ExpectedConditions
				.jsReturnsValue("return document.readyState==\"complete\";");
		String timeoutMessage = "Page didn't load after " + timeout + " seconds.";
		waitUntilCondition(condition, timeoutMessage, timeout);
	}
	
	/**
	 * method For refresh And attend visiblity of the total page.
	 *
	 * @param timeout
	 */
	public void refreshAndWait(Duration timeout) {
	    driver.navigate().refresh();
	    forLoading(timeout);
	}

	/**
	 * method For element to be displayed.
	 *
	 * @param timeout
	 * @param webElement
	 * @param webElementName
	 */
	public void forElementToBeDisplayed(Duration timeout, WebElement webElement, String webElementName) {
		ExpectedCondition<WebElement> condition = ExpectedConditions.visibilityOf(webElement);
		String timeoutMessage = webElementName + " wasn't displayed after " + timeout + " seconds.";
		waitUntilCondition(condition, timeoutMessage, timeout);
	}

	/**
	 * method For presence of elements.
	 *
	 * @param timeout
	 * @param elementLocator
	 * @param elementName
	 */
	public void forPresenceOfElements(Duration timeout, By elementLocator, String elementName) {
		ExpectedCondition<List<WebElement>> condition = ExpectedConditions
				.presenceOfAllElementsLocatedBy(elementLocator);
		String timeoutMessage = elementName + " elements were not displayed after " + timeout + " seconds.";
		waitUntilCondition(condition, timeoutMessage, timeout);
	}
	
	public WebElement forVisibleElement(Duration timeout, By locator) {
	    WebDriverWait wait = new WebDriverWait(driver, timeout);
	    wait.ignoring(StaleElementReferenceException.class);
	    return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
	}


	/**
	 * methods Wait until clickable.
	 *
	 * @param driver
	 * @param by
	 * @return web element
	 */
	public static WebElement waitUntilClickable(WebDriver driver, By by) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		return wait.until(ExpectedConditions.elementToBeClickable(by));
	}


	
	/**
	 * method Wait until value input get visible.
	 *
	 * @param driver
	 * @param by
	 * @return web element
	 */
	public String forValueNotEmpty(WebElement element, Duration timeout) {
	    WebDriverWait wait = new WebDriverWait(driver, timeout);
	    return wait.until(d -> {
	        String value = element.getAttribute("value");
	        return (value != null && !value.isBlank()) ? value : null;
	    });
	}



}