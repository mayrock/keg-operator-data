/**
 * 
 */
package edu.thu.keg.mdap.datafield;

import edu.thu.keg.mdap.dataset.AbstractDataSet;

/**
 * A field containing IMSI number of a user
 * As IMSI is very frequently used, so this type 
 * is built in here. This field is always a key field
 * @author Yuanchao Ma
 */
public class ImsiField extends DataField {

	@SuppressWarnings("rawtypes")
	@Override
	public Class getDataType() {
		return String.class;
	}
	/**
	 * Construct a instance within a DataSet,
	 *   name and description
	 * @param ds The DataSet which the constructed 
	 * field belongs to
	 */
	public ImsiField(String name, AbstractDataSet ds, String desciption) {
		init(name, ds, desciption, true);
	}
	/**
	 * Construct a instance within a DataSet,
	 *  using default name and description
	 * @param ds The DataSet which the constructed 
	 * field belongs to
	 */
	public ImsiField(AbstractDataSet ds) {
		init("IMSI", ds, 
				"IMSI number of a user", true);
	}

}
