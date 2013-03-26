/**
 * 
 */
package edu.thu.keg.mdap.dataset;

import java.sql.ResultSet;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.provider.DataProvider;

/**
 * Stands for a dataset hosted in the platform.
 * @author Yuanchao Ma
 *
 */
public abstract class DataSet {
	
	private ResultSet resultSet = null;
	/**
	 * Get the name (unique identifier) of the dataset,
	 * like a table name in SQL or HIVE.
	 * @return the unique name. Default implementation
	 * is returning the class name. Subclass can override this.
	 */
	public String getName() {
		return this.getClass().getSimpleName();
	}
	private DataProvider provider = null;
	/**
	 * get query statement for querying data
	 * in the dataset. The statement would be 
	 * executed against the data provider. 
	 * @return A string representing the query statement
	 */
	public String getQueryStatement() {
		StringBuffer sb = new StringBuffer("SELECT ");
		
		for (DataField df : getDataFields()) {
			sb.append(df.getColumnName()).append(",");
		}
		
		sb.append("FROM ").append(getName()).append(" ");
		return sb.toString();
	}
	/**
	 * Return a disconnected in-memroy JDBC ResultSet
	 *  containing the records of this dataset, if it is loadable
	 * The default implementation uses HIVE.
	 * @return a JDBC ResultSet. The ResultSet is generated when the 
	 * method is called for the first time 
	 * @throws OperationNotSupportedException when dataset is not loadable
	 */
	public ResultSet getResultSet() throws OperationNotSupportedException {
		if (!isLoadable())
			throw new OperationNotSupportedException();
		if (resultSet == null)
			resultSet = provider.queryDataSet(this);
		return resultSet;
	}
	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * @return a boolean indicates whether this dataset is small enough to be loaded into memory
	 */
	public abstract boolean isLoadable();
	/**
	 * Get an array of the fields contained in the dataset
	 * @return an array containing all the fields in the schema
	 */
	public abstract DataField[] getDataFields();
}
