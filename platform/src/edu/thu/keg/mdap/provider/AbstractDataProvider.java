/**
 * 
 */
package edu.thu.keg.mdap.provider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

/**
 * A general implementation of the interface DataProvider, with some abstract
 * methods implemented.
 * 
 * @author Yuanchao Ma
 */
public abstract class AbstractDataProvider implements DataProvider {

	protected String connString;
	protected String userName;
	protected String password;
	protected HashMap<ResultSet, Connection> rsMapConn;

	@Override
	public String getConnectionString() {
		return connString;
	}

	public AbstractDataProvider(String connString) {
		this.rsMapConn = new HashMap<ResultSet, Connection>();
		this.connString = connString;
	}

	public AbstractDataProvider(String connString, String username,
			String password) {
		this.rsMapConn = new HashMap<ResultSet, Connection>();
		this.connString = connString;
		this.userName = username;
		this.password = password;
	}

	@Override
	public String toString() {
		return this.getConnectionString();
	}

	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}

	
}
