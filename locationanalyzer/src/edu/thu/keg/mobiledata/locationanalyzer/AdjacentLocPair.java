/**
 * 
 */
package edu.thu.keg.mobiledata.locationanalyzer;

import java.util.HashMap;

/**
 * @author myc
 *
 */
public class AdjacentLocPair {
	private CellLocation cell1;
	private CellLocation cell2;
	
	private int totalCount;
	private int usersCount;
	private int[] usersPerHourCount;
	private int[] totalPerHourCount;
	
	
	
	public AdjacentLocPair(CellLocation cell1, CellLocation cell2) {
		super();
		this.cell1 = cell1;
		this.cell2 = cell2;
		totalCount = 0;
		usersCount = 0;
		usersPerHourCount = new int[24];
		totalPerHourCount = new int[24];
	}
	public CellLocation getCell1() {
		return cell1;
	}
	public void setCell1(CellLocation cell1) {
		this.cell1 = cell1;
	}
	public CellLocation getCell2() {
		return cell2;
	}
	public void setCell2(CellLocation cell2) {
		this.cell2 = cell2;
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
