package edu.thu.keg.mobiledata.trafficip;
/**
 * 
 * @author WuChao
 * 
 */
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class DataReader {

	/**
	 * @param args
	 */
	public static HashMap<String,ipValue> map = new HashMap<String,ipValue>();
	private static String interval = "";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String[] addr = new String[3];
		addr[0] = "D://GB_Traffic_IP-Test";
//		addr[1] = "E://GB2";
		addr[2] = "D://GB_Traffic_IP-Test//test";
		readFile(addr[0]);
//		readFile(addr[1]);
		outPut(addr[2]);
	}

	private static void readFile(String addr) {
		//��ȡһ��Ŀ¼�µ������ļ�
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

	private static void singleFileReader(File file) {
		//��ȡһ���ļ�
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
			//�õ��ļ�ʹ�õļ����
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

	private static void dealSingleLine(String line) {
		//��ȡһ����Ϣ��������
		String[] arr = line.split(interval);
		int count = 1;
		ipValue mValue = new ipValue();
		StringBuffer mainKey = new StringBuffer();
		StringBuffer shortTime = new StringBuffer();
		if(matchValue(arr[1])&&matchValue(arr[3])&&matchValue(arr[4])&&matchValue(arr[6])) {
//			if(matchClock(arr[3])) {
				shortTime.append(arr[3]);
				shortTime.delete(16,23);
				//ʱ���Է��ӹ���
				int SerType = Integer.parseInt(arr[7]);
				int AppType = Integer.parseInt(arr[12]);
				double traffic = Double.parseDouble(arr[21]);
				traffic += Double.parseDouble(arr[22]);
				//��������
				mainKey.append(arr[1]+" "+shortTime.toString()+" "+arr[4]+" "+arr[6]);
				String mKey = mainKey.toString();
				//����Key
				if(map.containsKey(mKey)) {
					mValue = map.get(mKey);
					traffic += mValue.getTraffic();
					count += mValue.getCount();
					if(SerType!=0) mValue.setSer(SerType);
					if(AppType!=0) mValue.setApp(AppType);
					mValue.setTraffic(traffic);
					mValue.setCount(count);
				}
				else {
					HashMap<Integer,Integer> mSer = new HashMap<Integer,Integer>();
					HashMap<Integer,Integer> mApp = new HashMap<Integer,Integer>();
					mValue = new ipValue(traffic,count,mSer,mApp);
					if(SerType!=0) mValue.setSer(SerType);
					if(AppType!=0) mValue.setApp(AppType);
				}
				//ͨ��Key����Value
				map.put(mKey,mValue);
//			}
		}
//arr[1]:Imsi<<arr[3]:Period<<arr[4]:LAC<<arr[6]:Ci<<arr[7]:ServiceType<<arr[12]:AppType<<arr[21]:IPULTraffic<<arr[22]:IPDLTraffic
	}

	private static boolean matchValue(String str) {
		//�ж��ַ����Ƿ�������
		if("".equals(str) || "[EMPTY]".equals(str)) return false;
		else return true;
	}

	private static boolean matchClock(String str) {
		//ѡ����Ҫ��ʱ���
		str = str.substring(11,13);
		if("07".equals(str) ||"08".equals(str) || "09".equals(str) || "10".equals(str) || "17".equals(str) || "18".equals(str) || "19".equals(str) || "20".equals(str)) return true;
		else return false;
	}

	private static void outPut(String addr) {
		//�ѹ�ϣ���е����������ָ���ļ��в��������������SerType��AppType
		try {
			PrintWriter out = new PrintWriter(addr);
			out.println("Imsi Period(Year-Month-Day Hour:Minute) LAC Ci\tTraffic\tCount\tServiceType\tSerTypeCount\tAppType\tAppTypeCount");
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				String key = iterator.next();
				ipValue mValue = map.get(key);
				out.print(key+"\t"+mValue.getTraffic()+"\t"+mValue.getCount()+"\t");
				HashMap<Integer,Integer> mapSer = mValue.getMapSer();
				Iterator<Integer> iterSer = mapSer.keySet().iterator();
				int maxKey = 0;
				int maxCount = 0;
				while(iterSer.hasNext()) {
					Integer keySer = iterSer.next();
					Integer serCount = mapSer.get(keySer);
					if(serCount > maxCount) {
						maxKey = keySer;
						maxCount = serCount;
					}
				}
				out.print(maxKey+"\t"+maxCount);
				maxKey = 0;
				maxCount = 0;
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
			System.out.println("Error writing file:" + addr);
		}
	}

}