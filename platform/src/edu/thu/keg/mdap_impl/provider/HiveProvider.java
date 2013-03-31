/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.AbstractDataProvider;
import edu.thu.keg.mdap.provider.IllegalQueryException;

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
	public ResultSet queryDataSet(DataSet ds) throws IllegalQueryException {
		return this.executeQuery(ds.getQueryStatement());
	}


	@Override
	public ResultSet executeQuery(String query) throws IllegalQueryException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
