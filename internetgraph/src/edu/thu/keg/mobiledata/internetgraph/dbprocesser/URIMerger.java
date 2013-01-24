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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import edu.thu.keg.mobiledata.dataloader.DataLoader;
import edu.thu.keg.mobiledata.internetgraph.dbo.UriInfo;

/**
 * @author myc
 *
 */
public class URIMerger {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] t= new String[5];
		t[0] = "sss.5.qq.com.cn";
		t[1] = "1.sss.5.qq.com";
		t[2] = "keg.cs.tsinghua.edu.cn";
		t[3] = "a108.photo.store.qq.com";
		t[4] = "111.444.876.98";
		for (String s : t) {
			System.out.println(processUri(s, 2));
		}
		Connection conn = null;
		try {
			conn = DataLoader.getBeijingConn();
			generateMap(conn);
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void generateMap(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT URI FROM temp_URI3");
		System.out.println("Processing..." + (new Date()).toString());
		PreparedStatement ps = conn.prepareStatement("INSERT INTO [URI_Mapping]" 
		           + "([OriginalURI],[Domain3],[Domain2])"
		           + "VALUES (?,?,?)");
		while (rs.next()) {
			String orig = rs.getString("URI");
			if (orig == null)
				continue;
			String uri = processUri(orig, 3);
			String domain = processUri(orig, 2);
			if (uri.equals(""))
				continue;
			ps.setString(1, orig);
			ps.setString(2, uri);
			ps.setString(3, domain);
			ps.addBatch();
		}
		System.out.println("Writing..."+ (new Date()).toString());
		
		ps.executeBatch();
		System.out.println("Done"+ (new Date()).toString());
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
			String uri = processUri(orig, 2);
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
		ret.add("org");
		ret.add("gov");
		ret.add("ac");
		ret.add("co");
		ret.add("or");
		ret.add("go");
		ret.add("ne");
		return ret;
	}
	public static String processUri(String original) {
		return processUri(original, 2);
	}
	public static String processUri(String original, int level) {
		original = original.trim();
		if (original.matches("[\\.\\d:]+")) {
			return "";
		}
		if (original.contains("?") || original.contains("%")
				|| original.contains("(") || original.contains("<"))
			return "";
		String[] arr = original.split("\\.");
		int totalLength = arr.length;
		if (totalLength <= 1)
			return "";
		if (totalLength <= level)
			return original;
		int maxLength = level;
		String level1 = arr[arr.length - 2];
		if (getCommonLevel1Subfix().contains(level1)) {
			maxLength++;
		}
		StringBuilder sb = new StringBuilder(arr[totalLength - maxLength]);
		for (int i = totalLength - maxLength + 1;
				i < totalLength; i++) {
			sb.append("." + arr[i]);
		}
		return sb.toString().trim();
	}

}
