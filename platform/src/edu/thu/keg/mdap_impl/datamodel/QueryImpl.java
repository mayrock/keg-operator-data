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
	
		
	QueryImpl(DataSet ds) {
		this.fields = ds.getDataFields();
		this.wheres = new ArrayList<WhereClause>();
		this.orders = new ArrayList<OrderClause>();
		this.setProvider(ds.getProvider());
	}
	QueryImpl(DataField[] fields, List<WhereClause> wheres,
			List<OrderClause> orders, DataProvider provider) {
		this.fields = fields;
		this.wheres = wheres;
		this.orders = orders;
		this.provider = provider;
	}

	private DataField[] fields;
	private List<WhereClause> wheres;
	private List<OrderClause> orders;
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
		return new QueryImpl(fields, this.wheres, this.orders, this.provider);
	}
	@Override
	public Query where(DataField field, Operator op, Object value) {
		List<WhereClause> wheres = new ArrayList<WhereClause>();
		wheres.addAll(this.wheres);
		wheres.add(new WhereClause(field, op, value));
		return new QueryImpl(this.fields, wheres, this.orders, this.provider);
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
	@Override
	public Query orderBy(DataField field, Order order) {
		List<OrderClause> orders = new ArrayList<OrderClause>();
		orders.addAll(this.orders);
		orders.add(new OrderClause(field, order));
		return new QueryImpl(this.fields, this.wheres, orders, this.provider);
	}
	@Override
	public List<OrderClause> getOrderClauses() {
		return this.orders;
	}

}
