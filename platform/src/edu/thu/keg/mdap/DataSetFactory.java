package edu.thu.keg.mdap;

import edu.thu.keg.mdap.dataset.DataSet;

/**
 * This object provides information
 * about all datasets stored in the platform,
 * including all suppliers. Metadata stored in 
 * all data providers are found here.
 * This should be the main entrance of the framework
 * @author Yuanchao Ma
 *
 */
public interface DataSetFactory {
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
	
}
