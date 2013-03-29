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

 * A general implementation of the interface DataField.
 * This class can be instantiated directly, or sub-classed 
 * to form other specific-purpose fields.
 * @author Yuanchao Ma 
 */
@SuppressWarnings("rawtypes")
public class GeneralDataField implements DataField {
	protected String columnName;
	protected DataSet dataset;
	protected String desp;
	protected boolean isKey;
	protected Class c;
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
	 * Construct a instance within a DataSet,
	 *   name and description
	 * @param ds The DataSet which the constructed 
	 * field belongs to
	 */
	public GeneralDataField(String name, Class type, DataSet ds, String desciption, boolean isKey) {
		init(name, type, ds, desciption, isKey);
	}
	/**
	 * Initialize the fields of the object
	 * @param name Name of the column
	 * @param ds DataSet which the field belongs to
	 * @param desciption Description of the field
	 * @param isKey Whether the field is key
	 */
	protected void init(String name, Class type, DataSet ds, String desciption, boolean isKey) {
		this.columnName = name;
		this.c = type;
		this.dataset = ds;
		this.desp = desciption;
		this.isKey = isKey;
	}
	
	@Override
	public Object getValue() throws OperationNotSupportedException, SQLException {
		return this.getValue(this.getDataSet().getResultSet());
	}

	@Override
	public Class getDataType() {
		return this.c;
	}
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
