/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import edu.thu.keg.mdap.provider.DataProvider;

/**
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
	
	public void setProvider(DataProvider provider);
	public DataProvider getProvider();
	public String getQueryString();
}
