/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * @author myc
 *
 */
public interface TableStructure {
	/**
	 * Get the name (unique identifier) of the dataset,
	 * like a table name in SQL or HIVE.
	 * @return the unique name. 
	 */
	public String getName();
	/**
	 * Return a Query for querying actual data of this dataset, if it is loadable.
	 * @return a Query against the provider of this dataset. 
	 * @throws OperationNotSupportedException when dataset is not loadable
	 * @throws DataProviderException when actual data of the DataSet does not
	 * exist on the DataProvider
	 */
	public Query getQuery() throws OperationNotSupportedException, DataProviderException;
	/**
	 * Get description of this dataset.
	 * @return Human-friendly description.
	 */
	public String getDescription();
}
