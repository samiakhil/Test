package com.Saviynt.Scripts;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.Saviynt.FunctionalLibrary.GenericMethods;
import com.Saviynt.Pages.LoginPage;
import com.Saviynt.Utilities.Logs;
import com.Saviynt.Utilities.PropertyUtil;
import com.Saviynt.pageFactoryInitilization.PageElementsInitialization;

public class SaviyntLogin_Test extends GenericMethods {
	
	/* Objects Declaration Section */

	public LoginPage loginPage;
	public PageElementsInitialization elementsInitialization;
	
	/* Test Input Data Section */
	//String url = "loginPageUrl";
	String url ="appUrl";
	String userName = "userName";
	String password = "password";

	/* Launch the browser and navigate the Application */
	@BeforeClass
	@Parameters("browser")
	public void appLaunch(String browser) {
		
		Logs.initLogs(SaviyntLogin_Test.class.getName());
		Logs.startTestCase(this.getClass().getSimpleName());
		
		Logs.info("App Url Navigated");
		GenericMethods.openBrowser(browser);
		GenericMethods.navigateAppUrl(url);
		
		
		loginPage = new LoginPage();
		
		elementsInitialization = new PageElementsInitialization();
		elementsInitialization.loginPageObjectory();
		
	}
	@Test(priority =1, enabled = true)
	public void enterUserCredentials() throws Throwable  {
		
		GenericMethods.sychronizationinterval();
		GenericMethods.getSnapShot("LoginScreen");
		loginPage.userNameField.clear();
		loginPage.userNameField.sendKeys(PropertyUtil.getValueFromKey(userName));
		GenericMethods.sychronizationinterval();
		loginPage.password.clear();
		loginPage.password.sendKeys(PropertyUtil.getValueFromKey(password));
		loginPage.clickOnLoginButton();
		GenericMethods.sychronizationinterval();
		GenericMethods.getSnapShot("HomeScreen");
		
		
	}
	/* Method for quit driver session */
	@AfterClass
	public void quitDriversession() {
		
		GenericMethods.CloseDriverSession();
		Logs.endTestCase(this.getClass().getSimpleName());	
	}

}
