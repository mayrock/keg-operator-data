/**
 * 
 */
package edu.thu.keg.mdap_impl;

import java.util.HashMap;

import edu.thu.keg.mdap.DataProviderManager;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap_impl.provider.JdbcProvider;

/**
 * @author Yuanchao Ma, Bozhi Yuan
 * 
 */
public class DataProviderManagerImpl implements DataProviderManager {

	private static DataProviderManagerImpl instance = null;

	public synchronized static DataProviderManagerImpl getInstance() {
		// TODO multi-thread
		if (instance == null)
			instance = new DataProviderManagerImpl();
		return instance;
	}

	private HashMap<String, DataProvider> providers;

	private DataProviderManagerImpl() {
		this.providers = new HashMap<String, DataProvider>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.thu.keg.mdap.DataProviderManager#getProvider(java.lang.String)
	 */
	@Override
	public DataProvider getProvider(String connString) {
		if (providers.containsKey(connString)) {
			return providers.get(connString);
		} else {
			DataProvider p = new JdbcProvider(connString);
			providers.put(connString, p);
			return p;
		}
	}

	@Override
	public DataProvider getDefaultSQLProvider(String dbName) {
		String address = Config.getProperty(Config.SqlAddress);
		String conn = "jdbc:" + address + ";databaseName=" + dbName
				+ ";integratedSecurity=true;";
		return getProvider(conn);
	}

	@Override
	public DataProvider getDefaultOracleProvider(String dbName,
			String userName, String password) {
		String address = Config.getProperty(Config.OracleAddress);
		String conn = "jdbc:oracle:thin:" + userName + "/" + password + "@"
				+ address + ":" + dbName;
		return getProvider(conn);
	}

	@Override
	public DataProvider getDefaultHiveProvider() {
		// TODO Auto-generated method stub
		return null;
	}

}
