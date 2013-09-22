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
 * Represents a data provider storing datasets. Can be a SQL database, Hive,
 * HDFS files, etc.. A DataProvider object is uniquely identified by a
 * Connection String
 * 
 * @author Yuanchao Ma, Bozhi Yuan
 */
public interface DataProvider {
	/**
	 * Open(run) the query against the data provider, and open the stream for
	 * reading data
	 * 
	 * @param query
	 *            the query
	 * @throws DataProviderException
	 *             when the query is invalid, or communication with the provider
	 *             returns a error.
	 */
	public void openQuery(Query query) throws DataProviderException;

	/**
	 * Get the connection string of this DataProvider
	 */
	public String getConnectionString();

	/**
	 * Advance a previously opened query
	 * 
	 * @param q
	 *            the query
	 * @return whether the query has past the last record
	 * @throws DataProviderException
	 *             when data provider gives an error
	 */
	public boolean next(Query q) throws DataProviderException;

	/**
	 * Get value of current record of a query, of the given field
	 * 
	 * @param q
	 *            the query
	 * @param field
	 *            the field
	 * @return the value
	 * @throws DataProviderException
	 *             when data provider gives an error
	 */
	public Object getValue(Query q, DataField field)
			throws DataProviderException;

	/**
	 * Close a query and release resource.
	 * 
	 * @param q
	 *            the query
	 * @throws DataProviderException
	 *             when data provider gives an error
	 */
	public void closeQuery(Query q) throws DataProviderException;
	/**
	 * 提供直接sql语句的接口
	 * @param rs
	 * @throws DataProviderException
	 */
	public void closeResultSet(ResultSet rs) throws DataProviderException;

	/**
	 * Get a string representation of a Query on this provider
	 * 
	 * @param q
	 *            the Query to be parsed
	 * @return A string that represent the Query and can be understand by the
	 *         provider
	 */
	public String getQueryString(Query q);

	/**
	 * Remove a dataset content from the provider
	 * 
	 * @param ds
	 *            the to-be-removed dataset.
	 * @throws DataProviderException
	 *             when data provider gives an error
	 */
	public void removeContent(DataSet ds) throws DataProviderException;

	/**
	 * Write to the data provider the actual data of the dataset. Existing data
	 * would be wiped first.
	 * 
	 * @param ds
	 *            the dataset whose data is to be written
	 * @param data
	 *            the data to be written
	 * @throws DataProviderException
	 *             when data provider gives an error
	 */
	public void writeDataSetContent(DataSet ds, DataContent data)
			throws DataProviderException;

	public String getUserName();

	public String getPassword();
/**
 * 提供直接关闭connection的语句
 * @param queryStr
 * @return
 * @throws IllegalQueryException
 */
	public ResultSet executeQuery(String queryStr) throws IllegalQueryException;
}
