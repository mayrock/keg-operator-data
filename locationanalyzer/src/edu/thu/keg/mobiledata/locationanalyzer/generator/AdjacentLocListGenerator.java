/**
 * 
 */
package edu.thu.keg.mobiledata.locationanalyzer.generator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashMap;

import edu.thu.keg.mobiledata.dataloader.DataLoader;
import edu.thu.keg.mobiledata.locationanalyzer.AdjacentLocList;
import edu.thu.keg.mobiledata.locationanalyzer.AdjacentLocPair;
import edu.thu.keg.mobiledata.locationanalyzer.Site;

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
			conn = DataLoader.getBeijingConn();
			AdjacentLocList list = generator.getListFromDB(conn);
			
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn.prepareStatement("INSERT INTO AdjacentLocation" +
					"(SiteId1, SiteId2, Hour, UserCount, TotalCount)" +
					"VALUES (?, ?, ?, ?, ?)");
			for (Site loc : list.getSites().values()) {
				for (AdjacentLocPair pair: loc.getNextSites()) {
					int siteId1 = pair.getSite1().getSiteId();
					int siteId2 = pair.getSite2().getSiteId();
					int[] uc = pair.getUsersPerHourCount();
					int[] tc = pair.getTotalPerHourCount();
					for (int i = 0; i < 24; ++i) {	
						if (pair.getUsersCount() == 0)
							continue;
						stmt.setInt(1, siteId1);
						stmt.setInt(2, siteId2);
						stmt.setInt(3, i);
						stmt.setInt(4, uc[i]);
						stmt.setInt(5, tc[i]);
						stmt.addBatch();
					}
				}
				stmt.executeBatch();
				conn.commit();
				System.out.println(loc.getSiteId() + " succeed.");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public class LocationRecord {
		private String imsi;
		private int siteId;
		private int hour;
		private int longitude;
		private int latitude;
		public String getImsi() {
			return imsi;
		}
		public int getSiteId() {
			return siteId;
		}
		public int getHour() {
			return hour;
		}
		
		public int getLongitude() {
			return longitude;
		}
		public int getLatitude() {
			return latitude;
		}
		public void init(String imsi, int siteId, int hour, int longtitude, int latitude) {
			this.imsi = imsi;
			this.siteId = siteId;
			this.hour = hour;
			this.longitude = longtitude;
			this.latitude = latitude;
		}
		public LocationRecord(String imsi, int siteId, int hour, int lon, int lat) {
			super();
			init(imsi, siteId, hour, lon, lat);
		}
		public LocationRecord(ResultSet rs) throws SQLException {
			String imsi = rs.getString("IMSI");
			int siteId = rs.getInt("SiteId");
			Calendar c = Calendar.getInstance();
			c.setTime(rs.getTime("ConnectTime"));
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int longitude = rs.getInt("Longitude");
			int latitude = rs.getInt("Latitude");
			init(imsi, siteId, hour, longitude, latitude);
		}
	}
	public AdjacentLocList getListFromDB(Connection conn) throws SQLException {
		AdjacentLocList list = new AdjacentLocList();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT IMSI,SiteId, Longitude,Latitude,ConnectTime FROM Data" 
		        + " ORDER BY IMSI ASC, ConnectTime ASC");
		rs.next();
		LocationRecord record = new LocationRecord(rs);
		LocationRecord nextRecord;
		HashMap<AdjacentLocPair, int[]> pairs = new HashMap<AdjacentLocPair, int[]>();
		long count = 0;
		while (rs.next()) {
			if (count % 1000000 == 0) {
				System.out.println(count);
			}
			count++;
			nextRecord = new LocationRecord(rs);
			if (nextRecord.getSiteId() == record.getSiteId())
				continue;
			if (!nextRecord.getImsi().equals(record.getImsi())) {
				record = nextRecord;
				storePairs(pairs);
				pairs = new HashMap<AdjacentLocPair, int[]>();
				continue;
			}
			Site site1 = list.getSite(record.getSiteId());
			if (site1 == null) {
				site1 = list.addSite(record.getSiteId(), record.getLongitude(), record.getLatitude());
			}
			Site site2 = list.getSite(nextRecord.getSiteId());
			if (site2 == null) {
				site2 = list.addSite(nextRecord.getSiteId(),
						nextRecord.getLongitude(), nextRecord.getLatitude());
			}
			int hour = record.getHour();
			AdjacentLocPair pair = list.getAdjacentLocPair(site1, site2);
			if (!pairs.containsKey(pair)) {
				int[] arr = new int[24];
				arr[hour] = 1;
				pairs.put(pair, arr);
			} else {
				pairs.get(pair)[hour] += 1;
			}
			record = nextRecord;
			
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
