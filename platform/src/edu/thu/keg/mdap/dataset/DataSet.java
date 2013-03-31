/**
 * 
 */
package edu.thu.keg.mdap.dataset;

import java.sql.ResultSet;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafield.DataField;
import edu.thu.keg.mdap.provider.IllegalQueryException;

/**
 * @author myc
 *
 */
public interface DataSet {
	/**
	 * Get the name (unique identifier) of the dataset,
	 * like a table name in SQL or HIVE.
	 * @return the unique name. Default implementation
	 * is returning the class name. Subclass can override this.
	 */
	public String getName();
	/**
	 * get query statement for querying data
	 * in the dataset. The statement would be 
	 * executed against the data provider. 
	 * @return A string representing the query statement
	 */
	public String getQueryStatement();
	/**
	 * Return a disconnected in-memroy JDBC ResultSet
	 *  containing the records of this dataset, if it is loadable
	 * The default implementation uses HIVE.
	 * @return a JDBC ResultSet. The ResultSet is generated when the 
	 * method is called for the first time. Return null if no data 
	 * exists in the data provider
	 * @throws OperationNotSupportedException when dataset is not loadable
	 * @throws IllegalQueryException when actual data of the DataSet does not
	 * exist on the DataProvider
	 */
	public ResultSet getResultSet() throws OperationNotSupportedException, IllegalQueryException;
	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * @return a boolean indicates whether this dataset is small enough to be loaded into memory
	 */
	public boolean isLoadable();
	/**
	 * Get an array of the fields contained in the dataset
	 * @return an array containing all the fields in the schema
	 */
	public DataField[] getDataFields();
}
