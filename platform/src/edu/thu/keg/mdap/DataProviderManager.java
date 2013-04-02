/**
 * 
 */
package edu.thu.keg.mdap;

import java.sql.SQLException;

import edu.thu.keg.mdap.provider.DataProvider;

/**
 * This object provides information
 * about all supported data providers of the platform.
 * This object should only be used by DataSet producers.
 * This object is responsible for aggregated management
 * of data provider instances. 
 * @author Yuanchao Ma
 *
 */
public interface DataProviderManager {

	/**
	 * Get a DataProvider instance from a connection string
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public DataProvider getProvider(String connString);
}
