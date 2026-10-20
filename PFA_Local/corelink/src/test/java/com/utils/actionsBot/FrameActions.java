package com.utils.actionsBot;

import com.utils.*;
import com.utils.logs.LogsManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FrameActions {
	  private final Wait waitManager;

	    public FrameActions(WebDriver driver) {
	        this.waitManager = new Wait(driver);
	    }

	    /**
	     * Switches to a frame by index
	     *
	     * @param index the index of the frame
	     */
	    public void switchToFrameByIndex(int index) {
	        waitManager.fluentWait().until(d -> {
	            try {
	                d.switchTo().frame(index);
	                LogsManager.info("Switched to frame by index: " + index);
	                return true;
	            } catch (Exception e) {
	                return false;
	            }
	        });
	    }

	    /**
	     * Switches to a frame by name or ID
	     *
	     * @param nameOrId the name or ID of the frame
	     */
	    public void switchToFrameByNameOrId(String nameOrId) {
	        waitManager.fluentWait().until(d -> {
	            try {
	                d.switchTo().frame(nameOrId);
	                LogsManager.info("Switched to frame by name or ID: " + nameOrId);
	                return true;
	            } catch (Exception e) {
	                return false;
	            }
	        });
	    }

	    /**
	     * Switches to a frame by WebElement
	     */
	    public void switchToFrameByElement(By frameLocator) {
	        waitManager.fluentWait().until(d -> {
	            try {
	                d.switchTo().frame(d.findElement(frameLocator));
	                LogsManager.info("Switched to frame by element: " + frameLocator);
	                return true;
	            } catch (Exception e) {
	                return false;
	            }
	        });
	    }

	    /**
	     * Switches back to the default content
	     */
	    public void switchToDefaultContent() {
	        waitManager.fluentWait().until(d -> {
	            try {
	                d.switchTo().defaultContent();
	                LogsManager.info("Switched to default content");
	                return true;
	            } catch (Exception e) {
	                return false;
	            }
	        });
	    }
	}

