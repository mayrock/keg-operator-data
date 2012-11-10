/**
 * 
 */
package edu.thu.keg.mobiledata.internetgraph.dbprocesser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import edu.thu.keg.mobiledata.internetgraph.dbo.UriInfo;

/**
 * @author myc
 *
 */
public class URIMerger {
	private static final int MAX_LENGTH = 4;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] t= new String[4];
		t[0] = "sss.5.qq.com.cn";
		t[1] = "1.sss.5.qq.com";
		t[2] = "keg.cs.tsinghua.edu.cn";
		t[3] = "a108.photo.store.qq.com";
		for (String s : t) {
			System.out.println(processUri(s));
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection
					("jdbc:sqlserver://localhost:1433;databaseName=ZhuData;integratedSecurity=true;");
			mergeURI(conn);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public static void mergeURI (Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM URI_New");
		HashMap<String, UriInfo> map = new HashMap<String, UriInfo>(100000);
		System.out.println("Processing...");
		while (rs.next()) {
			String orig = rs.getString("URI");
			int nTotal = rs.getInt("TotalCount");
			int nUser = rs.getInt("UserCount");
			int nLocation = rs.getInt("LocationCount");
			String uri = processUri(orig);
			if (!map.containsKey(uri)) {
				map.put(uri, 
						new UriInfo(uri, nTotal, nUser, nLocation));
			} else {
				UriInfo info = map.get(uri);
				info.addRecord(nTotal, nUser, nLocation);
			}
		}
		System.out.println("Writing...");
		PreparedStatement ps = conn.prepareStatement("INSERT INTO [ZhuData].[dbo].[URI_Merged]" 
           + "([URI],[TotalCount],[UserCount],[LocationCount])"
           + "VALUES (?,?,?,?)");
		for (UriInfo info : map.values()) {
			ps.setString(1, info.getURI());
			ps.setInt(2, info.getTotalCount());
			ps.setInt(3, info.getUserCount());
			ps.setInt(4, info.getLocationCount());
			ps.addBatch();
		}
		ps.executeBatch();
		System.out.println("Done");
		
	}
	public static HashSet<String> getCommonLevel1Subfix() {
		HashSet<String> ret = new HashSet<String>();
		ret.add("edu");
		ret.add("com");
		ret.add("net");
		return ret;
	}
	public static String processUri(String original) {
		String[] arr = original.split("\\.");
		int totalLength = arr.length;
		if (totalLength <= MAX_LENGTH)
			return original;
		int maxLength = MAX_LENGTH;
		String level1 = arr[arr.length - 2];
		if (getCommonLevel1Subfix().contains(level1)) {
			maxLength++;
		}
		StringBuilder sb = new StringBuilder(arr[totalLength - maxLength]);
		for (int i = totalLength - maxLength + 1;
				i < totalLength; i++) {
			sb.append("." + arr[i]);
		}
		return sb.toString();
	}

}
