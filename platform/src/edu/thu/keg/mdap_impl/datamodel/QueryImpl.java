package edu.thu.keg.mdap_impl.datamodel;

import java.util.ArrayList;
import java.util.List;

import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Simple implementation of the Query interface.
 * Only a wrapper around plain SQL query.
 * @author Yuanchao Ma
 *
 */
public class QueryImpl implements Query {
	
		
	public QueryImpl(DataSet ds) {
		this.fields = ds.getDataFields();
		this.wheres = new ArrayList<WhereClause>();
	}
	public QueryImpl(DataField[] fields, List<WhereClause> wheres,
			DataProvider provider) {
		this.fields = fields;
		this.wheres = wheres;
		this.provider = provider;
	}

	private DataField[] fields;
	private List<WhereClause> wheres;
	@Override
	public DataField[] getFields() {
		return fields;
	}

	private DataProvider provider = null;
	@Override
	public DataProvider getProvider() {
		return this.provider;
	}

	@Override
	public void setProvider(DataProvider provider) { 
		this.provider = provider;
	}
	@Override
	public void close() throws DataProviderException {
		this.getProvider().closeQuery(this);
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
	@Override
	public Query select(DataField[] fields) {
		return new QueryImpl(fields, this.wheres, this.provider);
	}
	@Override
	public Query where(DataField field, Operator op, Object value) {
		List<WhereClause> wheres = new ArrayList<WhereClause>();
		wheres.addAll(this.wheres);
		wheres.add(new WhereClause(field, op, value));
		return new QueryImpl(this.fields, wheres, this.provider);
	}
	@Override
	public Query join(Query q2, DataField f1, DataField f2) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DataField[] getDataFields() {
		return this.fields;
	}
	@Override
	public List<WhereClause> getWhereClauses() {
		return this.wheres;
	}

}
