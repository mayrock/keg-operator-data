/**
 * 
 */
package edu.thu.keg.mdap;

import java.sql.ResultSet;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafield.DataField;

/**
 * @author myc
 * Stands for a dataset hosted in the platform.
 *
 */
public abstract class DataSet {
	
	private ResultSet resultSet = null;
	public String getName() {
		return this.getClass().getSimpleName();
	}
	/**
	 * get HIVE query statement for querying data
	 * in the dataset. 
	 * @return
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
			resultSet = HiveQuerier.executeQuery(this.getQueryStatement());
		return resultSet;
	}
	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * @return
	 */
	public abstract boolean isLoadable();
	/**
	 * Get an array of the fields contained in the dataset
	 * @return
	 */
	public abstract DataField[] getDataFields();
}
