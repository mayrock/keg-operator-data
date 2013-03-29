/**
 * 
 */
package edu.thu.keg.mdap.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.dataset.DataSet;

/**
 * Represents a data provider storing datasets. 
 * Can be a SQL database, Hive, HDFS files, etc..
 * It is recommended that subclasses are designed
 * as singletons
 * @author Yuanchao Ma
 */
public interface DataProvider {
	/**
	 * Run a query against the dataset and return ResultSet
	 * @param query the query string returning a table
	 * @return the disconnected ResultSet returned by the query.
	 */
	public abstract ResultSet executeQuery(String query);
	/**
	 * Query the data provider the get the ResultSet of a dataset.
	 * Default implementation is calling executeQuery on the query statement of the 
	 * dataset.
	 * @param ds the dataset whose contents (ResultSet) are expected
	 * @return the disconnected ResultSet of the dataset
	 */
	public ResultSet queryDataSet(DataSet ds);
}
