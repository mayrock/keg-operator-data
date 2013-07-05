/**
 * 
 */
package edu.thu.keg.mdap_impl.provider;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import edu.thu.keg.mdap.datafeature.DataView;
import edu.thu.keg.mdap.datamodel.DataContent;
import edu.thu.keg.mdap.datamodel.DataField;
import edu.thu.keg.mdap.datamodel.DataSet;
import edu.thu.keg.mdap.datamodel.GeneralDataField;
import edu.thu.keg.mdap.datamodel.Query;
import edu.thu.keg.mdap.datamodel.DataField.FieldFunctionality;
import edu.thu.keg.mdap.datamodel.DataField.FieldType;
import edu.thu.keg.mdap.datamodel.Query.OrderClause;
import edu.thu.keg.mdap.datamodel.Query.WhereClause;
import edu.thu.keg.mdap.provider.DataProvider;
import edu.thu.keg.mdap.provider.DataProviderException;
import edu.thu.keg.mdap_impl.Config;
import edu.thu.keg.mdap_impl.DataProviderManagerImpl;
import edu.thu.keg.mdap_impl.DataSetManagerImpl;
import edu.thu.keg.mdap_impl.PlatformImpl;
import edu.thu.keg.mdap_impl.datamodel.DataSetImpl;

/**
 * This class is responsible for interacting with Hive
 * 
 * @author Qi Li, Bozhi Yuan
 */

public class HiveProvider extends JdbcProvider {
	private static String tableLocation = "/hiveTable";

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

	/**
	 * absolutely hive don't support nested query so level=0
	 * 
	 * */
	private String getQueryString(Query query, int level) {
		StringBuilder strBuil = new StringBuilder();

		String selectStr = getSelectStr(query);
		String fromStr = getFromStr(query);
		String whereStr = getWhereStr(query);
		String orderbyStr = getOrderByStr(query);
		String groupbyStr = getGroupByStr(query);

		strBuil.append(selectStr);
		strBuil.append(fromStr);

		if (!whereStr.equals("")) {
			strBuil.append(whereStr);

		}
		if (!groupbyStr.equals("")) {
			strBuil.append(groupbyStr);

		}
		if (!orderbyStr.equals("")) {
			strBuil.append(orderbyStr);

		}

		return strBuil.toString();

	}

	@Override
	// ds will be created , argment:data is the resource of data;
	public void writeDataSetContent(DataSet ds, DataContent data)
			throws DataProviderException {

		removeContent(ds);

		// String ddl = getDDL(ds);
		// execute(ddl);
		// 将data里面的字段复制到ds中，复制ds中拥有的所有字段

		if (data instanceof Query) {
			Query q = (Query) data;
			if (q.getProvider() == ds.getProvider()) {
				StringBuilder strBuil = new StringBuilder();

				List<DataField> dataFieldList = ds.getDataFields();

				if (dataFieldList == null || dataFieldList.size() == 0) {
					// TODO

				}

				strBuil.append("create table ");
				strBuil.append(ds.getName());
				strBuil.append(" row format delimited fields terminated by ',' as select ");

				int size = dataFieldList.size();
				String str = null;
				for (int i = 0; i < size - 1; i++) {
					str = dataFieldList.get(i).getName();
					strBuil.append(str);
					strBuil.append(" ");
					strBuil.append(str);
					strBuil.append(",");

				}

				str = dataFieldList.get(size - 1).getName();
				strBuil.append(str);
				strBuil.append(" ");
				strBuil.append(str);

				strBuil.append(" from testF");

				// String sql="create table "+
				// ds.getName()+" row format delimited fields terminated by ',' as select "+strBuil.toString()+" from testF";

				// String insertQueryStr = "INSERT INTO " + ds.getName() + " ( "
				// + strBuil.toString() + " ) SELECT "
				// + strBuil.substring(0, strBuil.length() - 1) + " FROM ( "
				// + q.toString() + " ) as in0";
				execute(strBuil.toString());
			}
		}

	}

	// hive don't support field is null or not in DDL
	// so don't use the key word null when create table in hive
	// actually , hive yet don't support varchar ...etc ,
	// About String type,hive only have the type "string"
	private String getDDL(DataField df) {
		FieldType type = df.getFieldType();
		String typeStr = "";
		switch (type) {
		case ShortString:
			typeStr = " STRING ";
			break;
		case LongString:
			typeStr = " STRING ";
			break;
		case Text:
			typeStr = " STRING ";
			break;
		case Double:
			typeStr = " FLOAT ";
			break;
		case Int:
			typeStr = " INT ";
			break;
		case DateTime:
			typeStr = " TIMESTAMP ";
			break;
		}

		return df.getName() + typeStr;
	}

	// generate create table DDL
	// create table ........as select
	// the command will create a inner table in /user/hive/warehouse
	private String getDDL(DataSet ds) {
		StringBuilder sb = new StringBuilder("create table ");
		sb.append(ds.getName());
		sb.append(" ( ");
		List<DataField> fields = ds.getDataFields();
		for (int i = 0; i < fields.size() - 1; i++) {
			sb.append(getDDL(fields.get(i))).append(",");
		}
		sb.append(getDDL(fields.get(fields.size() - 1))).append(" ) ");

		sb.append(" row format delimited fields terminated by ',' ");

		System.out.println(sb.toString());

		return sb.toString();
	}

