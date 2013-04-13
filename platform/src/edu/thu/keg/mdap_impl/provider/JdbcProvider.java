/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
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
	private HashMap<Query, ResultSet> results;

	public JdbcProvider(String connString) {
		this.connString = connString;
		results = new HashMap<Query, ResultSet>();
	}
	
	private Connection getConnection() throws SQLException {
		if (this.conn == null || this.conn.isClosed())
			this.conn = DriverManager.getConnection(connString);
		return this.conn;
	}

	private ResultSet executeQuery(String query) throws IllegalQueryException {
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (Exception ex) {
			throw new IllegalQueryException();
		}
	}

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
	public void removeDataSet(DataSet ds) {
		//TODO
	}


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

	@Override
	public void writeDataSetContent(DataSet ds, DataContent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openQuery(Query query) throws DataProviderException {
		if (!results.containsKey(query)) {
			ResultSet rs = executeQuery(query.getQueryString());
			results.put(query, rs);
		}
	}

	@Override
	public boolean next(Query q) throws DataProviderException {
		try {
			return results.get(q).next();
		} catch (SQLException e) {
			throw new DataProviderException(e.getMessage());
		}
	}

	@Override
	public Object getValue(Query q, DataField field)
			throws DataProviderException {
		try {
			ResultSet rs = results.get(q);
			if (field.getDataType() == String.class) {
				return rs.getString(field.getColumnName());
			} else if (field.getDataType() == Double.class) {
				return rs.getDouble(field.getColumnName());
			} else if (field.getDataType() == Integer.class) {
				return rs.getInt(field.getColumnName());
			} else if (field.getDataType() == Date.class) {
				return rs.getDate(field.getColumnName());
			}
		} catch (SQLException e) {
			throw new DataProviderException(e.getMessage());
		}
		throw new IllegalArgumentException("Type of this field is not supported");
	}

	@Override
	public void closeQuery(Query q) throws DataProviderException {
		try {
			results.get(q).close();
			results.remove(q);
			if (results.size() == 0)
				getConnection().close();
		} catch (SQLException e) {
			throw new DataProviderException();
		}
	}

}
