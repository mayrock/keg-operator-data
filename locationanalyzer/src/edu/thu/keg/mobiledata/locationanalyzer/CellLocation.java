package edu.thu.keg.mobiledata.locationanalyzer;

import java.util.ArrayList;

public class CellLocation {
	private static final int INITIAL_NEIGHBER_COUNT = 20;
	private String cellName;

	public String getCellName() {
		return cellName;
	}
	
	public CellLocation(String name) {
		this.cellName = name;
		nextCells = new ArrayList<AdjacentLocPair>(INITIAL_NEIGHBER_COUNT);
	}
	
	private ArrayList<AdjacentLocPair> nextCells;

	public ArrayList<AdjacentLocPair> getNextCells() {
		return nextCells;
	}
	
	private ArrayList<AdjacentLocPair> previousCells;

	public ArrayList<AdjacentLocPair> getPreviousCells() {
		return previousCells;
	}
	
	public void addNextCell(AdjacentLocPair pair) {
		nextCells.add(pair);
	}
	public void addPreviousCell(AdjacentLocPair pair) {
		previousCells.add(pair);
	}
	
	
}
