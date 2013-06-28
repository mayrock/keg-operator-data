package edu.thu.keg.mdap_impl.datamodel;

import java.util.ArrayList;
import java.util.List;

import edu.thu.keg.mdap.datamodel.AggregatedDataField;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Simple implementation of the Query interface. Only a wrapper around plain SQL
 * query.
 * 
 * @author Yuanchao Ma
 * 
 */
public class QueryImpl implements Query {

	QueryImpl(DataSet ds) {
		this.fields = ds.getDataFields().toArray(new DataField[0]);
		this.wheres = new ArrayList<WhereClause>();
		this.orders = new ArrayList<OrderClause>();
		this.innerQuery = null;
		this.setProvider(ds.getProvider());

		this.state = DataContentState.Ready;
	}

	public QueryImpl(Query q) {
		this.fields = q.getFields();
		this.wheres = q.getWhereClauses();
		this.orders = q.getOrderClauses();
		this.provider = q.getProvider();
		this.innerQuery = q.getInnerQuery();
		this.state = DataContentState.Ready;
	}

	QueryImpl(DataField[] fields, List<WhereClause> wheres,
			List<OrderClause> orders, DataProvider provider, Query innerQuery) {
		this.fields = fields;
		this.wheres = wheres;
		this.orders = orders;
		this.provider = provider;
		this.innerQuery = innerQuery;
		this.state = DataContentState.Ready;
	}

	private Query innerQuery;
	private DataContentState state;

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
		this.state = DataContentState.Closed;
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
		this.state = DataContentState.Opened;
	}

	@Override
	public Query select(DataField... fields) {
		if (this.getGroupByFields() != null)
			return new QueryImpl(transformFields(fields), this.wheres,
					this.orders, this.provider, this);
		else
			return new QueryImpl(fields, this.wheres, this.orders,
					this.provider, null);
	}

	@Override
	public Query where(String fieldName, Operator op, Object value) {
		DataField field = null;
		for (DataField tdf : this.fields) {
			if (tdf.getName().equals(fieldName)) {
				field = tdf;
			}
		}
		if (field == null) {
			throw new IllegalArgumentException("The field " + fieldName
					+ " does not exist in the field list of this query");
		}
		List<WhereClause> wheres = new ArrayList<WhereClause>();
		if (field instanceof AggregatedDataField) {
			wheres.add(new WhereClause(field, op, value));
			return new QueryImpl(transformFields(fields), wheres, this.orders,
					this.provider, this);
		} else {
			wheres.addAll(this.wheres);
			wheres.add(new WhereClause(field, op, value));
			return new QueryImpl(fields, wheres, this.orders, this.provider,
					null);
		}
	}

	@Override
	public Query join(Query q2, DataField f1, DataField f2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WhereClause> getWhereClauses() {
		return this.wheres;
	}

	@Override
	public Query orderBy(String fieldName, Order order) {
		DataField field = null;
		for (DataField tdf : this.fields) {
			if (tdf.getName().equals(fieldName)) {
				field = tdf;
			}
		}
		if (field == null) {
			throw new IllegalArgumentException("The field " + fieldName
					+ " does not exist in the field list of this query");
		}
		List<OrderClause> orders = new ArrayList<OrderClause>();
		orders.addAll(this.orders);
		orders.add(new OrderClause(field, order));
		return new QueryImpl(this.fields, this.wheres, orders, this.provider,
				null);
	}

	@Override
	public List<OrderClause> getOrderClauses() {
		return this.orders;
	}

	@Override
	public Query getInnerQuery() {
		return this.innerQuery;
	}

	@Override
	public List<DataField> getGroupByFields() {
		List<DataField> gf = new ArrayList<DataField>();
		int count = 0;
		for (DataField f : this.fields) {
			if (!(f instanceof AggregatedDataField)) {
				gf.add(f);
				count++;
			}
		}
		if (count == this.fields.length) {
			return null;
		}
		return gf;
	}

	private DataField[] transformFields(DataField... dataFields) {
		DataField[] nfs = new DataField[dataFields.length];
		for (int i = 0; i < dataFields.length; i++) {
			DataField df = dataFields[i];
			if (df instanceof AggregatedDataField) {
				DataField ndf = new GeneralDataField(df.getName(),
						df.getFieldType(), df.getDescription(), df.isKey(),
						df.allowNull(), df.isDim(), df.getFunction());
				nfs[i] = ndf;
			} else {
				nfs[i] = df;
			}
		}
		return nfs;
	}

	@Override
	public DataContentState getState() {
		return this.state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.provider.getQueryString(this);
	}

}
