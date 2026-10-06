package com.anyclip;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePageObject {
	private static final int WAIT_TIMEOUT_SECONDS = 15;
	private static final int POLLING_INTERVAL_MILLIS = 200;
	protected final WebDriver driver;
	private final WebDriverWait wait;

	public BasePageObject(WebDriver driver) {
		if (driver == null) {
			throw new IllegalArgumentException("Driver must not be null");
		}
		this.driver = driver;
		this.wait = new WebDriverWait(driver, WAIT_TIMEOUT_SECONDS, POLLING_INTERVAL_MILLIS);
	}

	public void waitUntilElementIsLoaded(WebElement element) {
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	protected WebElement waitUntilElementIsClickable(WebElement element) {
		return wait.until(ExpectedConditions.elementToBeClickable(element));
	}

	public void setWindowsSize(int x, int y) {
		if (x <= 0 || y <= 0) {
			throw new IllegalArgumentException("Window dimensions must be positive");
		}
		driver.manage().window().setSize(new Dimension(x, y));
	}

	public void switchToNewTab() {
		final String currentWindow = driver.getWindowHandle();
		String targetWindow = wait.until(webDriver -> {
			for (String handle : webDriver.getWindowHandles()) {
				if (!handle.equals(currentWindow)) {
					return handle;
				}
			}
			return null;
		});
		driver.switchTo().window(targetWindow);
	}

	/** @deprecated Use {@link #switchToNewTab()} instead. */
	@Deprecated
	public void swithOnNewTab() {
		switchToNewTab();
	}
}
