/**
 * 
 */
package edu.thu.keg.mdap.datasetfeature;

import edu.thu.keg.mdap.datamodel.DataField;

/**
 * 
 * @author Yuanchao Ma
 *
 */
public class TimeSeriesDataSet extends StatisticsDataSet {
	
	/**
	 * Init a new TimeSeriesDataSet instance with all its fields
	 * @param keyFields the key DataFields
	 * @param timeField the time DataField
	 * @param valueFields the value DataFields
	 */
	public TimeSeriesDataSet(DataField[] keyFields, DataField timeField, DataField[] valueFields) {
		super(keyFields, valueFields);
		this.timeField = timeField;
	}
	
	/**
	 * Init a new TimeSeriesDataSet instance with 1 key field and 1 value field
	 * @param keyField the key DataField
	 * @param timeField the time DataField
	 * @param valueField the value DataField
	 */
	public TimeSeriesDataSet(DataField keyField, DataField timeField, DataField valueField) {
		super(keyField, valueField);
		this.timeField = timeField;
	}

	private DataField timeField;
	/**
	 * 
	 * @return the DataField storing the time data
	 */
	public DataField getTimeField() {
		return this.timeField;
	}
}
