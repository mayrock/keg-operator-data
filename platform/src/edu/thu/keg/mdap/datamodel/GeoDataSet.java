/**
 * 
 */
package edu.thu.keg.mdap.datamodel;



/**
 * A dataset implementing this interface
 * contains latitude and longitude field, and 
 * can be drawn on a map. Records are geographical
 * points, regions, etc.
 * @author Yuanchao Ma
 */
public abstract class GeoDataSet implements DataSetFeature {
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
	@Override
	public Class<? extends DataSetFeature> getFeatureType() {
		return GeoDataSet.class;
	}
	
	
}
