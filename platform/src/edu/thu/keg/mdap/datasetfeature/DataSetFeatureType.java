/**
 * 
 */
package edu.thu.keg.mdap.datasetfeature;

import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;

/**
 * @author myc
 *
 */
public enum DataSetFeatureType {
	GeoFeature(FieldFunctionality.Latitude, FieldFunctionality.Longitude),
	DistributionFeature, 
	TimeSeriesFeature(FieldFunctionality.TimeStamp);
	
	private FieldFunctionality[] funcs;
	DataSetFeatureType(FieldFunctionality ... funcs) {
		this.funcs = funcs;
	}
	public FieldFunctionality[] getFuncs() {
		return this.funcs;
	}
}
