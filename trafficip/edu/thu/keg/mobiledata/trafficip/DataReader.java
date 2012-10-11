package edu.thu.keg.mobiledata.trafficip;
import java.io.*;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.Scanner;

public class DataReader {

	/**
	 * @param args
	 */
	public static HashMap<String,ipValue> map = new HashMap<String,ipValue>();
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
//			ipValue mValue = map.get(key);
//			System.out.println(key+" : "+mValue.getTraffic()+" "+mValue.getCount());
//			Iterator iterSer = mValue.getMapSer().keySet().iterator();
//			while(iterSer.hasNext()) {
//				Object keySer = iterSer.next();
//				System.out.print(keySer+" "+mValue.getMapSer().get(keySer)+" ");
//			}
//			System.out.println();
//			Iterator iterApp = mValue.getMapApp().keySet().iterator();
//			while(iterApp.hasNext()) {
//				Object keyApp = iterApp.next();
//				System.out.print(keyApp+" "+mValue.getMapApp().get(keyApp)+" ");
//			}
//			System.out.println();
//		}
	}

	public static void readFile(String addr) {
		//读取一个目录下的所有文件
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
		//读取一个文件
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
			//得到文件使用的间隔符
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
		//读取一行信息并做处理
		String[] arr = line.split(interval);
		double traffic;
		int count = 1;
		ipValue mValue = new ipValue();
		StringBuffer mainKey = new StringBuffer();
		StringBuffer shortTime = new StringBuffer();
		if(matchValue(arr[1])&&matchValue(arr[3])&&matchValue(arr[4])&&matchValue(arr[6])) {
//			if(matchClock(arr[3])) {
				shortTime.append(arr[3]);
				shortTime.delete(16,23);
				//时间以分钟归类
				String SerType = arr[7];
				String AppType = arr[12];
				Scanner in = stringToScanner(arr[21]);
				traffic = in.nextDouble();
				in.close();
				in = stringToScanner(arr[22]);
				traffic += in.nextDouble();
				//计算流量
				mainKey.append(arr[1]+" "+shortTime.toString()+" "+arr[4]+" "+arr[6]);
				String mKey = mainKey.toString();
				//生成Key
				if(map.containsKey(mKey)) {
					mValue = map.get(mKey);
					traffic += mValue.getTraffic();
					count += mValue.getCount();
					if(!"0".equals(SerType)) mValue.setSer(SerType);
					if(!"0".equals(AppType)) mValue.setApp(AppType);
					mValue.setTraffic(traffic);
					mValue.setCount(count);
				}
				else {
					HashMap<String,Integer> mSer = new HashMap<String,Integer>();
					HashMap<String,Integer> mApp = new HashMap<String,Integer>();
					mValue = new ipValue(traffic,count,mSer,mApp);
					if(!"0".equals(SerType)) mValue.setSer(SerType);
					if(!"0".equals(AppType)) mValue.setApp(AppType);
				}
//				System.out.println(mKey+" : "+mValue.getTraffic()+" "+mValue.getCount());
				//通过Key更新Value
				map.put(mKey,mValue);
//			}
		}
//arr[1]:Imsi<<arr[3]:Period<<arr[4]:LAC<<arr[6]:Ci<<arr[7]:ServiceType<<arr[12]:AppType<<arr[21]:IPULTraffic<<arr[22]:IPDLTraffic
	}

	public static boolean matchValue(String str) {
		//判断字符串是否有意义
		if("".equals(str) || "[EMPTY]".equals(str)) return false;
		else return true;
	}

	public static Scanner stringToScanner(String str) {
		//把字符串转化为输入流以便作为其他格式读入
		byte[] bytes = str.getBytes();
		Scanner reader = new Scanner(new InputStreamReader(new ByteArrayInputStream(bytes)));
		return reader;
	}

	public static boolean matchClock(String str) {
		//选择需要的时间段
		str = str.substring(11,13);
		if("07".equals(str) ||"08".equals(str) || "09".equals(str) || "10".equals(str) || "17".equals(str) || "18".equals(str) || "19".equals(str) || "20".equals(str)) return true;
		else return false;
	}

}