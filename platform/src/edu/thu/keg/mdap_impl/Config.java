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
	private static final String FILE = "config.xml";
	private static Properties prop = null;
	
	public static void init() throws IOException {
		InputStream is = new FileInputStream(FILE);
		prop = new Properties();
		prop.loadFromXML(is);
	}
	private static Properties getProp() throws IOException {
		if (prop == null)
			init();
		return prop;
	}
	public static String getProperty(String key){
		try {
			return getProp().getProperty(key);
		} catch (IOException e) {
			return null;
		}
	}
	public static void setProperty(String key, String value) throws IOException {
		getProp().setProperty(key, value);
		OutputStream os = new FileOutputStream(FILE);
		getProp().storeToXML(os, null);
	}
	public static String getDataSetStorage() {
		return getProperty("DataSetStorage");
	}
}
