package edu.thu.keg.mdap;

import edu.thu.keg.mdap.dataset.DataSet;

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
	public DataSet[] getDataSetList();
	/**
	 * Store(register) metadata of the DataSet into the platform
	 * @param ds The DataSet object to be stored.
	 */
	public void storeDataSet(DataSet ds);
}
