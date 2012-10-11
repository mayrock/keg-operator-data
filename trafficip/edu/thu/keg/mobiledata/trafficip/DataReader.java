package edu.thu.keg.mobiledata.trafficip;
import java.io.*;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.Scanner;

public class DataReader {

	/**
	 * @param args
	 */
	public static HashMap<String,String> map=new HashMap<String,String>();
	private static String interval = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] addr = new String[2];
		addr[0] = "D://GB_Traffic_IP";
		addr[1] = "E://GB2";
		readFile(addr[0]);
		readFile(addr[1]);
//		Iterator iterator = map.keySet().iterator();
//		while(iterator.hasNext()) {
//			Object key = iterator.next();
//			System.out.println(key+" : "+map.get(key));
//		}
	}

	public static void readFile(String addr) {
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
			for(File subFile : subFiles) {
				File[] subF = subFile.listFiles(new FilenameFilter() {
					public boolean accept(File arg0,String arg1) {
						return !arg1.contains("finished");
					}
				});
				for(File f : subF) {
					singleFileReader(f);
				}
			}
		}
	}

	public static void singleFileReader(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String strLine;
			while((strLine = reader.readLine()) != null)   {
				if(strLine.startsWith("Column")) {
					String[] arr = strLine.split("[: ]");
					interval = arr[3];
					break;
				}
			}
			strLine = reader.readLine();
			strLine = reader.readLine();
			while((strLine = reader.readLine()) != null) {
				try {
					dealSingleLine(strLine);
				}catch (Exception ex){
					continue;
				}
			}
			reader.close();
		}catch(Exception e) {
			System.out.println("Error reading file:" + file.getAbsolutePath());
		}
	}

	public static void dealSingleLine(String line) {
		String[] arr = line.split(interval);
		double Traffic;
		int count = 1;
		StringBuffer mainKey = new StringBuffer();
		StringBuffer mainValue = new StringBuffer();
		StringBuffer shortTime = new StringBuffer();
		if(hasValue(arr[1])&&hasValue(arr[3])&&hasValue(arr[4])&&hasValue(arr[6])) {
			Scanner in = stringToScanner(arr[21]);
			Traffic = in.nextDouble();
			in.close();
			in = stringToScanner(arr[22]);
			Traffic += in.nextDouble();
			shortTime.append(arr[3]);
			shortTime.delete(16,23);
			mainKey.append(arr[1]+" "+shortTime.toString()+" "+arr[4]+" "+arr[6]);
			String mKey = mainKey.toString();
			if(map.containsKey(mKey)) {
				in = stringToScanner(map.get(mKey));
				Traffic += in.nextDouble();
				count += in.nextInt();
			}
			mainValue.append(Traffic+" "+count);
			String mValue = mainValue.toString();
//			System.out.println(mKey+" : "+mValue);
			map.put(mKey,mValue);
		}
//arr[1]:Imsi<<arr[3]:Period<<arr[4]:LAC<<arr[6]:Ci<<arr[7]:ServiceType<<arr[12]:AppType<<arr[21]:IPULTraffic<<arr[22]:IPDLTraffic
	}

	public static boolean hasValue(String str) {
		if("".equals(str) || "[EMPTY]".equals(str)) return false;
		else return true;
	}

	public static Scanner stringToScanner(String str) {
		byte[] bytes = str.getBytes();
		Scanner reader = new Scanner(new InputStreamReader(new ByteArrayInputStream(bytes)));
		return reader;
	}

}