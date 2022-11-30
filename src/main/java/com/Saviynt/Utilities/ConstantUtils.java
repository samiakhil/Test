package com.Saviynt.Utilities;

public class ConstantUtils {
	public static String projectPath = System.getProperty("user.dir");

	/* Paths Section */
	public static final String screenShotPath = projectPath +"/ScreenShots/";

	public static final String propertyFilePath = "./Object_Repository/EnvironmentProperties.properties";

	public static final String extentReportsPath = "./ExtentReports/ ";
	
	public static final String ieServerPath = projectPath + "/BrowserServers/IEDriverServer.exe";
	
	
	

	/* Test Inputs */
	public static final String waitTime = "90";

	public static final int sychronizationTime = 2000;
	
	public static final int pageLoadTime =15;
}
