package edu.thu.keg.mdap;

import java.util.Collection;

import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datasetfeature.DataSetFeature;
import edu.thu.keg.mdap.provider.DataProvider;

/**
 * This object provides information
 * about all datasets stored in the platform,
 * including all suppliers. Metadata stored in 
 * all data providers are found here.
 * @author Yuanchao Ma
 *
 */
public interface DataSetManager {
	/**
	 * Get a dataset stored in the platform and return a reference.
	 * It can be stored in all registered providers.
	 * @param name the name of the requested dataset
	 * @return a reference of DataSet
	 */
	public DataSet getDataSet(String name);
	/**
	 * Get all datasets stored in the platform
	 * @return a array containing all the dataset references
	 */
	public Collection<DataSet> getDataSetList();
	/**
	 * Store(register) metadata of the DataSet into the platform
	 * @param ds The DataSet object to be stored.
	 */
	public void storeDataSet(DataSet ds);
	/**
	 * Create a new DataSet instance, using the given name, connection string
	 * and data field set.
	 * @param name The unique name of the DataSet
	 * @param provider its DataProvider
	 * @param fields DataField set
	 * @return A new DataSet instance
	 */
	public DataSet createDataSet(String name, String description, DataProvider provider, DataField[] fields, boolean loadable);
	/**
	 * Remove a DataSet from the platform, as well as remove 
	 * its actual data from its provider.
	 * No operation is performed if the DataSet does not exist.
	 * @param ds The DataSet to be removed
	 */
	public void removeDataSet(DataSet ds);
	
	/**
	 * Get all datasets that supports the certain type of feature
	 * @param feature A Class instance that represents the demanded feature type
	 * @return A collection of DataSet's
	 */
	public Collection<DataSet> getDataSetList(Class<? extends DataSetFeature> feature);
}
