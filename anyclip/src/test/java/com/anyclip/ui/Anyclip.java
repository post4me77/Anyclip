package com.anyclip.ui;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.anyclip.BasePageObject;

public class Anyclip extends BasePageObject {

	public Anyclip(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//*[@class=\"btn-success\"]")
	WebElement submitButton;

	@FindBy(xpath = "//*[contains(@name,'email')]")
	WebElement emailField;

	@FindBy(xpath = "//*[contains(@name,'password')]")
	WebElement passwordField;

	@FindBy(xpath = "//*[contains(@name,'password')]")
	WebElement rememberCheckBoxButton;

	public void setElementText(WebElement element, String text) throws IOException, InterruptedException {
		waitUntilElementIsLoaded(element);
		element.sendKeys(text);
	}

	public void setEmailAndPassword(String textEmail, String textPassword) throws IOException, InterruptedException {
		setElementText(emailField, textEmail);
		setElementText(passwordField, textPassword);
		waitUntilElementIsLoaded(rememberCheckBoxButton);
		rememberCheckBoxButton.click();
		waitUntilElementIsLoaded(submitButton);
		submitButton.click();
	}

	public void waitForElement(WebElement element) throws IOException, InterruptedException {
		waitUntilElementIsLoaded(element);
	}
}
