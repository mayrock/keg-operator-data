/**
 * 
 */
package edu.thu.keg.mdap;

/**
 * @author myc
 *
 */
public abstract class GeographicalDataSet extends DataSet {
	public abstract String getLatitudeFieldName();
	public abstract String getLongtitudeFieldName();
	public abstract boolean isOrdered();
}
