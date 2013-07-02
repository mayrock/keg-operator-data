/**
 * 
 */
package edu.thu.keg.mdap;

import edu.thu.keg.mdap.provider.DataProvider;

/**
 * This object provides information about all supported data providers of the
 * platform. This object should only be used by DataSet producers. This object
 * is responsible for aggregated management of data provider instances. For
 * convenience, getters for SQL and HIVE providers are built-in, as the two are
 * must-have basic provider types.
 * 
 * @author Yuanchao Ma, Bozhi Yuan
 * 
 */
public interface DataProviderManager {

	/**
	 * Get a DataProvider instance from a connection string
	 * 
	 * @param connString
	 *            the connection string of the demanded DataProvider
	 * @return A DataProvider instance. The instance is only created, and is not
	 *         guaranteed of connectivity
	 */
	public DataProvider getProvider(String connString);

	/**
	 * Get a data provider for a database stored in the default DBMS
	 * 
	 * @param dbName
	 *            Name of the database
	 * @return a provider instance for the database
	 */
	public DataProvider getDefaultSQLProvider(String dbName);

	/**
	 * @return the instance for the default hive provider.
	 */
	public DataProvider getDefaultHiveProvider();

	/**
	 * 
	 * @param dbName
	 *            the database name
	 * @param userName
	 *            the db username
	 * @param passWord
	 *            the db password
	 * @return the instance for the default oracle provider.
	 */
	public DataProvider getDefaultOracleProvider(String dbName,
			String userName, String passWord);

}
