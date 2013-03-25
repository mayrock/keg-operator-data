/**
 * 
 */
package edu.thu.keg.mdap.datafield;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.DataSet;

/**
 * @author myc
 * This class stands for a field in a dataset.
 * A concrete field should 
 * extend this class, or its sub-classes
 *
 */
public abstract class DataField {
	protected String columnName;
	protected DataSet dataset;
	protected String desp;
	protected boolean isKey;

	public String getColumnName() {
		return columnName;
	}
	public DataSet getDataSet() {
		return dataset;
	}
	public String getDescription() {
		return this.desp;
	}
	public boolean isKey() {
		return this.isKey;
	}
	protected void init(String name, DataSet ds, String desciption, boolean isKey) {
		this.columnName = name;
		this.dataset = ds;
		this.desp = desciption;
		this.isKey = isKey;
	}
	
	
	public Object getValue() throws OperationNotSupportedException, SQLException {
		return this.getValue(this.getDataSet().getResultSet());
	}
	@SuppressWarnings("rawtypes")
	public abstract Class getDataType();
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
