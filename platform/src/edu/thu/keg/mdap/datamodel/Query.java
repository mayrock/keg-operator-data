/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.List;

import edu.thu.keg.mdap.provider.DataProvider;

/**
 * Used for getting data from a data source. It is the unified interface 
 * for querying data. 
 * @author Yuanchao Ma
 *
 */
public interface Query extends DataContent {
	
	class WhereClause {
		DataField field;
		Operator operator;
		Object value;
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

	
	public enum Operator {
		EQ(" = "), NEQ(" <> "), GT(" > "), LT(" < "),
		GEQ(" >= "), LEQ(" <= "), LIKE(" LIKE ");
		private String str;
		Operator(String str) {
			this.str = str;
		}
		public String getStr() {
			return this.str;
		}
	}
	
	public Query select(DataField[] fields);
	public Query where(DataField field, Operator op, Object value);
	public Query join(Query q2, DataField f1, DataField f2);
	/**
	 * Set the provider used for executing this query and getting data.
	 * @param provider The provider.
	 */
	public void setProvider(DataProvider provider);
	/**
	 * @return The provider used for executing this query and getting data. 
	 */
	public DataProvider getProvider();
	/**
	 *
	 * @return String representation of this query.
	 */
	public String toString();
	
	public DataField[] getDataFields();
	
	public List<WhereClause> getWhereClauses();
}
