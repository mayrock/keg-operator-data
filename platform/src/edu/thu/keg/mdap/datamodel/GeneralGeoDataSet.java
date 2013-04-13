/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

/**
 * General implementation of the GeoDataSet interface
 * @author myc
 *
 */
public class GeneralGeoDataSet extends GeoDataSet {

	private DataField latitudeField;
	private DataField longitudeField;
	private DataField tagField;
	private boolean ordered;
	
	
	public GeneralGeoDataSet(DataField latitudeField, DataField longitudeField,
			DataField tagField, boolean ordered) {
		super();
		this.latitudeField = latitudeField;
		this.longitudeField = longitudeField;
		this.tagField = tagField;
		this.ordered = ordered;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.GeographicalDataSet#getLatitudeField()
	 */
	@Override
	public DataField getLatitudeField() {
		return this.latitudeField;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.GeographicalDataSet#getLongitudeField()
	 */
	@Override
	public DataField getLongitudeField() {
		return this.longitudeField;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.GeographicalDataSet#isOrdered()
	 */
	@Override
	public boolean isOrdered() {
		return this.ordered;
	}

	@Override
	public DataField getTagField() {
		return this.tagField;
	}

}
