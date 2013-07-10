package edu.thu.keg.mdap_impl.datamodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		this.join = null;
		this.innerQuery = null;
		this.setProvider(ds.getProvider());

		this.state = DataContentState.Ready;
	}

	public QueryImpl(Query q) {
		this.fields = q.getFields();
		this.wheres = q.getWhereClauses();
		this.orders = q.getOrderClauses();
		this.join = q.getJoinOnClause();
		this.provider = q.getProvider();
		this.innerQuery = q.getInnerQuery();
		this.state = DataContentState.Ready;
	}

	QueryImpl(DataField[] fields, List<WhereClause> wheres,
			List<OrderClause> orders,JoinOnClause join, DataProvider provider, Query innerQuery) {
		if (innerQuery == null) {
			this.fields = fields;
			this.wheres = wheres;
			this.orders = orders;
			this.join = join;
			this.provider = provider;
			this.innerQuery = innerQuery;
		} else {
			this.fields = fields.clone();
			this.wheres = wheres;
			this.orders = orders;
			this.join = join;
			this.provider = provider;
			this.innerQuery = innerQuery;
		}
		this.state = DataContentState.Ready;
	}

	private Query innerQuery;
	private DataContentState state;

	private DataField[] fields;
	private List<WhereClause> wheres;
	private List<OrderClause> orders;
	private JoinOnClause join;

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
		Query q = null;
		if (this.getGroupByFields() != null) {
			q = new QueryImpl(fields, null,
					null, null, this.provider, this);
			transformFields(q);
		} else {
			q = new QueryImpl(fields, this.wheres, this.orders,
					this.join, this.provider, null);
		}
		return q;
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
			Query q = new QueryImpl(fields, wheres, null,
					null, this.provider, this);
			transformFields(q);
			return q;
		} else {
			wheres.addAll(this.wheres);
			wheres.add(new WhereClause(field, op, value));
			return new QueryImpl(fields, wheres, this.orders, this.join, this.provider,
					null);
		}
	}

	@Override
	public Query join(Query q2, Map<DataField, DataField> fieldsMap) {
		JoinOnClause newJoin = new JoinOnClause(q2, fieldsMap);
		Query q = new QueryImpl(this.fields, null, null, newJoin,
				this.provider, this);
		transformFields(q);
		return q;
	}

	@Override
	public List<WhereClause> getWhereClauses() {
		return this.wheres;
	}

	@Override
	public Query orderBy(String fieldName, Order order) {

		DataField field = null;
		for (DataField tdf : this.fields) {
			if (tdf.getName().equalsIgnoreCase(fieldName)) {
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
		return new QueryImpl(this.fields, this.wheres, orders, this.join, this.provider,
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

	private void transformFields(Query q) {
		for (int i = 0; i < q.getFields().length; i++) {
			DataField df = q.getFields()[i];
				q.getFields()[i] = new GeneralDataField(df.getName(),
						df.getFieldType(), df.getDescription(), df.isKey(),
						df.allowNull(), df.isDim(), df.getFunction(), q.getInnerQuery());
		}
		HashMap<DataField, DataField> nfs = new HashMap<DataField, DataField>();
		for (DataField fs : q.getJoinOnClause().getOns().keySet()) {
			DataField nk = new GeneralDataField(fs.getName(),
					fs.getFieldType(), fs.getDescription(), fs.isKey(),
					fs.allowNull(), fs.isDim(), fs.getFunction(), q.getJoinOnClause().getQuery());
			DataField df = q.getJoinOnClause().getOns().get(fs);
			DataField nv = new GeneralDataField(df.getName(),
					df.getFieldType(), df.getDescription(), df.isKey(),
					df.allowNull(), df.isDim(), df.getFunction(), q.getInnerQuery());
			
			nfs.put(nk,nv);		
		}
		q.getJoinOnClause().setOns(nfs);
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
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#hashCode()
	// */
	// @Override
	// public int hashCode() {
	// final int prime = 31;
	// int result = 1;
	// result = prime * result + Arrays.hashCode(fields);
	// result = prime * result
	// + ((innerQuery == null) ? 0 : innerQuery.hashCode());
	// result = prime * result + ((orders == null) ? 0 : orders.hashCode());
	// result = prime * result
	// + ((provider == null) ? 0 : provider.hashCode());
	// result = prime * result + ((state == null) ? 0 : state.hashCode());
	// result = prime * result + ((wheres == null) ? 0 : wheres.hashCode());
	// return result;
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see java.lang.Object#equals(java.lang.Object)
	// */
	// @Override
	// public boolean equals(Object obj) {
	// if (this == obj)
	// return true;
	// if (obj == null)
	// return false;
	// if (getClass() != obj.getClass())
	// return false;
	// QueryImpl other = (QueryImpl) obj;
	// if (!Arrays.equals(fields, other.fields))
	// return false;
	// if (innerQuery == null) {
	// if (other.innerQuery != null)
	// return false;
	// } else if (!innerQuery.equals(other.innerQuery))
	// return false;
	// if (orders == null) {
	// if (other.orders != null)
	// return false;
	// } else {
	// if (orders.size() != other.orders.size())
	// return false;
	// for (int i = 0; i < orders.size(); i++) {
	// if (!orders.get(i).getField()
	// .equals(other.orders.get(i).getField()))
	// return false;
	// if (orders.get(i).getOrder()
	// .compareTo(other.orders.get(i).getOrder()) != 0)
	// return false;
	// }
	// }
	// if (provider == null) {
	// if (other.provider != null)
	// return false;
	// } else if (!provider.getConnectionString().equals(
	// other.provider.getConnectionString()))
	// return false;
	// if (state != other.state)
	// return false;
	// if (wheres == null) {
	// if (other.wheres != null)
	// return false;
	// } else {
	// if (wheres.size() != other.wheres.size())
	// return false;
	// for (int i = 0; i < wheres.size(); i++) {
	// if (!wheres.get(i).getField()
	// .equals(other.wheres.get(i).getField()))
	// return false;
	// if (wheres.get(i).getOperator()
	// .compareTo(other.wheres.get(i).getOperator()) != 0)
	// return false;
	// if (wheres.get(i).getValue() != other.wheres.get(i).getValue())
	// return false;
	// }
	// }
	// return true;
	// }

	@Override
	public JoinOnClause getJoinOnClause() {
		return this.join;
	}
}
