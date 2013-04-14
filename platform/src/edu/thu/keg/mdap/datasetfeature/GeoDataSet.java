/**
 * 
 */
package edu.thu.keg.mdap.datasetfeature;

import edu.thu.keg.mdap.datamodel.DataField;




/**
 * A dataset implementing this interface
 * is a geographical dataset, and 
 * can be drawn on a map. Records are geographical
 * points, regions, etc.
 * @author Yuanchao Ma
 */
public class GeoDataSet extends DataSetFeature {
	/**
	 * @return Reference of the latitude field 
	 * in the database
	 */
	public DataField getLatitudeField() {
		return this.latitudeField;
	}
	/**
	 * @return Reference of the longitude field 
	 * in the database
	 */
	public DataField getLongitudeField() {
		return this.longitudeField;
	}
	/**
	 * 
	 * @return Reference of the tag field, which contains description of the point.
	 */
	public DataField getTagField() {
		return this.tagField;
	}
	/**
	 * Whether points of the dataset is ordered
	 * @return True if ordered. False otherwise
	 */
	public boolean isOrdered() {
		return this.ordered;
	}
	@Override
	public Class<? extends DataSetFeature> getFeatureType() {
		return GeoDataSet.class;
	}
	

	private DataField latitudeField;
	private DataField longitudeField;
	private DataField tagField;
	private boolean ordered;
	
	
	public GeoDataSet(DataField latitudeField, DataField longitudeField,
			DataField tagField, boolean ordered) {
		super();
		this.latitudeField = latitudeField;
		this.longitudeField = longitudeField;
		this.tagField = tagField;
		this.ordered = ordered;
	}
	
	
}
