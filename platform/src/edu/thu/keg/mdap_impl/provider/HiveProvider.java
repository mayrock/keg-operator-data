/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.dataset.AbstractDataSet;
import edu.thu.keg.mdap.provider.DataProvider;

/**
 * This class is responsible for interacting 
 * with Hive 
 * @author Yuanchao Ma
 */
public class HiveProvider extends DataProvider {
	private static HiveProvider instance;
	/**
	 * Get the unique instance of this Singleton
	 * @return reference of the unique instance
	 */
	public static synchronized HiveProvider getInstance() {
		if (instance == null)
			instance = new HiveProvider();
		return instance;
	}
	private HiveProvider() {}
	@Override
	public ResultSet executeQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet queryDataSet(AbstractDataSet ds) {
		return this.executeQuery(ds.getQueryStatement());
	}
	
}
