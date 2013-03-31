/**
 * 
 */
package edu.thu.keg.mdap.provider;

import java.sql.ResultSet;

import edu.thu.keg.mdap.dataset.DataSet;

/**
 * A general implementation of the interface DataProvider,
 * with some abstract methods implemented. 
 * @author Yuanchao Ma
 */
public abstract class AbstractDataProvider implements DataProvider {

	protected String connString;
	@Override
	public ResultSet queryDataSet(DataSet ds) throws IllegalQueryException {
		return this.executeQuery(ds.getQueryStatement());
	}
	@Override
	public String getConnectionString() {
		return connString;
	}
	
	protected void init(String connString) {
		this.connString = connString;
	}

}
