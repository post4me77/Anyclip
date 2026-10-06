package com.anyclip.ui;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.anyclip.DriverFactory;
import com.anyclip.ParallelTestRunner;
import com.anyclip.ReadPropertyFile;

@RunWith(ParallelTestRunner.class)
public class AnyclipTest {
	DriverFactory objDriver;
	Anyclip anyclip;
	String BASEURL = "https://bootsnipp.com/login";
	String ANYEMAIL = "test@test.com";
	String BYPASS = "Qwerty123";

	@Before
	public void setUp() throws IOException, InterruptedException {
		objDriver = new DriverFactory();
		anyclip = new Anyclip(objDriver.getDriver());
		anyclip.setWindowsSize(ReadPropertyFile.getVallueWithComma("size").get(0),
				ReadPropertyFile.getVallueWithComma("size").get(1));
		objDriver.getDriver().navigate().to(BASEURL);
	}

	@After
	public void tearDown() {
		if (objDriver != null) {
			objDriver.quitDriver();
		}
	}

	@Test
	public void makeSureInvalidCredentialsMessageIsDisplayed() throws InterruptedException, IOException {
		anyclip.setEmailAndPassword(ANYEMAIL, BYPASS);
		Assert.assertEquals("E-mail or password was incorrect, please try again",
				anyclip.getLoginErrorMessage());
	}

	@Test
	public void loginFormIsDisplayed() {
		Assert.assertTrue("Login fields and submit button must be visible", anyclip.isLoginFormDisplayed());
	}

	@Test
	public void passwordIsMasked() {
		Assert.assertEquals("password", anyclip.getPasswordFieldType());
	}

	@Test
	public void rememberMeCanBeSelectedAndCleared() {
		Assert.assertFalse(anyclip.isRememberMeSelected());
		anyclip.toggleRememberMe();
		Assert.assertTrue(anyclip.isRememberMeSelected());
		anyclip.toggleRememberMe();
		Assert.assertFalse(anyclip.isRememberMeSelected());
	}

	@Test
	public void forgotPasswordLinkOpensRecoveryPage() {
		anyclip.openForgotPassword();
		Assert.assertEquals("https://bootsnipp.com/password", anyclip.getCurrentUrl());
	}

	@Test
	public void registrationLinkOpensRegistrationPage() {
		anyclip.openRegistration();
		Assert.assertEquals("https://bootsnipp.com/register", anyclip.getCurrentUrl());
	}

	@Test
	public void emptyCredentialsAreRejected() throws IOException, InterruptedException {
		assertLoginRejected("", "");
	}

	@Test
	public void emptyEmailIsRejected() throws IOException, InterruptedException {
		assertLoginRejected("", BYPASS);
	}

	@Test
	public void emptyPasswordIsRejected() throws IOException, InterruptedException {
		assertLoginRejected(ANYEMAIL, "");
	}

	@Test
	public void malformedEmailIsRejected() throws IOException, InterruptedException {
		assertLoginRejected("invalid-email", BYPASS);
	}

	@Test
	public void unknownAccountWithRememberMeIsRejected() throws IOException, InterruptedException {
		anyclip.toggleRememberMe();
		assertLoginRejected("ui-test-" + java.util.UUID.randomUUID() + "@example.invalid", BYPASS);
	}

	private void assertLoginRejected(String email, String password) throws IOException, InterruptedException {
		anyclip.setEmailAndPassword(email, password);
		Assert.assertEquals("E-mail or password was incorrect, please try again", anyclip.getLoginErrorMessage());
		Assert.assertEquals(BASEURL, anyclip.getCurrentUrl());
		Assert.assertTrue("Login form must remain available after rejected login", anyclip.isLoginFormDisplayed());
	}

}