	private String getGroupByStr(Query query) {

		List<DataField> list_group = query.getGroupByFields();
		if (list_group == null || list_group.size() == 0) {
			return "";

		}

		StringBuilder strBuil = new StringBuilder();

		strBuil.append(" GROUP BY ");
		int size = list_group.size();

		for (int i = 0; i < size - 1; i++) {
			strBuil.append(list_group.get(i).getName());
			strBuil.append(",");
		}

		strBuil.append(list_group.get(list_group.size() - 1).getName() + " ");
		return strBuil.toString();

	}

	private String getOrderByStr(Query query) {
		// the key "order by"

		List<OrderClause> orders = query.getOrderClauses();
		if (orders == null || orders.size() == 0) {
			System.out.println("ok");
			return "";

		}

		StringBuilder strBuil = new StringBuilder();

		strBuil.append(" ORDER BY ");
		OrderClause order = null;

		for (int i = 0; i < orders.size() - 1; i++) {
			order = orders.get(i);

			strBuil.append(order.getField().getName());
			strBuil.append(" ");
			strBuil.append(order.getOrder().toString());
			strBuil.append(", ");
		}

		order = orders.get(orders.size() - 1);

		strBuil.append(order.getField().getName());
		strBuil.append(" ");
		strBuil.append(order.getOrder().toString());
		strBuil.append(" ");

		return strBuil.toString();

	}

	private String getWhereStr(Query query) {
		StringBuilder strBuil = new StringBuilder();
		// add key "where"
		List<WhereClause> list_where = query.getWhereClauses();
		if (list_where == null || list_where.size() == 0) {

			return "";
		}

		int size = list_where.size();
		strBuil.append(" where ");

		for (int i = 0; i < size; i++) {
			strBuil.append(whereConditionToStr(list_where.get(i)));
			if (i != size - 1) {
				strBuil.append(" and ");

			}
		}

		strBuil.append(" ");
		return strBuil.toString();

	}

	private String getFromStr(Query query) {
		// add key from
		StringBuilder strBuil = new StringBuilder();
		DataField[] fields = query.getFields();
		strBuil.append(" from ");
		strBuil.append(fields[0].getDataSet().getName() + " ");

		return strBuil.toString();
	}

	private String getSelectStr(Query query) {
		// add key select
		StringBuilder strBuil = new StringBuilder();
		strBuil.append("SELECT ");
		DataField[] fields = query.getFields();

		if (fields == null || fields.length == 0) {

			return null;
		}

		int len = fields.length;
		for (int i = 0; i < len; i++) {
			strBuil.append(fields[i].getQueryName());

			if (i != len - 1) {
				strBuil.append(",");

			}
		}
		strBuil.append(" ");
		return strBuil.toString();
	}

	private String whereConditionToStr(WhereClause where) {
		StringBuilder sb = new StringBuilder();
		sb.append(where.getField().getName()).append(
				where.getOperator().toString());
		if (where.getField().getFieldType().isNumber())
			sb.append(where.getValue().toString());
		else
			sb.append("'").append(where.getValue().toString()).append("'");
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		PlatformImpl p = new PlatformImpl("config.xml");
		Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
		// Config.init("./config.xml");

		DataProvider hiveProvider = DataProviderManagerImpl.getInstance()
				.getDefaultHiveProvider("default", null, null);

		DataField[] fields = null;
		DataSet dsSite = null;

		fields = new DataField[3];
		fields[0] = new GeneralDataField("EN_NAME", FieldType.ShortString, "",
				true, FieldFunctionality.Identifier);
		fields[1] = new GeneralDataField("LAC", FieldType.Int, "", false,
				FieldFunctionality.Value);
		fields[2] = new GeneralDataField("CI", FieldType.Int, "", false,
				FieldFunctionality.Value);

		dsSite = DataSetManagerImpl.getInstance().createDataSet("TESTF",
				"liqi", "小区地理位置信息", hiveProvider, true, fields);
		DataSetManagerImpl.getInstance().setDataSetPermission("TESTF", "liqi",
				DataSetImpl.PERMISSION_PUBLIC, null);

		Query q1;
		DataSet ds = DataSetManagerImpl.getInstance().getDataSet("TESTF");
		q1 = ds.getQuery();

		q1 = q1.orderBy("LAC", Query.Order.ASC);
		q1 = q1.orderBy("EN_NAME", Query.Order.ASC);
		q1 = q1.where("LAC", Query.Operator.GEQ, 5000);

		q1.open();
		int i = 0;
		while (q1.next() && i++ < 3) {
			System.out.println(q1.getValue(ds.getField("LAC")) + " "
					+ q1.getValue(ds.getField("CI")));
		}
		q1.close();
	}

	@Override
	public Object getValue(Query q, DataField field)
			throws DataProviderException {
		try {
			ResultSet rs = results.get(q);
			FieldType type = field.getFieldType();
			String fieldName = field.getName().toLowerCase();
			switch (type) {
			case ShortString:
			case LongString:
			case Text:
				return rs.getString(fieldName);
			case Double:
				return rs.getDouble(fieldName);
			case Int:
				return rs.getInt(fieldName);
			case DateTime:
				return rs.getDate(fieldName);
			}
		} catch (SQLException e) {
			throw new DataProviderException(e.getMessage());
		}
		throw new IllegalArgumentException(
				"Type of this field is not supported");
	}

}
