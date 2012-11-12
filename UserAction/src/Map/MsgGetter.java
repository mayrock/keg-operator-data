/**
 * 
 * @author WuChao
 *
 */
package Map;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

public class MsgGetter {

	private class LocationTransferRecord {

		private String loc1;
		private String loc2;
		private int hour;
		private int userCount;
		private int totalCount;

		public String getLoc1() {
			return loc1;
		}

		public String getLoc2() {
			return loc2;
		}

		public int getHour() {
			return hour;
		}

		public int getUserCount() {
			return userCount;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public LocationTransferRecord(
				String loc1,String loc2,int hour,int userCount,int totalCount) {
			super();
			this.loc1 = loc1;
			this.loc2 = loc2;
			this.hour = hour;
			this.userCount = userCount;
			this.totalCount = totalCount;
		}

	}

	private class LatlngRecord {

		private Double lat1;
		private Double lng1;
		private Double lat2;
		private Double lng2;

		public Double getLat1() {
			return lat1;
		}

		public Double getLng1() {
			return lng1;
		}

		public Double getLat2() {
			return lat2;
		}

		public Double getLng2() {
			return lng2;
		}

		public LatlngRecord(Double lat1, Double lng1, Double lat2, Double lng2) {
			super();
			this.lat1 = lat1;
			this.lng1 = lng1;
			this.lat2 = lat2;
			this.lng2 = lng2;
		}

	}

	/**
	 * @param args
	 */
	public ArrayList<LocationTransferRecord> records = new ArrayList<LocationTransferRecord>(200);
	public ArrayList<LatlngRecord> latlngs = new ArrayList<LatlngRecord>(200);
	private String loc1;
	private String loc2;
	private String hour;
	private String count;

	public String getLoc1() {
		return loc1;
	}

	public void setLoc1(String loc1) {
		this.loc1 = loc1;
	}

	public String getLoc2() {
		return loc2;
	}

	public void setLoc2(String loc2) {
		this.loc2 = loc2;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String execute()throws IOException{
		// TODO Auto-generated method stub

		ActionContext ac = ActionContext.getContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		getAdjLoc();
		getLatlng();
		String result = getJsonData();
		System.out.println(result);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.getWriter().print(result);
		return null;
	}

	private String getJsonData() {
		//打包地址和经纬度信息
		JSONObject json_result = new JSONObject();
		JSONArray msg_list = new JSONArray();
		JSONArray latlng_list = new JSONArray();
		JSONObject pim = null;
		try {
			for(LocationTransferRecord record : records) {
				pim = new JSONObject();
				pim.put("loc1",record.getLoc1());
				pim.put("loc2",record.getLoc2());
				pim.put("hour",record.getHour());
				pim.put("count",record.getUserCount());
				msg_list.put(pim);
			}
			for(LatlngRecord latlng : latlngs) {
				pim = new JSONObject();
				pim.put("lat1",latlng.getLat1());
				pim.put("lng1",latlng.getLng1());
				pim.put("lat2",latlng.getLat2());
				pim.put("lng2",latlng.getLng2());
				latlng_list.put(pim);
			}
			json_result.put("msginfo",msg_list);
			json_result.put("latlnginfo",latlng_list);
		}catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}
		return json_result.toString();
	}

	private void getAdjLoc() {
		//得到地址信息
		String loc1_new = this.loc1.trim();
		String loc2_new = this.loc2.trim();
		String hour_new = this.hour.trim();
		String count_new = this.count.trim();
		System.out.println(
				loc1_new + "*" + loc2_new + "*" + hour_new + "*" + count_new);
		//去除不必要的空格
		String query;
		String str1 = "";
		String str2 = "";
		String str3 = "";
		String[] arr;
		if(loc1_new.equals("") && loc2_new.equals("") && hour_new.equals("") && count_new.equals(""))
			query = "select Top 200 LocName1,LocName2,Hour,UserCount, TotalCount " +
					"from AdjacentLocation " +
					"order by UserCount desc";
		else {
			if(loc1_new.equals("") && !(loc2_new.equals("")))
				str1 = "LocName2 = '" + loc2_new + "' ";
			else if (!(loc1_new.equals("")) && loc2_new.equals(""))
				str1 = "LocName1 = '" + loc1_new + "' ";
			else
				str1 = "LocName1 = '" + loc1_new + "' and LocName2 = '"
						+ loc2_new + "' ";
			if (hour_new.equals(""))
				str2 = "Hour >= 0 ";
			else
				str2 = "Hour = " + hour_new + " ";
			if (count_new.equals(""))
				str3 = "UserCount >= 0 ";
			else {
				arr = count_new.split("-");
				if (arr.length == 1)
					str3 = "UserCount = '" + arr[0] + "' ";
				else
					str3 = "UserCount >= '" + arr[0] + "' and UserCount <= '"
							+ arr[1] + "' ";
			}
			query = "select Top 200 LocName1,LocName2,Hour,UserCount, TotalCount "
					+ "from AdjacentLocation "
					+ "where "
					+ str1
					+ "and "
					+ str2 + "and " + str3 + "order by UserCount desc";
		}
		Connection conn = null;
		Statement stmt = null;
		System.out.println(query);
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String l1 = rs.getString("LocName1");
				String l2 = rs.getString("LocName2");
				int h = rs.getInt("Hour");
				int nUser = rs.getInt("UserCount");
				int nTotal = rs.getInt("TotalCount");
				LocationTransferRecord record = new 
						LocationTransferRecord(l1,l2,h,nUser,nTotal);
				records.add(record);
			}
			stmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void getLatlng() {
		//得到经纬度信息
		Connection conn = null;
		Statement stmt = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			for(LocationTransferRecord record : records) {
				String query = "select Longitude,Latitude " +
						"from ZhuData.dbo.LocationInfo " +
						"where CellName = '" + record.getLoc1() +
						"' or CellName = '" + record.getLoc2() + "'";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				Double[] latitude = new Double[2];
				Double[] longitude = new Double[2];
				int i = 0;
				while(rs.next()) {
					latitude[i] = rs.getDouble("Latitude");
					longitude[i] = rs.getDouble("Longitude");
					i++;
				}
				LatlngRecord latlng = new 
						LatlngRecord(latitude[0],longitude[0],latitude[1],longitude[1]);
				latlngs.add(latlng);
			}
			stmt.close();
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}