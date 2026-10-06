package com.anyclip;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {
	private WebDriver driver;
	ReadPropertyFile readPropertyFile= new ReadPropertyFile();

	public DriverFactory() {
		configureChromeDriver();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}

	private static synchronized void configureChromeDriver() {
		if (System.getProperty("webdriver.chrome.driver") != null) {
			return;
		}
		if (System.getProperty("os.name").startsWith("Mac")
				&& ("aarch64".equals(System.getProperty("os.arch"))
						|| "arm64".equals(System.getProperty("os.arch")))) {
			String relativePath = "jars/chromedriver-mac-arm64/chromedriver";
			File executable = new File(relativePath);
			if (!executable.isFile()) {
				executable = new File("anyclip", relativePath);
			}
			if (executable.canExecute()) {
				System.setProperty("webdriver.chrome.driver", executable.getAbsolutePath());
			}
		}
	}

	public WebDriver getDriver() {
		return driver;
	}

	public void quitDriver() {
		driver.quit();
	}
}
