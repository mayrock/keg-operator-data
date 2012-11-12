/**
 * 
 */
package edu.thu.keg.mobiledata.locationanalyzer;


/**
 * @author myc
 *
 */
public class AdjacentLocPair {
	private Site site1;
	private Site site2;
	
	private int totalCount;
	private int usersCount;
	private int[] usersPerHourCount;
	private int[] totalPerHourCount;
	
	
	
	public AdjacentLocPair(Site site1, Site site2) {
		super();
		this.site1 = site1;
		this.site2 = site2;
		totalCount = 0;
		usersCount = 0;
		usersPerHourCount = new int[24];
		totalPerHourCount = new int[24];
	}
	public Site getSite1() {
		return site1;
	}
	public void setSite1(Site site1) {
		this.site1 = site1;
	}
	public Site getSite2() {
		return site2;
	}
	public void setSite2(Site site2) {
		this.site2 = site2;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public int getUsersCount() {
		return usersCount;
	}
	public int[] getUsersPerHourCount() {
		return usersPerHourCount;
	}
	public int[] getTotalPerHourCount() {
		return totalPerHourCount;
	}
	public void addUser(int[] hourArr) {
		for (int i = 0; i < 24; ++i) {
			totalPerHourCount[i] += hourArr[i];
			totalCount += hourArr[i];
			if (hourArr[i] > 0)
				usersPerHourCount[i]++;
		}
		usersCount++;
	}
	//public void set


}
