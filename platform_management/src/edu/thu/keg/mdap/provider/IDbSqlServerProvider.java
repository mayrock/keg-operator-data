package edu.thu.keg.mdap.provider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDbSqlServerProvider {

	public Connection getConnection() throws SQLException;

	public String getConnectionString();

	public ResultSet executeQuery(String query) throws IllegalQueryException;

	public void execute(String text) throws DatabaseProviderException;

	public void closeConnection() throws SQLException;

}
