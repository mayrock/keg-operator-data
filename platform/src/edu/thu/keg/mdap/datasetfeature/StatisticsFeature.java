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
public class StatisticsFeature extends DataSetFeature {
	

	
	/**
	 * Init a new StatisticsDataSet with all its fields
	 * @param keyFields the key DataFields
	 * @param valueFields the value fields
	 */
	public StatisticsFeature(DataField[] keyFields, DataField[] valueFields) {
		super(keyFields, valueFields);
	}
	
	/**
	 * Init a new StatisticsDataSet with 1 key field and 1 value field
	 * @param keyField the key DataField
	 * @param valueField the value field
	 */
	public StatisticsFeature(DataField keyField, DataField valueField) {
		super(keyField, valueField);
	}
}
