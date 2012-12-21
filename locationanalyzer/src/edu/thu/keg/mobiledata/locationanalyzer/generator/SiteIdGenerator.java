package edu.thu.keg.mobiledata.locationanalyzer.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.thu.keg.mobiledata.dataloader.DataLoader;
import edu.thu.keg.mobiledata.locationanalyzer.AdjacentLocPair;
import edu.thu.keg.mobiledata.locationanalyzer.Site;

public class SiteIdGenerator {
	private class Loc {
		private double latitude;
		private double longitude;
		public double getLatitude() {
			return latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public Loc(double lat, double lon) {
			super();
			this.latitude = lat;
			this.longitude = lon;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			long temp;
			temp = Double.doubleToLongBits(latitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			temp = Double.doubleToLongBits(longitude);
			result = prime * result + (int) (temp ^ (temp >>> 32));
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Loc other = (Loc) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (Double.doubleToLongBits(latitude) != Double
					.doubleToLongBits(other.latitude))
				return false;
			if (Double.doubleToLongBits(longitude) != Double
					.doubleToLongBits(other.longitude))
				return false;
			return true;
		}
		private SiteIdGenerator getOuterType() {
			return SiteIdGenerator.this;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection conn = DataLoader.getBeijingConn();
		SiteIdGenerator gen = new SiteIdGenerator();
		gen.process(conn);
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void process(Connection conn) {
		try {
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery("SELECT Longitude, Latitude" +
					" FROM LocationInfo" +
					" Order by Longitude + Latitude ASC");
			HashMap<Loc, Integer> locMap = new HashMap<Loc, Integer>(50000);
			int i = 0;
			while (rs.next()) {
				double lon = rs.getDouble("Longitude");
				double lat = rs.getDouble("Latitude");
				Loc l = new Loc(lat, lon);
				if (!locMap.containsKey(l)) {
					locMap.put(l, i++);
				}
			}
			PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO SiteInfo" +
						"(SiteId, Longitude, Latitude)" +
						"VALUES (?, ?, ?)");
			conn.setAutoCommit(false);
			for (Entry<Loc, Integer> l : locMap.entrySet()) {
				double lat = l.getKey().getLatitude();
				double lon = l.getKey().getLongitude();
				int id = l.getValue();
				stmt2.setInt(1, id);
				stmt2.setDouble(2, lon);
				stmt2.setDouble(3, lat);
				stmt2.addBatch();
			}
			stmt2.executeBatch();
			conn.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
