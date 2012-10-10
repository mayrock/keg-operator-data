package edu.thu.keg.mobiledata.trafficip;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.util.HashMap;

public class DataReader {

	/**
	 * @param args
	 */
	public static HashMap<String,Integer> map=new HashMap<String,Integer>();
	private static String sep = "";
	private static String sett = "";
	private static String[] typeArr;
	private static String[] nameArr;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//
		String addr = "D://GB_Traffic_IP";
		File[] files = new File(addr).listFiles(new FileFilter() {
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});
		for(File file : files) {
			File[] subFiles = file.listFiles(new FileFilter() {
				public boolean accept(File arg0) {
					return arg0.isDirectory();
				}
			});
			for (File subFile : subFiles) {
				File[] subF = subFile.listFiles(new FilenameFilter() {
					public boolean accept(File arg0, String arg1) {
						return !arg1.contains("finished");
					}
				});
				for (File f : subF) {
					singleFileReader(f);
				}
			}
		}
		//
	}

	public static void singleFileReader(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String strLine;
			while((strLine = reader.readLine()) != null)   {
				if(strLine.startsWith("Column")) {
					String[] arr = strLine.split("[: ]");
					sett = arr[2];
					sep = arr[3];
					break;
				}
			}
			strLine = reader.readLine();
			typeArr = strLine.split(sep);
			strLine = reader.readLine();
			nameArr = strLine.split(sep);
			for (int i = 0; i < 27; ++i) {
				System.out.println(nameArr[i] + " " + typeArr[i] + "," + System.lineSeparator());
			}
			reader.close();
		}catch(Exception e) {
			System.out.println("Error reading file:" + file.getAbsolutePath());
		}
	}
}
