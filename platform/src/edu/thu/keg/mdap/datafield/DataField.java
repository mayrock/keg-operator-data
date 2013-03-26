/**
 * 
 */
package edu.thu.keg.mdap.datafield;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.dataset.DataSet;

/**

 * This class stands for a field in a dataset.
 * A concrete field should 
 * extend this class, or its sub-classes
 * @author Yuanchao Ma 
 */
public abstract class DataField {
	protected String columnName;
	protected DataSet dataset;
	protected String desp;
	protected boolean isKey;
	/**
	 * Get the column name of the field
	 * @return The column name as a string
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * Get the dataset which the field belongs
	 * @return Reference of the dataset
	 */
	public DataSet getDataSet() {
		return dataset;
	}
	/**
	 * Get the description of the field, containing information
	 * about the column for human
	 * @return The description as a string
	 */
	public String getDescription() {
		return this.desp;
	}
	/**
	 * Whether the field is a key field in the dataset
	 * @return true when it is key, false otherwise
	 */
	public boolean isKey() {
		return this.isKey;
	}
	/**
	 * Initialize the fields of the object
	 * @param name Name of the column
	 * @param ds DataSet which the field belongs to
	 * @param desciption Description of the field
	 * @param isKey Whether the field is key
	 */
	protected void init(String name, DataSet ds, String desciption, boolean isKey) {
		this.columnName = name;
		this.dataset = ds;
		this.desp = desciption;
		this.isKey = isKey;
	}
	
	/**
	 * Get the value of this field of the current record
	 * @return A reference of the value
	 * @throws OperationNotSupportedException If the dataset cannot be loaded
	 * @throws SQLException when reading the resultset
	 */
	public Object getValue() throws OperationNotSupportedException, SQLException {
		return this.getValue(this.getDataSet().getResultSet());
	}
	@SuppressWarnings("rawtypes")
	/**
	 * Get the type of the field
	 * @return
	 */
	public abstract Class getDataType();
	/**
	 * Get value of the field of the current record of the given resultset.
	 * @param rs The field should be contained in rs
	 * @return A reference of the value
	 * @throws SQLException when reading the resultset
	 */
	protected Object getValue(ResultSet rs) throws SQLException {
		if (getDataType() == String.class) {
			return rs.getString(this.getColumnName());
		} else if (getDataType() == Double.class) {
			return rs.getDouble(this.getColumnName());
		} else if (getDataType() == Integer.class) {
			return rs.getInt(this.getColumnName());
		} else if (getDataType() == Date.class) {
			return rs.getDate(this.getColumnName());
		}
		throw new IllegalArgumentException("Type of this field is not supported");
	}
}
