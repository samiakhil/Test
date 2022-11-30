package com.Saviynt.FunctionalLibrary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.Saviynt.Utilities.ConstantUtils;
import com.Saviynt.Utilities.PropertyUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class GenericMethods {
	public static WebDriver driver;
	public static InternetExplorerOptions capabilities;

	/* Launches Required Browser */
	public static WebDriver openBrowser(String browser) {

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("IE")) {
			capabilities = new InternetExplorerOptions();
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			System.setProperty("webdriver.ie.driver", ConstantUtils.ieServerPath);
			driver = new InternetExplorerDriver(capabilities);
		} else if (browser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();

		}
		return driver;
	}

	// method for verifying alert
	public static boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	/* Navigate Application Url */
	public static void navigateAppUrl(String url) {
		driver.manage().deleteAllCookies();
		driver.get(PropertyUtil.getValueFromKey(url));
		driver.manage().window().maximize();
	}
	

	/* closing WebDriver Session */
	public static void CloseDriverSession() {
		driver.quit();
	}

	/* get Current Application Page Url */
	public static String titleValidation() {
		return driver.getTitle();
	}

	/* Reusable method to validate if element is available in application */
	public static boolean elementToBePresent(WebElement element) {
		return element.isDisplayed();
	}

	/* Reusable method to get text of element */
	public static String getTextOfElement(WebElement element) {
		return element.getText();
	}

	/* Reusable method to validate if element is selected in application */
	public static boolean elementToBeSelect(WebElement element) {
		return element.isSelected();
	}

	/* Reusable method to validate if element is Enable in application */
	public static boolean elementToBeEnable(WebElement element) {
		return element.isEnabled();
	}

	/* Reusable method for get ScreenShot */
	public static void getSnapShot(String testName) {
		File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		File dest = new File(ConstantUtils.screenShotPath + testName + " " + generateDate() + " " + ".png");
			
		try {
			FileUtils.copyFile(screenshotFile, dest);
			Reporter.log("<a href='" + dest + "' target=_blank'>Click for Screenshot</a>");
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}

	/* Method for get Current Date with Time */
	public static String generateDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY_MM_dd" + " " + "_HH_mm_ss");
		return sdf.format(date);
	}

	/* Reusable Wait method */
	public static void waitForElement(WebElement element) {
		//WebDriverWait wait = new WebDriverWait(driver, Integer.parseInt(ConstantUtils.waitTime));
		//Conditions.elementToBeClickable(element));
	}

	// Method for switch window
	public static void switchToNewWindow(int windowNumber) {
		Set<String> s = driver.getWindowHandles();
		Iterator<String> ite = s.iterator();
		int i = 1;
		while (ite.hasNext() && i < 10) { // replace i < 10 value with s.size()
			String popupHandle = ite.next().toString();
			driver.switchTo().window(popupHandle);
			if (i == windowNumber)
				break;
			i++;
		}
	}

	// Method for check the button and click on it
	public static void checkIfButtonExistsAndClick(WebElement element) throws Throwable {
		if (element.isDisplayed()) {
			GenericMethods.sychronizationinterval();
			element.click();
		}
	}

	// Method for verify element is present in application
	public static boolean checkIfElementExists(WebElement element) {
		if (element.isDisplayed()) {
			return true;
		}
		return false;
	}

	// Method for Synchronization
	public static void sychronizationinterval() throws Throwable {
		Thread.sleep(ConstantUtils.sychronizationTime);
	}

	
	// method for switch window
	public static void SwitchToAnotherWindow(int window_number) {

		List<String> windowlist = null;

		Set<String> windows = driver.getWindowHandles();

		windowlist = new ArrayList<String>(windows);

		String currentWindow = driver.getWindowHandle();

		for (int i = 0; i < windowlist.size(); i++) {
			System.out.println(i + " is " + windowlist.get(i));
			if (window_number == i) {

				driver.switchTo().window(windowlist.get(i));
				break;
			}
		}
	}

	// method for wait page load
	public static void waitForPageLoad() {
		driver.manage().timeouts().pageLoadTimeout(ConstantUtils.pageLoadTime, TimeUnit.SECONDS);
	}
	
	// method for maximize browser window
	public static void maximizeWindow() {
		
		driver.manage().window().maximize();
	}
	
	// method for close the browser window
		public static void closeWindow() {
			
			driver.close();
		}

}
