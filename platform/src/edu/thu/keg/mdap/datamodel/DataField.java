/**
 * 
 */
package edu.thu.keg.mdap.datamodel;



/**

 * This interface stands for a field in a dataset.
 * A concrete field should 
 * extend this class, or its sub-classes
 * @author Yuanchao Ma 
 */
public interface DataField {
	
	public enum FieldType { Int, Double, DateTime, ShortString, LongString, Text}
	/**
	 * Get the column name of the field
	 * @return The column name as a string
	 */
	public String getColumnName();
	/**
	 * Get the dataset which the field belongs
	 * @return Reference of the dataset
	 */
	public DataSet getDataSet();
	/**
	 * Get the description of the field, containing information
	 * about the column for human
	 * @return The description as a string
	 */
	public String getDescription();
	/**
	 * Whether the field is a key field in the dataset
	 * @return true when it is key, false otherwise
	 */
	public boolean isKey();
	/**
	 * 
	 * @return whether null value is allowed for this field
	 */
	public boolean allowNull();
	/**
	 * @return the type of the field
	 */
	public FieldType getFieldType();
	/**
	 * Set the DataSet which this field belongs to
	 * @param ds the DataSet instance
	 */
	public void setDataSet(DataSet ds);
}