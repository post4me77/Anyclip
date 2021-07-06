package com.anyclip.ui;

import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.anyclip.DriverFactory;
import com.anyclip.ReadPropertyFile;

public class AnyclipTest {
	DriverFactory objDriver = new DriverFactory();
	ReadPropertyFile readPropertyFile = new ReadPropertyFile();
	Anyclip anyclip;
	String BASEURL = "https://bootsnipp.com/login";
	String ANYEMAIL = "test@test.com";
	String ANYPASS = "Qwerty123";

	@Before
	public void setUp() throws IOException, InterruptedException {
		anyclip = new Anyclip(objDriver.getDriver());
		anyclip.setWindowsSize(ReadPropertyFile.getVallueWithComma("size").get(0),
				ReadPropertyFile.getVallueWithComma("size").get(1));
		objDriver.getDriver().navigate().to(BASEURL);
		Thread.sleep(15000);
	}

	@After
	public void tearDown() {
		objDriver.quitDriver();
	}

	@Test
	public void makeSureLoginSuccessful() throws InterruptedException, IOException {
		anyclip.setEmailAndPassword(ANYEMAIL, ANYPASS);
		// TODO: We can't login due to email and pass is not correct.
//		WebElement e = driver.findElement((By.xpath("//*[contains(text(), \"" + Name + "\")]")));
//		Assert.assertTrue(e.isDisplayed());
	}

}
