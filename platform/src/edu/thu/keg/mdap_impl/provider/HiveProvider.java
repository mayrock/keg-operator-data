/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.provider.DataProviderException;

/**
 * This class is responsible for interacting with Hive
 * 
 * @author Yuanchao Ma
 */
public class HiveProvider extends JdbcProvider {

	public HiveProvider(String connString) throws SQLException {
		super(connString);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.thu.keg.mdap_impl.provider.JdbcProvider#getQueryString(edu.thu.keg
	 * .mdap.datamodel.Query)
	 */
	@Override
	public String getQueryString(Query q) {
		return this.getQueryString(q, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.thu.keg.mdap_impl.provider.JdbcProvider#openQuery(edu.thu.keg.mdap
	 * .datamodel.Query)
	 */
	@Override
	public void openQuery(Query query) throws DataProviderException {
		if (!super.results.containsKey(query)) {
			String sql = getQueryString(query, 0);

			ResultSet rs = super.executeQuery(sql);
			super.results.put(query, rs);
		}
	}

	private String getQueryString(Query q, int level) {
		return null;
	}

}
