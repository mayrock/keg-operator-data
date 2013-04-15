/**
 * 
 */
package edu.thu.keg.mdap;

/**
 * The main entrance of the whole platform.
 * 
 * @author Yuanchao Ma
 *
 */
public interface Platform {
	/**
	 * Get the DataSetManager instance for manipulating DataSets 
	 * in the platform
	 * @return a unique DataSetManager instance.
	 */
	public DataSetManager getDataSetManager();
	/**
	 * Get the DataProviderManager instance for getting 
	 * DataProviders in the platform
	 * @return a unique DataProviderManager instance.
	 */
	public DataProviderManager getDataProviderManager();

}
