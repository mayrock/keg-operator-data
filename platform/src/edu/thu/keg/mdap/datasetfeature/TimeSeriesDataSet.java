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
	 * @param keyField the key DataField
	 * @param timeField the time DataField
	 * @param valueFields the value DataFields
	 */
	public TimeSeriesDataSet(DataField keyField, DataField timeField, DataField[] valueFields) {
		super(keyField, valueFields);
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
