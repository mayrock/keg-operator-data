/**
 * 
 */
package edu.thu.keg.mdap_impl;


import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap_impl.provider.JdbcProvider;

/**
 * @author Yuanchao Ma
 *
 */
public class DataProviderManagerImpl implements DataProviderManager {

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.DataProviderManager#getProvider(java.lang.String)
	 */
	@Override
	public DataProvider getProvider(String connString){
		return new JdbcProvider(connString);
	}

}
