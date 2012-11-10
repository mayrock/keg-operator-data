package edu.thu.keg.mobiledata.locationanalyzer;

import java.util.HashMap;

public class AdjacentLocList {
	public static final int INITIAL_CELL_COUNT = 6000;
	private HashMap<String, CellLocation> cells;

	public HashMap<String, CellLocation> getCells() {
		return cells;
	}
	
	public AdjacentLocList() {
		cells = new HashMap<String, CellLocation>(INITIAL_CELL_COUNT);
	}
	public CellLocation addCellLocation(String cellName) {
		CellLocation newLoc = new CellLocation(cellName);
		cells.put(cellName, newLoc);
		return newLoc;
	}
	public AdjacentLocPair getAdjacentLocPair(String cellName1, String cellName2) {
		CellLocation cell1, cell2;
		if (!cells.containsKey(cellName1)) {
			cell1 = addCellLocation(cellName1);
		} else {
			cell1 = cells.get(cellName1);
		}
		if (!cells.containsKey(cellName2)) {
			cell2 = addCellLocation(cellName2);
		} else {
			cell2 = cells.get(cellName2);
		}
		for (AdjacentLocPair pair : cell1.getNextCells()) {
			if (pair.getCell2().getCellName().equals(cellName2)) {
				return pair;
			}
		}
		AdjacentLocPair newPair = new AdjacentLocPair(cell1, cell2);
		cell1.addNextCell(newPair);
		cell2.addNextCell(newPair);
		return newPair;
	}
}
