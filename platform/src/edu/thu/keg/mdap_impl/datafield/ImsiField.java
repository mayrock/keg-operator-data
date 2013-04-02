/**
 * 
 */
package edu.thu.keg.mdap_impl.datafield;


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
	 * @param ds The DataSet which the constructed 
	 * field belongs to
	 */
	public ImsiField(String name, String desciption) {
		super(name, String.class, desciption, true);
	}
	/**
	 * Construct a instance within a DataSet,
	 *  using default name and description
	 * @param ds The DataSet which the constructed 
	 * field belongs to
	 */
	public ImsiField() {
		super("IMSI", String.class, 
				"IMSI number of a user", true);
	}

}
