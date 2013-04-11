/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

/**
 * @author myc
 *
 */
public class GeneralGeoDataSet extends GeoDataSet {

	private DataField latitudeField;
	private DataField longitudeField;
	private boolean ordered;
	
	
	public GeneralGeoDataSet(DataField latitudeField, DataField longitudeField,
			boolean ordered) {
		super();
		this.latitudeField = latitudeField;
		this.longitudeField = longitudeField;
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
	 * @see edu.thu.keg.mdap.datamodel.GeographicalDataSet#getLongtitudeField()
	 */
	@Override
	public DataField getLongtitudeField() {
		return this.longitudeField;
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.datamodel.GeographicalDataSet#isOrdered()
	 */
	@Override
	public boolean isOrdered() {
		return this.ordered;
	}

}
