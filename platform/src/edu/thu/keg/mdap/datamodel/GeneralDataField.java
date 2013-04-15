/**
 * 
 */
package edu.thu.keg.mdap.datamodel;



/**

 * A general implementation of the interface DataField.
 * This class can be instantiated directly, or sub-classed 
 * to form other specific-purpose fields.
 * @author Yuanchao Ma 
 */
public class GeneralDataField implements DataField {
	protected String columnName;
	protected DataSet dataset;
	protected String desp;
	protected boolean isKey;
	protected FieldType type;
	protected boolean allowNull;
	@Override
	public String getColumnName() {
		return columnName;
	}
	@Override
	public DataSet getDataSet() {
		return dataset;
	}
	@Override
	public String getDescription() {
		return this.desp;
	}
	@Override
	public boolean isKey() {
		return this.isKey;
	}
	/**
	 * Construct a instance using the given
	 *   name, type and description, and whether is key
	 * Initialize the fields of the object
	 * @param name Name of the column
	 * @param ds DataSet which the field belongs to
	 * @param description Description of the field
	 * @param isKey Whether the field is key
	 * @param allowNull Whether the field allows null value
	 */
	public GeneralDataField(String name, FieldType type, String description,
			boolean isKey, boolean allowNull) {
		if (isKey == true && allowNull == true)
			throw new IllegalArgumentException();
		this.columnName = name;
		this.type = type;
		this.dataset = null;
		this.desp = description;
		this.isKey = isKey;
		this.allowNull = allowNull;
	}
	
	/**
	 * Construct a instance using the given
	 *   name, type and description, and whether is key
	 * Initialize the fields of the object
	 * @param name Name of the column
	 * @param ds DataSet which the field belongs to
	 * @param description Description of the field
	 * @param isKey Whether the field is key
	 */
	public GeneralDataField(String name, FieldType type, String description,
			boolean isKey) {
		this.columnName = name;
		this.type = type;
		this.dataset = null;
		this.desp = description;
		this.isKey = isKey;
		this.allowNull = !isKey;
	}
		
	@Override
	public FieldType getFieldType() {
		return this.type;
	}

	@Override
	public void setDataSet(DataSet ds) {
		this.dataset = ds;
	}
	@Override
	public boolean allowNull() {
		return this.allowNull;
	}
}
