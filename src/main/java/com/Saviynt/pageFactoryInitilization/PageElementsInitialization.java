package com.Saviynt.pageFactoryInitilization;

import org.openqa.selenium.support.PageFactory;

import com.Saviynt.FunctionalLibrary.GenericMethods;
import com.Saviynt.Pages.LoginPage;


public class PageElementsInitialization extends GenericMethods {

	// Login page elements initialization
	public void loginPageObjectory() {

		PageFactory.initElements(driver, LoginPage.class);
	}

}
