/**
 * 
 */
package edu.thu.keg.mdap_impl.datamodel;

import edu.thu.keg.mdap.datamodel.GeneralDataField;



/**
 * A field containing IMSI number of a user
 * As IMSI is very frequently used, so this type 
 * is built in here. This field is always a key field
 * @author Yuanchao Ma
 */
public class ImsiField extends GeneralDataField {

	/**
	 * Construct a instance within a DataSet,
	 *   name and description
	 * @param name The name of this field
	 * @param description description of this field
	 */
	public ImsiField(String name, String description) {
		super(name, String.class, description, true);
	}
	/**
	 * Construct a instance within a DataSet,
	 *  using default name and description
	 */
	public ImsiField() {
		super("IMSI", String.class, 
				"IMSI number of a user", true);
	}

}
