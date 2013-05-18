/**
 * 
 */
package edu.thu.keg.mdap.datafeature;

import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;

/**
 * @author myc
 *
 */
public enum DataFeatureType {
	GeoFeature(FieldFunctionality.Latitude, FieldFunctionality.Longitude),
	DistributionFeature, 
	TimeSeriesFeature(FieldFunctionality.TimeStamp),
	ValueFeature(FieldFunctionality.Value);
	
	private FieldFunctionality[] funcs;
	DataFeatureType(FieldFunctionality ... funcs) {
		this.funcs = funcs;
	}
	public FieldFunctionality[] getFuncs() {
		return this.funcs;
	}
}