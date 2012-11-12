package edu.thu.keg.mobiledata.locationanalyzer;

import java.util.HashMap;

public class AdjacentLocList {
	public static final int INITIAL_CELL_COUNT = 6000;
	private HashMap<String, Site> sites;

	public HashMap<String, Site> getSites() {
		return sites;
	}
	
	public AdjacentLocList() {
		sites = new HashMap<String, Site>(INITIAL_CELL_COUNT);
	}
	/**
	 * Add a new site location to the node list. If the site already exists,
	 * then simply return the instance
	 * @param siteId
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	public Site addSite(String siteId, int longitude, int latitude) {
		if (sites.containsKey(siteId)) {
			return sites.get(siteId);
		}
		Site newLoc = new Site(siteId, longitude, latitude);
		sites.put(siteId, newLoc);
		return newLoc;
	}
	/**
	 * Return the instance of this siteId. null if the siteId does not exist
	 * @param siteId
	 * @return
	 */
	public Site getSite(String siteId) {
		return sites.get(siteId);
	}
	public AdjacentLocPair getAdjacentLocPair(Site site1, Site site2) {
		for (AdjacentLocPair pair : site1.getNextSites()) {
			if (pair.getSite2().equals(site2)) {
				return pair;
			}
		}
		AdjacentLocPair newPair = new AdjacentLocPair(site1, site2);
		site1.addNextSite(newPair);
		site2.addNextSite(newPair);
		return newPair;
	}
}
