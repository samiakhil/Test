package com.Saviynt.Pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.Saviynt.FunctionalLibrary.GenericMethods;

public class LoginPage extends GenericMethods {

	// Page Elements section
	@FindBy(how = How.XPATH, using = "//input[@id='username']")
	public static WebElement userNameField;

	@FindBy(how = How.XPATH, using = "//input[@placeholder='Password']")
	public static WebElement password;

	@FindBy(how = How.XPATH, using = "//button[@type='submit']")
	public static WebElement loginButton;

	// Page Commands section
	public void clickOnLoginButton() throws Throwable {

		GenericMethods.sychronizationinterval();
		loginButton.click();
	}

	
	}


