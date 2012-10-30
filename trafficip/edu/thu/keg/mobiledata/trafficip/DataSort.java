package edu.thu.keg.mobiledata.trafficip;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
/**
 * 
 * @author WuChao
 * Unused
 * 
 */
public class DataSort {

	/**
	 * @param args
	 */
	private static int count = 0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String addr = "D://GB_Traffic_IP-File2";
		readFile(addr);
	}

	private static void readFile(String addr) {
		//读取目录下的所有文件
		File[] files = new File(addr).listFiles(new FileFilter() {
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});
//		for(File file : files) {
		for(int i = 4;i < 7;i++) {
			File[] subFiles = files[i].listFiles(new FilenameFilter() {
				public boolean accept(File arg0,String arg1) {
					return !arg1.contains("finished");
				}
			});
			String outPutAddr = "D://GB_Traffic_IP-File3//" + files[i].getName() + ".txt";
//			System.out.println(outPutAddr);
			HashMap<String,ipValue> map = new HashMap<String,ipValue>();
			for(File subFile : subFiles) {
//				System.out.println(subFile.getAbsolutePath());
				singleFileReader(subFile,map);
			}
			outPut(map,outPutAddr);
		}
	}

	private static void singleFileReader(File file,HashMap<String,ipValue> map) {
		//读取一个文件
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String strLine;
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
		String[] arr = line.split("\t");
		ipValue mValue = new ipValue();
		StringBuffer mainKey = new StringBuffer();
		double traffic = Double.parseDouble(arr[4]);
		int count = Integer.parseInt(arr[5]);
		int appType = Integer.parseInt(arr[6]);
		int appTypeCount = Integer.parseInt(arr[7]);
		mainKey.append(arr[0]+"\t"+arr[1]+"\t"+arr[2]+"\t"+arr[3]);
		String mKey = mainKey.toString();
		//生成Key
		if(map.containsKey(mKey)) {
			mValue = map.get(mKey);
			traffic += mValue.getTraffic();
			count += mValue.getCount();
			mValue.setApp(appType,appTypeCount);
			mValue.setTraffic(traffic);
			mValue.setCount(count);
		}
		else {
			HashMap<Integer,Integer> mApp = new HashMap<Integer,Integer>();
			mValue = new ipValue(traffic,count,mApp);
			mValue.setApp(appType,appTypeCount);
		}
		//通过Key更新Value
		map.put(mKey,mValue);
	}

	private static void outPut(HashMap<String,ipValue> map,String outPutAddr) {
		//把哈希表中的内容输出到指定文件中并整理出最大计数的SerType和AppType
		try {
			PrintWriter out = new PrintWriter(outPutAddr);
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				ipValue mValue = map.get(key);
				double traffic = mValue.getTraffic();
				out.printf("%s\t%.3f\t%d\t",key,traffic,mValue.getCount());
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
				out.println(maxKey+"\t"+maxCount);
			}
			out.close();
		}catch(Exception e) {
			System.out.println("Error writing file!");
		}
	}

}
