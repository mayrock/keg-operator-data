package group;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class Group {

	/**
	 * @param args
	 */
	private HashMap<Integer,String> map_all = new HashMap<Integer,String>();
	private HashMap<Integer,String> map = new HashMap<Integer,String>();
	private String[] msg = {"At home","In bussiness center","On the way","At work"};
	private final double EARTH_RADIUS = 6378.137;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Group ob = new Group();
		ob.initInfo();
		System.out.print("Input a SiteId:");
		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		String info = ob.analyseSiteId(n);
		System.out.println("The SiteId info: " + info + "!");
		in.close();
	}

	private void initInfo() {
		try {
			Scanner in = new Scanner(
					new File("D:\\result\\location_important\\AreaCount_500_allKind_new.txt"));
			String str = in.nextLine();
			String[] temp;
			while(in.hasNextLine()) {
				str = in.nextLine();
				temp = str.split("\t");
				map_all.put(Integer.parseInt(temp[0]),temp[1] + " " + temp[2]);
			}
			in.close();
			for(int i = 1; i <= 4; i++) {
				in = new Scanner(new File(
						"D:\\result\\location_important\\Result" + i + ".txt"));
				in.nextLine();
				in.nextLine();
				while(in.hasNextLine()) {
					str = in.nextLine();
					temp = str.split("\t");
					map.put(Integer.parseInt(temp[0]),msg[i - 1]);
				}
				in.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String analyseSiteId(int n) {
		String info;
		if(map.containsKey(n)) info = map.get(n);
		else {
			if(map_all.containsKey(n)) {
				String str = map_all.get(n);
				String[] temp = str.split(" ");
				double lat = Double.parseDouble(temp[0]);
				double lng = Double.parseDouble(temp[1]);
				int k = getNearSiteId(lat,lng);
				System.out.println("*" + k + "*");
				if(k != -1) info = map.get(k);
				else info = "No infomation";
			}
			else info = "This siteId don't exist";
		}
		return info;
	}

	private int getNearSiteId(double lat,double lng) {
		Iterator<Integer> iterator = map.keySet().iterator();
		String str;
		String[] temp;
		double lat1;
		double lng1;
		double d;
		int[] id = new int[1000];
		int i = 0;
		int k;
		int n = -2;
		while(iterator.hasNext()) {
			k = iterator.next();
			str = map_all.get(k);
			temp = str.split(" ");
			lat1 = Double.parseDouble(temp[0]);
			lng1 = Double.parseDouble(temp[1]);
			d = getDistance(lat,lng,lat1,lng1);
			System.out.println(d);
			if(d > 0.5) continue;
			else id[i++] = k;
		}
		int l = i;
		if(l == 0) n = -1;
		else {
			double min = 9.9e99;
			for(i = 0;i < l;i++) {
				k = id[i];
				str = map_all.get(k);
				temp = str.split(" ");
				lat1 = Double.parseDouble(temp[0]);
				lng1 = Double.parseDouble(temp[1]);
				d = getDistance(lat,lng,lat1,lng1);
				if(d < min) {
					min = d;
					n = k;
				}
			}
		}
		return n;
	}

	private double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}

	public double getDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) + 
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}

}
