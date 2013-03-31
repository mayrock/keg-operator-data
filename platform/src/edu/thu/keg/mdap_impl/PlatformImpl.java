/**
 * 
 */
package edu.thu.keg.mdap_impl;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.DataSetManager;
import edu.thu.keg.mdap.Platform;

/**
 * @author Yuanchao Ma
 *
 */
public class PlatformImpl implements Platform {

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.Platform#getDataSetManager()
	 */
	@Override
	public DataSetManager getDataSetManager() {
		return new DataSetManagerImpl();
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.Platform#getDataProviderManager()
	 */
	@Override
	public DataProviderManager getDataProviderManager() {
		return new DataProviderManagerImpl();
	}

}
