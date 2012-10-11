package edu.thu.keg.mobiledata.trafficip;
/**
 * 
 * @author WuChao
 * 
 */
import java.util.HashMap;

public class ipValue {
	private double traffic;
	private int count;
	private HashMap<Integer,Integer> mapSer;
	private HashMap<Integer,Integer> mapApp;

	public ipValue() {
		
	}

	public ipValue(double d,int i,HashMap<Integer,Integer> mSer,HashMap<Integer,Integer> mApp) {
		this.traffic = d;
		this.count = i;
		this.mapSer = mSer;
		this.mapApp = mApp;
	}

	public void setSer(int strSer) {
		int c = 1;
		if(mapSer.containsKey(strSer)) c += mapSer.get(strSer);
		mapSer.put(strSer,c);
	}

	public void setApp(int strApp) {
		int c = 1;
		if(mapApp.containsKey(strApp)) c += mapApp.get(strApp);
		mapApp.put(strApp,c);
	}

	public void setTraffic(double t) {
		traffic = t;
	}

	public void setCount(int c) {
		count = c;
	}

	public HashMap<Integer,Integer> getMapSer() {
		return mapSer;
	}

	public HashMap<Integer,Integer> getMapApp() {
		return mapApp;
	}

	public double getTraffic() {
		return traffic;
	}

	public int getCount() {
		return count;
	}

}