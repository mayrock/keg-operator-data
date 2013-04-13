/**
 * 
 */
package edu.thu.keg.mdap.datamodel;

import java.util.HashMap;
import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * Contains all metadata describing a dataset, stored in a data provider.
 * @author Yuanchao Ma
 *
 */
public interface DataSet {
	/**
	 * Get the name (unique identifier) of the dataset,
	 * like a table name in SQL or HIVE.
	 * @return the unique name. 
	 */
	public String getName();
	/**
	 * Return a connected ResultSet
	 *  containing the records of this dataset, if it is loadable.
	 * @return a JDBC ResultSet. The ResultSet is generated when the 
	 * method is called for the first time. 
	 * @throws OperationNotSupportedException when dataset is not loadable
	 * @throws DataProviderException when actual data of the DataSet does not
	 * exist on the DataProvider
	 */
	public Query getQuery() throws OperationNotSupportedException, DataProviderException;
	
	/**
	 * Get the provider of this DataSet
	 * @return a DataProvider instance
	 */
	public DataProvider getProvider();
	/**
	 * Whether this dataset is small enough to be loaded into memory
	 * @return a boolean indicates whether this dataset is small enough to be loaded into memory
	 */
	public boolean isLoadable();
	/**
	 * Get description of this dataset.
	 * @return Human-friendly description.
	 */
	public String getDescription();
	/**
	 * Get an array of the fields contained in the dataset
	 * @return an array containing all the fields in the schema
	 */
	public DataField[] getDataFields();
	/**
	 * @return a map containing all features of this dataset
	 */
	public HashMap<Class<? extends DataSetFeature>, DataSetFeature> getFeatures();
	/**
	 * Add information about a new feature supported by this dataset
	 * @param feature describes the to-be-added feature
	 */
	public void addFeature(DataSetFeature feature);
}
