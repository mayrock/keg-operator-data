package edu.thu.keg.mdap_impl.provider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import edu.thu.keg.mdap.provider.AbsSqlServerProvider;
import edu.thu.keg.mdap.provider.IDbSqlServerProvider;
import edu.thu.keg.mdap.provider.DatabaseProviderException;
import edu.thu.keg.mdap.provider.IllegalQueryException;

public class DbSqlServerProviderImpl extends AbsSqlServerProvider {
	private static DbSqlServerProviderImpl instance;

	public static DbSqlServerProviderImpl getInstance() {
		// TODO multi-thread
		if (instance == null)
			instance = new DbSqlServerProviderImpl();
		return instance;
	}

	private Connection conn;
	private String connString;

	private DbSqlServerProviderImpl() {

		this.setDefaultSQLProvider();
	}

	private void setDefaultSQLProvider() {
		String address = ResourceBundle.getBundle("database").getString(
				"SqlAddress");
		String databaseName = ResourceBundle.getBundle("database").getString(
				"databaseName");
		String connString = "jdbc:" + address + ";databaseName=" + databaseName
				+ ";integratedSecurity=true;";
		this.connString = connString;
	}

	@Override
	public String getConnectionString() {
		return connString;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (this.conn == null || this.conn.isClosed())
			this.conn = DriverManager.getConnection(connString);
		return this.conn;
	}

	@Override
	public ResultSet executeQuery(String query) throws IllegalQueryException {
		try {
			Statement stmt = getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (Exception ex) {
			throw new IllegalQueryException(ex.getMessage());
		}
	}

	@Override
	public void execute(String text) throws DatabaseProviderException {
		Statement stmt;
		try {
			stmt = this.getConnection().createStatement();
			stmt.executeUpdate(text);
			System.out.println("Excute:" + text);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void closeConnection() throws SQLException {
		this.conn.close();

	}

	@Override
	public String SelectFromWhere(String selectwhat, String fromwhat,
			String wherewhat) {
		return "select " + selectwhat + " from [" + fromwhat + "] where "
				+ wherewhat;
	}

	@Override
	public String SelectFrom(String selectwhat, String fromwhat) {
		return "select " + selectwhat + " from [" + fromwhat + "]";
	}

}
