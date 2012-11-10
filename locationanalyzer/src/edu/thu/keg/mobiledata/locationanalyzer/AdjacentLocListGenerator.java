/**
 * 
 */
package edu.thu.keg.mobiledata.locationanalyzer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Generate a list, each record being a adjacent location pair 
 * which appears consecutively in the location trace of a user.
 * @author myc
 *
 */
public class AdjacentLocListGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdjacentLocListGenerator generator = new AdjacentLocListGenerator();
		Connection conn;
		try {
			conn = DriverManager.getConnection
					("jdbc:sqlserver://localhost:1433;databaseName=ZhuData;" +
							"integratedSecurity=true;");
			AdjacentLocList list = generator.getListFromDB(conn);
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO AdjacentLocation" +
					"(LocName1, LocName2, Hour, UserCount, TotalCount)" +
					"VALUES (?, ?, ?, ?, ?)");
			for (CellLocation loc : list.getCells().values()) {
				for (AdjacentLocPair pair: loc.getNextCells()) {
					String cellName1 = pair.getCell1().getCellName();
					String cellName2 = pair.getCell2().getCellName();
					int[] uc = pair.getUsersPerHourCount();
					int[] tc = pair.getTotalPerHourCount();
					for (int i = 0; i < 23; i++) {
						stmt.setString(1, cellName1);
						stmt.setString(2, cellName2);
						stmt.setInt(3, i);
						stmt.setInt(4, uc[i]);
						stmt.setInt(5, tc[i]);
						stmt.addBatch();
					}
				}
				stmt.executeBatch();
				conn.commit();
				System.out.println(loc.getCellName() + " succeed.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public class LocationRecord {
		private String imsi;
		private String cellName;
		private int hour;
		public String getImsi() {
			return imsi;
		}
		public String getCellName() {
			return cellName;
		}
		public int getHour() {
			return hour;
		}
		public void init(String imsi, String cellName, int hour) {
			this.imsi = imsi;
			this.cellName = cellName;
			this.hour = hour;
		}
		public LocationRecord(String imsi, String cellName, int hour) {
			super();
			init(imsi, cellName, hour);
		}
		public LocationRecord(ResultSet rs) throws SQLException {
			String imsi = rs.getString("IMSI");
			String cellName = rs.getInt("LAC") + "_" + rs.getInt("CI");
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getTime("ConnectTime"));
			int hour = c.get(Calendar.HOUR_OF_DAY);
			init(imsi, cellName, hour);
		}
	}
	public AdjacentLocList getListFromDB(Connection conn) throws SQLException {
		AdjacentLocList list = new AdjacentLocList();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT IMSI,LAC,CI,ConnectTime FROM GN" 
				+"  where imsi is not null and connecttime > '2012-01-01'"
		        + " ORDER BY IMSI ASC, ConnectTime ASC");
		rs.next();
		LocationRecord record = new LocationRecord(rs);
		LocationRecord nextRecord;
		HashMap<AdjacentLocPair, int[]> pairs = new HashMap<AdjacentLocPair, int[]>();
		long count = 0;
		while (rs.next()) {
			nextRecord = new LocationRecord(rs);
			if (nextRecord.getCellName().equals(record.getCellName()))
				continue;
			if (!nextRecord.getImsi().equals(record.getImsi())) {
				record = nextRecord;
				storePairs(pairs);
				pairs = new HashMap<AdjacentLocPair, int[]>();
				continue;
			}
			AdjacentLocPair pair = list.getAdjacentLocPair(record.getCellName(), nextRecord.getCellName());
			if (!pairs.containsKey(pair)) {
				int[] arr = new int[25];
				arr[record.getHour()] = 1;
				pairs.put(pair, arr);
			} else {
				pairs.get(pair)[record.getHour()] += 1;
			}
			record = nextRecord;
			count++;
			if (count % 1000 == 0) {
				System.out.println(count);
			}
		}
		storePairs(pairs);
		return list;
	}
	private void storePairs(HashMap<AdjacentLocPair, int[]> pairs) {
		for(AdjacentLocPair pair : pairs.keySet()) {
			pair.addUser(pairs.get(pair));
		}
	}

}
