/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.sql.ResultSet;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * @author Yuanchao Ma
 *
 */
public interface DataSet {
	/**
	 * Get the name (unique identifier) of the dataset,
	 * like a table name in SQL or HIVE.
	 * @return the unique name. 
	 */
	public String getName();
	/**
	 * get query statement for querying data
	 * in the dataset. The statement would be 
	 * executed against the data provider. 
	 * @return A string representing the query statement
	 */
	public String getQueryStatement();
	/**
	 * Return a connected ResultSet
	 *  containing the records of this dataset, if it is loadable.
	 * @return a JDBC ResultSet. The ResultSet is generated when the 
	 * method is called for the first time. 
	 * @throws OperationNotSupportedException when dataset is not loadable
	 * @throws DataProviderException when actual data of the DataSet does not
	 * exist on the DataProvider
	 */
	public ResultSet getQuery() throws OperationNotSupportedException, DataProviderException;
	
	/**
	 * Get the provider of this DataSet
	 * @return a DataProvider instance
	 */
	public DataProvider getProvider();
	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * @return a boolean indicates whether this dataset is small enough to be loaded into memory
	 */
	public boolean isLoadable();
	/**
	 * Get an array of the fields contained in the dataset
	 * @return an array containing all the fields in the schema
	 */
	public DataField[] getDataFields();
	/**
	 * Close the ResultSet of this DataSet, if any.
	 * @throws SQLException 
	 * @throws DataProviderException 
	 */
	public void closeResultSet() throws DataProviderException;
}
