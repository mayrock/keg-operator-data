/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import edu.thu.keg.mdap.provider.DataProvider;

/**
 * Used for getting data from a data source. It is the unified interface 
 * for querying data. 
 * @author Yuanchao Ma
 *
 */
public interface Query extends DataContent {
	
	public static final int EQ = 0;
	public static final int NEQ = 1;
	public static final int GT = 2;
	public static final int LT = 3;
	public static final int GEQ = 4;
	public static final int LEQ = 5;
	public static final int LIKE = 6;
	
//	public Query select(DataField[] fields);
//	public Query where(DataField[] field, int op, Object value);
//	public Query join(Query q2, DataField f1, DataField f2);
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
	 * String representation of this query.
	 * @return
	 */
	public String getQueryString();
}
