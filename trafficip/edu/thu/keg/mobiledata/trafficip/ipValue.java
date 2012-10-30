package edu.thu.keg.mobiledata.trafficip;
import java.util.HashMap;
/**
 * 
 * @author WuChao
 * 
 */
public class ipValue {
	private double traffic;
	private int count;
	private HashMap<Integer,Integer> mapApp;

	public ipValue() {
		
	}

	public ipValue(double d,int i,HashMap<Integer,Integer> mApp) {
		this.traffic = d;
		this.count = i;
		this.mapApp = mApp;
	}

	public void setApp(int strApp) {
		int c = 1;
		if(mapApp.containsKey(strApp)) c += mapApp.get(strApp);
		mapApp.put(strApp,c);
	}

	public void setApp(int strApp,int appTypeCount) {
		// TODO Auto-generated method stub
		if(mapApp.containsKey(strApp)) appTypeCount += mapApp.get(strApp);
		mapApp.put(strApp,appTypeCount);
	}

	public void setTraffic(double t) {
		traffic = t;
	}

	public void setCount(int c) {
		count = c;
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