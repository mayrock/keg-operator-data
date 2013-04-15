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

import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.provider.AbstractDataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap.provider.IllegalQueryException;

/**
 * @author Yuanchao Ma
 *
 */
public class JdbcProvider extends AbstractDataProvider {
	
	private Connection conn;
	private HashMap<Query, ResultSet> results;

	public JdbcProvider(String connString) {
		super(connString);
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
			throw new IllegalQueryException(ex.getMessage());
		}
	}

	@Override
	public void removeContent(DataSet ds) throws DataProviderException {
		execute(getDrop(ds));
	}

	private String getDrop(DataSet ds) {
		return "DROP TABLE " + ds.getName();
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
	public void writeDataSetContent(DataSet ds, DataContent data) throws DataProviderException {
		
		removeContent(ds);
		
		String ddl = getDDL(ds);
		execute(ddl);
		
		if (data instanceof Query) {
			Query q = (Query)data;
			if (q.getProvider() == ds.getProvider()) {
				StringBuilder sb = new StringBuilder();
				DataField[] fields = ds.getDataFields();
				for (int i = 0; i < fields.length - 1; i++) {
					DataField df = fields[i];
					sb.append(df.getColumnName()).append(",");
				}
				sb.append(fields[fields.length - 1].getColumnName());
				
				String insertQueryStr = "INSERT INTO " + ds.getName() + " ( "
						+ sb.toString() + " ) SELECT " + sb.toString()
						+ " FROM ( " + q.getQueryString() + " ) as t0";
				execute(insertQueryStr);
			}
		}
	}
	
	private String getDDL(DataSet ds) {
		StringBuilder sb = new StringBuilder("CREATE TABLE ");
		sb.append(ds.getName());
		sb.append(" ( ");
		DataField[] fields = ds.getDataFields();
		for (int i = 0; i < fields.length - 1; i++) {
			sb.append(getDDL(fields[i])).append(",");
		}
		sb.append(getDDL(fields[fields.length - 1])).append(" ) ");
		return sb.toString();
	}
	
	private String getDDL(DataField field) {
		FieldType type = field.getFieldType();
		String typeStr = "";
		switch (type) {
		case ShortString:
			typeStr = " NVARCHAR(50) ";
			break;
		case LongString:
			typeStr = " NVARCHAR(200) ";
			break;
		case Text:
			typeStr = " NVARCHAR(999) ";
			break;
		case Double:
			typeStr = " FLOAT ";
			break;
		case Int:
			typeStr = " INTEGER ";
			break;
		case DateTime:
			typeStr = " DATETIME ";
			break;
		}
		String nullStr = "";
		if (field.allowNull())
			nullStr = " NULL ";
		else
			nullStr = " NOT NULL ";
		return field.getColumnName() + typeStr + nullStr;
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
			FieldType type = field.getFieldType();
			switch (type) {
			case ShortString:
			case LongString:
			case Text:
				return rs.getString(field.getColumnName());
			case Double:
				return rs.getDouble(field.getColumnName());
			case Int:
				return rs.getInt(field.getColumnName());
			case DateTime:
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
