/**
 * 
 */
package edu.thu.keg.mdap.datamodel;


/**
 * A dataset containing statistics.
 * Usually not very large
 * @author Yuanchao Ma
 *
 */
public interface StatisticsDataSet extends DataSetFeature {
	public DataField getKeyField();
	
	public DataField[] getValueFields();
}
