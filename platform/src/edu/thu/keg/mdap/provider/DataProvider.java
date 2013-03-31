/**
 * 
 */
package edu.thu.keg.mdap.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.dataset.DataSet;

/**
 * Represents a data provider storing datasets. 
 * Can be a SQL database, Hive, HDFS files, etc..
 * A DataProvider object is uniquely identified 
 * by a Connection String
 * @author Yuanchao Ma
 */
public interface DataProvider {
	/**
	 * Run a query against the dataset and return ResultSet
	 * @param query the query string returning a table
	 * @return the disconnected ResultSet returned by the query.
	 * @throws IllegalQueryException when the DataSet (with its name) does not exist on the DataProvider.
	 */
	public abstract ResultSet executeQuery(String query) throws IllegalQueryException;
	/**
	 * Query the data provider the get the ResultSet of a dataset.
	 * Default implementation is calling executeQuery on the query statement of the 
	 * dataset.
	 * @param ds the dataset whose contents (ResultSet) are expected
	 * @return the disconnected ResultSet of the dataset
	 * @throws IllegalQueryException when the DataSet (with its name) does not exist on the DataProvider.
	 */
	public ResultSet queryDataSet(DataSet ds) throws IllegalQueryException;
	/**
	 * 
	 * @return The connection string of this DataProvider
	 */
	public String getConnectionString();
}
