package com.utils.actionsBot;

import com.utils.*;
import com.utils.dataReader.PropertyReader;
import com.utils.logs.LogsManager;
import com.hooks.SetUp;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;

/**
 * The {@code BrowserActions} class provides a centralized abstraction layer
 * for performing common browser-level actions using Selenium WebDriver.
 * <p>
 * It encapsulates key browser operations such as navigation, window/tab
 * management, and page refresh, keeping test logic clean and readable.
 *
 * <p><b>Example usage:</b>
 * <pre>
 *     BrowserActions browser = new BrowserActions(driver);
 *     browser.maximizeWindow();
 *     browser.navigateTo("https://automationpractice.com");
 *     browser.refreshPage();
 * </pre>
 *
 * @author RIAHI arbia
 * @version 1.0
 */
public class BrowserActions {

    private final PropertyReader config = new PropertyReader();
    private final WebDriver driver;
    private final Wait waitManager;

    /**
     * Constructor that initializes BrowserActions with a specific WebDriver instance.
     *
     * @param driver the WebDriver instance used to perform browser actions.
     */
    public BrowserActions(WebDriver driver) {
        this.driver = driver;
        this.waitManager = new Wait(driver);
    }

    /** Maximizes the current browser window. */
    public void maximizeWindow() {
        driver.manage().window().maximize();
        LogsManager.info("Browser window maximized");
    }

    /** Retrieves the current page URL. */
    public String getCurrentUrl() {
        String url = driver.getCurrentUrl();
        LogsManager.info("Current URL: " + url);
        return url;
    }

    /**
     * Opens the specified URL.
     *
     * @param url the URL to navigate to.
     */
    public void navigateTo(String url) {
        driver.get(url);
        LogsManager.info("Navigated to URL: " + url);
    }
    
    public void closeBrowser() {
		driver.quit();
		LogsManager.info("Navigateur fermé proprement.");
	}

    /** Moves the browser forward in its history. */
    public void forward() {
        driver.navigate().forward();
        LogsManager.info("Navigated forward");
    }

    /** Moves the browser backward in its history. */
    public void back() {
        driver.navigate().back();
        LogsManager.info("Navigated back");
    }

    /** Refreshes the current page. */
    public void refreshPage() {
        driver.navigate().refresh();
        LogsManager.info("Page refreshed");
    }

    /** Closes the currently active browser window. */
    public void closeCurrentWindow() {
        driver.close();
        LogsManager.info("Closed current window");
    }

    /** Opens a new browser window and switches to it. */
    public void openNewWindow() {
        driver.switchTo().newWindow(WindowType.WINDOW);
        LogsManager.info("Opened a new browser window");
    }

    /**
     * Closes the browser extension tab if extensions are enabled in configuration.
     * Waits until more than one window is opened, then closes the extension tab and
     * switches back to the main window.
     */
    public void closeExtensionTab() {
        if (config.getProperties("extensions").equalsIgnoreCase("enabled")) {
            String currentWindowHandle = driver.getWindowHandle();

            waitManager.fluentWait().until(
                    d -> d.getWindowHandles().size() > 1
            );

            for (String windowHandle : driver.getWindowHandles()) {
                if (!windowHandle.equals(currentWindowHandle)) {
                    driver.switchTo().window(windowHandle).close();
                }
            }

            driver.switchTo().window(currentWindowHandle);
            LogsManager.info("Extension tab closed");
        }
    }

    /**
     * Switches focus to a browser window that matches the given title.
     * If no match is found, the focus returns to the original window.
     *
     * @param targetTitle the title of the target window to switch to.
     */
    public static void switchToWindow(String targetTitle) {
        String origin = SetUp.getDriver().getWindowHandle();

        for (String handle : SetUp.getDriver().getWindowHandles()) {
            SetUp.getDriver().switchTo().window(handle);
            if (SetUp.getDriver().getTitle().equals(targetTitle)) {
                LogsManager.info("Switched to window with title: " + targetTitle);
                return;
            }
        }

        SetUp.getDriver().switchTo().window(origin);
        LogsManager.warn("Window with title not found: " + targetTitle);
    }

    /** Switches focus to the next browser tab (index 1). */
    public void switchToNextTab() {
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(1));
        LogsManager.info("Switched to next tab");
    }

    /** Closes the current tab and switches to the next available tab. */
    public void closeAndSwitchToNextTab() {
        driver.close();
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(1));
        LogsManager.info("Closed current tab and switched to next");
    }

    /** Switches to the previous tab (index 0). */
    public void switchToPreviousTab() {
        ArrayList<String> tab = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tab.get(0));
        LogsManager.info("Switched to previous tab");
    }
}
