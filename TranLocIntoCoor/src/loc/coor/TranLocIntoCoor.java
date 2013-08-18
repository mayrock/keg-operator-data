package loc.coor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TranLocIntoCoor {

	private PrintWriter out;
	private int index = 0;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TranLocIntoCoor ob = new TranLocIntoCoor();
		ob.getLocOutputCoor();
	}

	private void getLocOutputCoor(){
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=BeijingData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			String query = "select Latitude,Longitude " +
					"from BeijingData.dbo.Traffic_121024_latlng";
			double[] lat = new double[1000];
			double[] lng = new double[1000];
			ResultSet rs = stmt.executeQuery(query);
			out = new PrintWriter(new File("D:\\Ariesnix\\LatLng.txt"));
			while(rs.next()){
				lat[index] = rs.getDouble("Latitude");
				lng[index] = rs.getDouble("Longitude");
				tranLocIntoCoor(lat[index],lng[index]);
				index++;
			}
			out.close();
			
			for(int i = 0; i < index; i++) {
				System.out.println(i + " " + index);
				query = "select ConnectTime,Traffic " +
						"from BeijingData.dbo.Traffic_121024 " +
						"where latitude = " + lat[i] + " and longitude = " + lng[i] + " " +
						"order by ConnectTime asc";
				String time;
				double traffic;
				int count = 0;
				rs = stmt.executeQuery(query);
				out = new PrintWriter(new File("D:\\Ariesnix\\Coor-" + i + ".txt"));
				
				while(rs.next()){
					time = rs.getString("ConnectTime");
					traffic = rs.getDouble("Traffic");
					count = completeTime(count,time,traffic);
				}
				if(count != 1440){
					time = "2012-10-24 23:59";
					traffic = 0.0;
					count = completeTime(count,time,traffic);
				}
				out.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private void tranLocIntoCoor(double lat,double lng){
		double x;
		double y;
		x = (lng - 116.3581) / 0.1706;
		y = (39.9759 - lat) / 0.0951;
		if((x <= 0) || (x >= 1)){
			return;
		}
		if((y <= 0) || (y >= 1)){
			return;
		}
		int xCoor;
		int yCoor;
		xCoor = (int)(x * 994);
		yCoor = (int)(y * 722);
		out.println(index + " " + lat + " " + lng + " " + xCoor + " " + yCoor);
		System.out.println(index + " " + xCoor + " " + yCoor);
	}
	
	private int completeTime(int count,String time,double traffic){
		int hour = count / 60;
		int minute = count % 60;
		String[] temp = time.split(" ");
		String[] t = temp[1].split(":");
		int rHour = Integer.parseInt(t[0]);
		int rMinute = Integer.parseInt(t[1]);
		if((hour != rHour) || (minute != rMinute)){
			int loop = (rHour - hour) * 60 + rMinute - minute;
			for(int i = 0; i < loop; i++){
				out.println(temp[0] + " " + String.format("%02d",hour) +
						":" + String.format("%02d",minute) + " " + String.format("%.3f",0.0));
				minute++;
				if(minute == 60){
					minute = 0;
					hour++;
				}
			}
			count += loop;
		}
		out.println(time + " " + String.format("%.3f",traffic));
		count++;
		return count;
	}
}
