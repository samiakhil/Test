package com.Saviynt.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtil {
	/* accessing config data From EnvironmentPropertiy file   */
	public static String getValueFromKey(String key) {
		Properties configProperties = new Properties();
		try {
			configProperties.load(new FileInputStream(new File(ConstantUtils.propertyFilePath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return configProperties.getProperty(key);
	}	
}
