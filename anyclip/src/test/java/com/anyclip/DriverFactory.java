package com.anyclip;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {
	private WebDriver driver;
	ReadPropertyFile readPropertyFile= new ReadPropertyFile();

	public DriverFactory() {
		System.setProperty("webdriver.chrome.whitelistedIps", "");
		System.setProperty("webdriver.chrome.driver", "jars/chromedriver.exe");
		 

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void quitDriver() {
		driver.quit();
	}
}
