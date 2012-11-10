/**
 * 
 */
package edu.thu.keg.mobiledata.internetgraph.dbo;

/**
 * @author myc
 * Stores statistics of a URI
 *
 */
public class UriInfo {

	private String URI;
	private int totalCount;
	private int userCount;
	private int locationCount;
	public String getURI() {
		return URI;
	}
	public void setURI(String uRI) {
		URI = uRI;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int getLocationCount() {
		return locationCount;
	}
	public void setLocationCount(int locationCount) {
		this.locationCount = locationCount;
	}
	public UriInfo(String uRI, int totalCount, int userCount, int locationCount) {
		super();
		URI = uRI;
		this.totalCount = totalCount;
		this.userCount = userCount;
		this.locationCount = locationCount;
	}
	
	public void addRecord(int nTotal, int nUser, int nLocation) {
		this.totalCount += nTotal;
		if (nUser > this.userCount)
			this.userCount = nUser;
		if (nLocation > this.locationCount)
			this.locationCount = nLocation;
	}

}
