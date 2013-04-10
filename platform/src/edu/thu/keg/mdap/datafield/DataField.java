/**
 * 
 */
package edu.thu.keg.mdap.datafield;

import java.sql.ResultSet;
import java.sql.SQLException;

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
	 * Get the value of this field from the given ResultSet
	 * @return A reference of the value
	 * @throws SQLException when reading the resultset, i.e.
	 * the column name doesnot exist in the resultset
	 */
	public Object getValue(ResultSet rs) throws SQLException;
	@SuppressWarnings("rawtypes")
	/**
	 * @return the type of the field
	 */
	public Class getDataType();
	/**
	 * Set the DataSet which this field belongs to
	 * @param ds the DataSet instance
	 */
	public void setDataSet(DataSet ds);
}
