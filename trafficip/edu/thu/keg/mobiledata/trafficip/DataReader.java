package edu.thu.keg.mobiledata.trafficip;
/**
 * 
 * @author WuChao
 * 
 */
import java.io.*;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;

public class DataReader {

	/**
	 * @param args
	 */
	private static String interval = "";
	private static int count = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] addr = new String[3];
		addr[0] = "D://GB_Traffic_IP";
		addr[1] = "E://GB2";
		readFile(addr[0]);
		readFile(addr[1]);
	}

	private static void readFile(String addr) {
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
				String outPutAddr = "D://GB_Traffic_IP-File//" + file.getName() + "_" + subFile.getName() + ".txt";
//				System.out.println(outPutAddr);
				HashMap<String,ipValue> map = new HashMap<String,ipValue>();
				for(File f : subF) {
					singleFileReader(f,map);
				}
				outPut(map,outPutAddr);
			}
		}
	}

	private static void singleFileReader(File file,HashMap<String,ipValue> map) {
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
					dealSingleLine(strLine,map);
				}catch (Exception ex){
					continue;
				}
			}
			reader.close();
			System.out.println("Reading file " + ++count + " : " + file.getAbsolutePath());
		}catch(Exception e) {
			System.out.println("Error reading file:" + file.getAbsolutePath());
		}
	}

	private static void dealSingleLine(String line,HashMap<String,ipValue> map) {
		//读取一行信息并做处理
		String[] arr = line.split(interval);
		int count = 1;
		ipValue mValue = new ipValue();
		StringBuffer mainKey = new StringBuffer();
		StringBuffer shortTime = new StringBuffer();
		if(matchValue(arr[1])&&matchValue(arr[3])&&matchValue(arr[4])&&matchValue(arr[6])) {
			shortTime.append(arr[3]);
			shortTime.delete(16,23);
			//时间以分钟归类
			int AppType = Integer.parseInt(arr[12]);
			double traffic = Double.parseDouble(arr[21]);
			traffic += Double.parseDouble(arr[22]);
			//计算流量
			mainKey.append(arr[1]+"\t"+shortTime.toString()+"\t"+arr[4]+"\t"+arr[6]);
			String mKey = mainKey.toString();
			//生成Key
			if(map.containsKey(mKey)) {
				mValue = map.get(mKey);
				traffic += mValue.getTraffic();
				count += mValue.getCount();
				if(AppType != 0) mValue.setApp(AppType);
				mValue.setTraffic(traffic);
				mValue.setCount(count);
			}
			else {
				HashMap<Integer,Integer> mApp = new HashMap<Integer,Integer>();
				mValue = new ipValue(traffic,count,mApp);
				if(AppType != 0) mValue.setApp(AppType);
			}
			//通过Key更新Value
			map.put(mKey,mValue);
		}
//arr[1]:Imsi<<arr[3]:Period<<arr[4]:LAC<<arr[6]:Ci<<arr[12]:AppType<<arr[21]:IPULTraffic<<arr[22]:IPDLTraffic
	}

	private static boolean matchValue(String str) {
		//判断字符串是否有意义
		if("".equals(str) || "[EMPTY]".equals(str) || "0".equals(str)) return false;
		else return true;
	}

	private static void outPut(HashMap<String,ipValue> map,String outPutAddr) {
		//把哈希表中的内容输出到指定文件中并整理出最大计数的SerType和AppType
		try {
			FileWriter outPut = new FileWriter(outPutAddr,true);
			BufferedWriter out = new BufferedWriter(outPut);
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				ipValue mValue = map.get(key);
				double traffic = mValue.getTraffic();
				NumberFormat format = NumberFormat.getNumberInstance();
				format.setMaximumFractionDigits(3);
				String result = format.format(traffic);
//				traffic = Double.parseDouble(result);
				out.write(key+"\t"+result+"\t"+mValue.getCount()+"\t");
				int maxKey = 0;
				int maxCount = 0;
				HashMap<Integer,Integer> mapApp = mValue.getMapApp();
				Iterator<Integer> iterApp = mapApp.keySet().iterator();
				while(iterApp.hasNext()) {
					Integer keyApp = iterApp.next();
					Integer appCount = mapApp.get(keyApp);
					if(appCount > maxCount) {
						maxKey = keyApp;
						maxCount = appCount;
					}
				}
				out.write(maxKey+"\t"+maxCount+"\r\n");
			}
			out.close();
			outPut.close();
		}catch(Exception e) {
			System.out.println("Error writing file!");
		}
	}

}