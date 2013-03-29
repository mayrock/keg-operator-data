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

	@Override
	public ResultSet queryDataSet(DataSet ds) {
		return this.executeQuery(ds.getQueryStatement());
	}
}
