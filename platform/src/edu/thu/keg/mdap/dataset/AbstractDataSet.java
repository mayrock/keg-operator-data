/**
 * 
 */
package edu.thu.keg.mdap.dataset;

import java.sql.ResultSet;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.IllegalQueryException;

/**
 * A general implementation of the interface DataSet
 * @author Yuanchao Ma
 *
 */
public abstract class AbstractDataSet implements DataSet {
	
	private ResultSet resultSet = null;
	
	private DataProvider provider = null;

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	@Override
	public String getQueryStatement() {
		StringBuffer sb = new StringBuffer("SELECT ");
		
		for (DataField df : getDataFields()) {
			sb.append(df.getColumnName()).append(",");
		}
		
		sb.append("FROM ").append(getName()).append(" ");
		return sb.toString();
	}

	@Override
	public ResultSet getResultSet() throws OperationNotSupportedException, IllegalQueryException {
		if (!isLoadable())
			throw new OperationNotSupportedException();
		if (resultSet == null)
			resultSet = provider.queryDataSet(this);
		return resultSet;
	}

	
	

	
	
}
