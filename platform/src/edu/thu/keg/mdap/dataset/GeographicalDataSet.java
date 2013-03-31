/**
 * 
 */
package edu.thu.keg.mdap.dataset;

import edu.thu.keg.mdap.datafield.DataField;


/**
 * A dataset implementing this interface
 * contains latitude and longitude field, and 
 * can be drawn on a map. Records are geographical
 * points, regions, etc.
 * @author Yuanchao Ma
 */
public interface GeographicalDataSet extends DataSet {
	/**
	 * @return Reference of the latitude field 
	 * in the database
	 */
	public abstract DataField getLatitudeField();
	/**
	 * @return Reference of the longitude field 
	 * in the database
	 */
	public abstract DataField getLongtitudeField();
	/**
	 * Whether points of the dataset is ordered
	 * @return True if ordered. False otherwise
	 */
	public abstract boolean isOrdered();
}