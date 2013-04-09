/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import edu.thu.keg.mdap.provider.AbstractDataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.provider.IllegalQueryException;

/**
 * @author Yuanchao Ma
 *
 */
public class JdbcProvider extends AbstractDataProvider {
	
	private Connection conn;
	private String connString;
	private HashMap<String, ResultSet> results;

	public JdbcProvider(String connString) {
		this.connString = connString;
		results = new HashMap<String, ResultSet>();
	}
	
	private Connection getConnection() throws SQLException {
		if (this.conn == null || this.conn.isClosed())
			this.conn = DriverManager.getConnection(connString);
		return this.conn;
	}
	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.provider.DataProvider#executeQuery(java.lang.String)
	 */
	@Override
	public ResultSet executeQuery(String query) throws IllegalQueryException {
		if (results.containsKey(query))
			return (ResultSet)results.get(query);
		else {
			try {
				Statement stmt = getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(query);
				results.put(query, rs);
				return rs;
			} catch (Exception ex) {
				throw new IllegalQueryException();
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.thu.keg.mdap.provider.DataProvider#closeResultSet(java.sql.ResultSet)
	 */
	@Override
	public void closeResultSet(ResultSet rs) throws DataProviderException {
		try {
			rs.getStatement().close();
			
			boolean flag = true;
			for (ResultSet r : results.values()) {
				if (!r.isClosed()) {
					flag = false;
					break;
				}
			}
			if (flag)
				conn.close();
		} catch (SQLException e) {
			throw new DataProviderException(e.getMessage());
		}
	}

	@Override
	public void removeDataSet(String dsName) {
		
	}

	@Override
	public void execute(String text) throws DataProviderException {
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(text);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
