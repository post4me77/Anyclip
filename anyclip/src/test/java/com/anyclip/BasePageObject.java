package com.anyclip;

import java.io.IOException;
import java.util.ArrayList;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePageObject {
	protected static WebDriver driver;

	public BasePageObject(WebDriver driver) {
		super();
		BasePageObject.driver = driver;
	}

	public static void waitUntilElementIsLoaded(WebElement element) throws IOException, InterruptedException {
		new WebDriverWait(driver, 15).until(ExpectedConditions.visibilityOf(element));
	}

	public void setWindowsSize(int x, int y) {
		Dimension newSize = new Dimension(x, y);
		// Resize current window to the set dimension
		driver.manage().window().setSize(newSize);
	}

	public void swithOnNewTab() {
		ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
		driver.switchTo().window(tabs2.get(1));
	}
}
