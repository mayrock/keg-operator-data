package edu.thu.keg.mobiledata.locationanalyzer;

import java.util.ArrayList;

public class Site {
	private static final int INITIAL_NEIGHBER_COUNT = 20;
	private String siteId;
	private int longitude;
	private int latitude;

	public int getLongitude() {
		return longitude;
	}

	public int getLatitude() {
		return latitude;
	}

	public String getSiteId() {
		return siteId;
	}
	
	public Site(String name, int longitude, int latitude) {
		this.siteId = name;
		this.longitude = longitude;
		this.latitude = latitude;
		nextSites = new ArrayList<AdjacentLocPair>(INITIAL_NEIGHBER_COUNT);
	}
	
	private ArrayList<AdjacentLocPair> nextSites;

	public ArrayList<AdjacentLocPair> getNextSites() {
		return nextSites;
	}
	
	private ArrayList<AdjacentLocPair> previousSites;

	public ArrayList<AdjacentLocPair> getPreviousSites() {
		return previousSites;
	}
	
	public void addNextSite(AdjacentLocPair pair) {
		nextSites.add(pair);
	}
	public void addPreviousSite(AdjacentLocPair pair) {
		previousSites.add(pair);
	}
	public boolean isSameLoc(Site loc) {
		return loc.getLatitude() == this.latitude 
				&& loc.getLongitude() == this.longitude;
	}
	
}
