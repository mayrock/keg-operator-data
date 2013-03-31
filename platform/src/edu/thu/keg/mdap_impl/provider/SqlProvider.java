/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.provider.AbstractDataProvider;

/**
 * Interact with SQL database and provide datasets
 * @author myc
 *
 */
public class SqlProvider extends AbstractDataProvider {

	private SqlProvider(String connString) {
		init(connString);
	}
	
	//private Connection conn;
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.provider.DataProvider#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String query) {
		//TODO
		return null;
	}


}
