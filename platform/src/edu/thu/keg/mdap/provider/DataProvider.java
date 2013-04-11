/**
 * 
 */
package edu.thu.keg.mdap.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;

/**
 * Represents a data provider storing datasets. 
 * Can be a SQL database, Hive, HDFS files, etc..
 * A DataProvider object is uniquely identified 
 * by a Connection String
 * @author Yuanchao Ma
 */
public interface DataProvider {
	/**
	 * Run a query against the DataProvider and return ResultSet
	 * @param query the query string returning a table
	 * @return the disconnected ResultSet returned by the query.
	 * @throws DataProviderException when the query is invalid,
	 * 	or communication with the provider returns a error.
	 */
	public void openQuery(Query query) throws DataProviderException;
	/**
	 * 
	 * @return The connection string of this DataProvider
	 */
	public String getConnectionString();
	
	public boolean next(Query q) throws DataProviderException ;
	
	public Object getValue(Query q, DataField field) throws DataProviderException ;
	
	public void removeDataSet(String dsName);
	
	public void writeDataSetContent(DataSet ds, DataContent data);
}
