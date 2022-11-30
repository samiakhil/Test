package com.Saviynt.Utilities;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Logs {
	 
	// Initialize Log4j logs
	
	public static Logger Log;
	 public static void initLogs(String className){
		 
		 Log=Logger.getLogger(className);
		 PropertyConfigurator.configure("log4j.properties");
		 
	 }
	
	 //= Logger.getLogger(Logs.class.getName());//
	 
	 // This is to print log for the beginning of the test case, as we usually run so many test cases as a test suite
	 
	 public static void startTestCase(String sTestCaseName){
	 
	 Log.info("**************************************************************");
	
	 Log.info("$$$$$$$$     "+sTestCaseName+ " Execution Started     $$$$$$$$");
	 
	 Log.info("**************************************************************");
	  }
	 
	 //This is to print log for the ending of the test case
	 
	 public static  void endTestCase(String testcase){
	 
	 Log.info("$$$$$$$$$$$      "+ testcase +" Execution Completed "+"      $$$$$$$$$$$$$");
	 
	 Log.info("===============================================================");
	 }
	 
	 // Need to create these methods, so that they can be called  
	 
	 public static void info(String message) {
	 
	 Log.info(message);
	 
	 }
	 
	 public static void warn(String message) {
	 
	    Log.warn(message);
	 
	 }
	 
	 public static void error(String message) {
	 
	    Log.error(message);
	 
	 }
	 
	 public static void fatal(String message) {
	 
	    Log.fatal(message);
	 
	 }
	 
	 public static  void debug(String message) {
	 
	    Log.debug(message);
	 
	 }
	 
	}
