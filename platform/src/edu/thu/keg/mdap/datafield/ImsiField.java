/**
 * 
 */
package edu.thu.keg.mdap.datafield;

import edu.thu.keg.mdap.DataSet;

/**
 * @author myc
 * A field containing IMSI number of a user
 * As IMSI is very frequently used, so this type 
 * is built in here.
 */
public class ImsiField extends DataField {

	@SuppressWarnings("rawtypes")
	@Override
	public Class getDataType() {
		return String.class;
	}
	
	public ImsiField(String name, DataSet ds, String desciption) {
		init(name, ds, desciption, true);
	}
	
	public ImsiField(DataSet ds) {
		init("IMSI", ds, 
				"IMSI number of a user", true);
	}

}
