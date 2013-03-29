/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.Connection;
import java.sql.ResultSet;

import edu.thu.keg.mdap.provider.AbstractDataProvider;

/**
 * Interact with SQL database and provide datasets
 * @author myc
 *
 */
public class SqlProvider extends AbstractDataProvider {

	private static SqlProvider instance;
	private SqlProvider() {
		
	}
	/**
	 * Get the unique instance of this Singleton
	 * @return reference of the unique instance
	 */
	public static synchronized SqlProvider getInstance() {
		if (instance == null)
			instance = new SqlProvider();
		return instance;
	}
	
	private Connection conn;
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.provider.DataProvider#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String query) {
		//TODO
		return null;
	}

}
