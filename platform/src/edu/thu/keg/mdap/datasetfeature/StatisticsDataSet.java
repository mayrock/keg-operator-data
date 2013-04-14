/**
 * 
 */
package edu.thu.keg.mdap.datasetfeature;

import edu.thu.keg.mdap.datamodel.DataField;



/**
 * A dataset containing statistics.
 * Usually not very large
 * @author Yuanchao Ma
 *
 */
public class StatisticsDataSet extends DataSetFeature {
	
	private DataField keyField;
	private DataField[] valueFields;
	/**
	 * 
	 * @return the key DataField
	 */
	public DataField getKeyField() {
		return this.keyField;
	}
	/**
	 * 
	 * @return value fields
	 */
	public DataField[] getValueFields() {
		return this.valueFields;
	}
	/**
	 * Init a new StatisticsDataSet with all its fields
	 * @param keyField the key DataField
	 * @param valueFields the value fields
	 */
	public StatisticsDataSet(DataField keyField, DataField[] valueFields) {
		this.keyField = keyField;
		this.valueFields = valueFields;
	}
}
