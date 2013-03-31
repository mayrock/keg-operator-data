/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.AbstractDataProvider;

/**
 * This class is responsible for interacting 
 * with Hive 
 * @author Yuanchao Ma
 */
public class HiveProvider extends AbstractDataProvider {
	
	public HiveProvider(String connString) {
		init(connString);
		//TODO
	}
	@Override
	public ResultSet executeQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet queryDataSet(DataSet ds) {
		return this.executeQuery(ds.getQueryStatement());
	}
	
}
