/**
 * 
 */
package edu.thu.keg.mdap_impl.dataset;

import java.sql.ResultSet;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.dataset.DataSet;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * A general implementation of the interface DataSet
 * @author Yuanchao Ma
 *
 */
public class DataSetImpl implements DataSet {
	private ResultSet resultSet = null;
	private String name = null;
	private DataProvider provider = null;
	private boolean loadable;
	private DataField[] fields;

	private String defaultStmt(DataField[] fields) {
		StringBuffer sb = new StringBuffer("SELECT ");

		for (int i = 0; i < fields.length - 1; i++) {
			DataField df = fields[i];
			sb.append(df.getColumnName()).append(",");
		}
		sb.append(fields[fields.length - 1].getColumnName());

		sb.append(" FROM ").append(getName()).append(" ");
		return sb.toString();
	}
	public DataSetImpl(){
	
	}


	public DataSetImpl(String name, DataProvider provider,
			 boolean loadable, DataField[] fields) {
		super();
		this.name = name;
		this.provider = provider;
		this.loadable = loadable;
		setDataFields(fields);
	}
	
	private void setDataFields(DataField[] fields) {
		this.fields = fields;
		for (DataField field : fields) {
			field.setDataSet(this);
		}
	}



	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public String getQueryStatement() {
		return defaultStmt(getDataFields());
	}
	@Override
	public ResultSet getResultSet() throws 
	OperationNotSupportedException, DataProviderException {
		if (!isLoadable())
			throw new OperationNotSupportedException();
		if (resultSet == null)
			resultSet = provider.queryDataSet(this);
		return resultSet;
	}
	@Override
	public boolean isLoadable() {
		return this.loadable;
	}
	@Override
	public DataField[] getDataFields() {
		return this.fields;
	}
	@Override
	public ResultSet getResultSet(DataField[] fields)
			throws OperationNotSupportedException, DataProviderException {
		String stmt = defaultStmt(getDataFields());
		return provider.executeQuery(stmt);
	}
	@Override
	public DataProvider getProvider() {
		return this.provider;
	}
	@Override
	public void closeResultSet() throws DataProviderException {
		if (resultSet == null) {
			return;
		} else {
			getProvider().closeResultSet(resultSet);
		}
	}
}
