/**
 * 
 */
package edu.thu.keg.mdap.datafield;

import java.sql.SQLException;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.dataset.DataSet;

/**

 * This interface stands for a field in a dataset.
 * A concrete field should 
 * extend this class, or its sub-classes
 * @author Yuanchao Ma 
 */
public interface DataField {
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
	 * Get the value of this field of the current record
	 * @return A reference of the value
	 * @throws OperationNotSupportedException If the dataset cannot be loaded
	 * @throws SQLException when reading the resultset
	 */
	public Object getValue() throws OperationNotSupportedException, SQLException;
	@SuppressWarnings("rawtypes")
	/**
	 * Get the type of the field
	 * @return
	 */
	public Class getDataType();
}
