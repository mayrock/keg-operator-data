/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Store platform-wide configurations
 * @author Yuanchao Ma
 *
 */
public final class Config {
	private static String file = "config.xml";
	public static final String DataSetFile = "DataSetFile";
	private static Properties prop = null;
	
	public static void init(String file) throws IOException {
		Config.file = file;
		InputStream is = new FileInputStream(file);
		prop = new Properties();
		prop.loadFromXML(is);
	}
	public static String getProperty(String key){
		return prop.getProperty(key);
	}
	public static void setProperty(String key, String value) throws IOException {
		prop.setProperty(key, value);
		OutputStream os = new FileOutputStream(file);
		prop.storeToXML(os, null);
	}
	public static String getDataSetFile() {
		return getProperty("DataSetFile");
	}
}
