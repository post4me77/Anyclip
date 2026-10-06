package com.anyclip.ui;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.anyclip.BasePageObject;

public class Anyclip extends BasePageObject {

	public Anyclip(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(css = "button[type='submit'], input[type='submit']")
	WebElement submitButton;

	@FindBy(xpath = "//*[contains(@name,'email')]")
	WebElement emailField;

	@FindBy(xpath = "//*[contains(@name,'password')]")
	WebElement passwordField;

	@FindBy(css = "input[type='checkbox'][name='remember']")
	WebElement rememberCheckBoxButton;

	public void setElementText(WebElement element, String text) throws IOException, InterruptedException {
		WebElement field = waitUntilElementIsClickable(element);
		field.clear();
		field.sendKeys(text);
	}

	public void setEmailAndPassword(String textEmail, String textPassword) throws IOException, InterruptedException {
		setElementText(emailField, textEmail);
		setElementText(passwordField, textPassword);
		waitUntilElementIsClickable(submitButton).click();
	}

	@FindBy(css = "[role='alert'] h5")
	WebElement loginErrorMessage;

	public String getLoginErrorMessage() {
		waitUntilElementIsLoaded(loginErrorMessage);
		return loginErrorMessage.getText().trim();
	}

	public boolean isLoginFormDisplayed() {
		waitUntilElementIsLoaded(emailField);
		return emailField.isDisplayed() && passwordField.isDisplayed() && submitButton.isDisplayed();
	}

	public String getPasswordFieldType() {
		waitUntilElementIsLoaded(passwordField);
		return passwordField.getAttribute("type");
	}

	public void toggleRememberMe() {
		waitUntilElementIsClickable(rememberCheckBoxButton).click();
	}

	public boolean isRememberMeSelected() {
		waitUntilElementIsLoaded(rememberCheckBoxButton);
		return rememberCheckBoxButton.isSelected();
	}

	public void openForgotPassword() {
		openLink("#loginform a[href='https://bootsnipp.com/password']", "/password");
	}

	public void openRegistration() {
		openLink("#loginform a[href='https://bootsnipp.com/register']", "/register");
	}

	private void openLink(String selector, String path) {
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(selector))).click();
		wait.until(ExpectedConditions.urlToBe("https://bootsnipp.com" + path));
	}

	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	public void waitForElement(WebElement element) throws IOException, InterruptedException {
		waitUntilElementIsLoaded(element);
	}
}
