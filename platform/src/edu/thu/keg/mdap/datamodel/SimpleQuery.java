package edu.thu.keg.mdap.datamodel;

import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Simple implementation of the Query interface.
 * Only a wrapper around plain SQL query.
 * @author Yuanchao Ma
 *
 */
public class SimpleQuery implements Query {
	
	public SimpleQuery(DataField[] fields, String queryString) {
		this.fields = fields;
		this.queryString = queryString;
	}

	private DataField[] fields;
	@Override
	public DataField[] getFields() {
		return fields;
	}

	private DataProvider provider = null;
	@Override
	public DataProvider getProvider() {
		return this.provider;
	}

	private String queryString;
	@Override
	public String getQueryString() {
		return this.queryString;
	}
	@Override
	public void setProvider(DataProvider provider) { 
		this.provider = provider;
	}
	@Override
	public void close() throws DataProviderException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean next() throws DataProviderException {
		return this.getProvider().next(this);
	}
	@Override
	public Object getValue(DataField field) throws DataProviderException {
		return this.getProvider().getValue(this, field);
	}
	@Override
	public void open() throws DataProviderException {
		this.getProvider().openQuery(this);
	}

}
