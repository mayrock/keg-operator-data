/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.List;
import java.util.Map;

import edu.thu.keg.mdap.provider.DataProvider;

/**
 * Used for getting data from a data source. It is the unified interface 
 * for querying data. 
 * @author Yuanchao Ma
 *
 */
public interface Query extends DataContent {
	/**
	 * Representing a Where clause in the Query for 
	 * filtering data
	 * @author Yuanchao Ma
	 *
	 */
	class WhereClause {
		DataField field;
		Operator operator;
		Object value;
		/**
		 * @param field the DataField to filter on
		 * @param operator the comparison operator
		 * @param value to value to be compared with
		 */
		public WhereClause(DataField field, Operator operator, Object value) {
			super();
			this.field = field;
			this.operator = operator;
			this.value = value;
		}
		/**
		 * @return the field
		 */
		public DataField getField() {
			return field;
		}
		/**
		 * @return the operator
		 */
		public Operator getOperator() {
			return operator;
		}
		/**
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}
	}
	/**
	 * Representing a Order clause in the Query for 
	 * ordering data
	 * @author Yuanchao Ma
	 *
	 */
	class OrderClause {
		DataField field;
		Order order;
		/**
		 * 
		 * @param field the DataField to order on
		 * @param order the Order type
		 */
		public OrderClause(DataField field, Order order) {
			super();
			this.field = field;
			this.order = order;
		}
		/**
		 * @return the field
		 */
		public DataField getField() {
			return field;
		}
		/**
		 * @return the order
		 */
		public Order getOrder() {
			return order;
		}
		
	}
	
	class JoinOnClause {
		Query query;
		Map<DataField, DataField> ons;
		
		
		/**
		 * @param query
		 * @param ons
		 */
		public JoinOnClause(Query query, Map<DataField, DataField> ons) {
			super();
			this.query = query;
			this.ons = ons;
		}
		/**
		 * @param ons the ons to set
		 */
		public void setOns(Map<DataField, DataField> ons) {
			this.ons = ons;
		}
		/**
		 * @return the query
		 */
		public Query getQuery() {
			return query;
		}
		/**
		 * @return the ons
		 */
		public Map<DataField, DataField> getOns() {
			return ons;
		}
	}
	

	/**
	 * Operator available for where clause
	 * @author Yuanchao Ma
	 *
	 */
	public enum Operator {
		EQ(" = "), NEQ(" <> "), GT(" > "), LT(" < "),
		GEQ(" >= "), LEQ(" <= "), LIKE(" LIKE ");
		private String str;
		Operator(String str) {
			this.str = str;
		}
		/**
		 * @return String representation of this operator
		 */
		@Override
		public String toString() {
			return this.str;
		}
		/**
		 * Get a Operator from its String representation
		 * @param s the String representation
		 * @return the Operator constant
		 * @throws IllegalArgumentException when the string cannot be parsed
		 */
		public static Operator parse(String s) throws IllegalArgumentException {
			for (Operator op : Operator.values()) {
				if (s.trim().equals(op.toString().trim()))
					return op;
			}
			throw new IllegalArgumentException("Cannot parse " + s
					+ " to a Operator");
		}
	}
	/**
	 * Orders available for the OrderBy clause
	 * @author myc
	 *
	 */
	public enum Order {
		ASC, DESC;
		private String str;
		Order() {
			this.str = this.name();
		}
		/**
		 * @return String representation of this operator
		 */
		@Override
		public String toString() {
			return this.str;
		}
		/**
		 * Get a Order from its String representation
		 * @param str the String representation
		 * @return the Order constant
		 */
		public static Order parse(String str) {
			return Order.valueOf(str.trim());
		}
	}

	

	
	/**
	 * Select a sub-set of fields from this query and construct a new query
	 * @param fields the DataFields to select
	 * @return A new Query containing fields
	 */
	public Query select(DataField ... fields);
	/**
	 * Filter records of the query 
	 * @param fieldName name of the DataField to be filtered by
	 * @param op the comparison operation type
	 * @param value the value to be compared with 
	 * @return a new filtered Query
	 */
	public Query where(String fieldName, Operator op, Object value);
	/**
	 * Order the query according to a DataField
	 * @param fieldName name of the DataField to order on
	 * @return a new Query obeying the order
	 */
	public Query orderBy(String fieldName, Order order);
	/**
	 * Join two queries to form a new query. Join constraint is the equality of two fields.
	 * @param q2 to second Query for join
	 * @param f1 DataField from this Query to join on
	 * @param f2 DataField from the second query to join on
	 * @return a new joined Query
	 */
	public Query join(Query q2, Map<DataField, DataField> fieldsMap);
	/**
	 * Set the provider used for executing this query and getting data.
	 * @param provider The provider.
	 */
	public void setProvider(DataProvider provider);
	/**
	 * Get the provider used for executing this query and getting data. 
	 */
	public DataProvider getProvider();
	/**
	 * Get the string representation of this query.
	 */
	public String toString();
	/**
	 * Get the inner query of this query. 
	 * @return Return null if there is 
	 * no inner query
	 */
	public Query getInnerQuery();
	
	/**
	 * Get the where clauses contained in this query, if any
	 */
	public List<WhereClause> getWhereClauses();
	/**
	 * Get the orderBy clauses contained in this query, if any
	 */
	public List<OrderClause> getOrderClauses(); 
	/**
	 * Get the groupBy key fields of this query, if any
	 */
	public List<DataField> getGroupByFields();
	
	public JoinOnClause getJoinOnClause();
	
}
