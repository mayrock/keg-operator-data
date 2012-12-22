package Map;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionContext;

/**
 * @author wuchao
 */
public class SqlQuery {

	private ArrayList<Double> lat = new ArrayList<Double>();
	private ArrayList<Double> lng = new ArrayList<Double>();
	private ArrayList<Integer> id = new ArrayList<Integer>();
	private HashMap<String,Integer> map1 = new HashMap<String,Integer>();
	private HashMap<Integer,String> map2 = new HashMap<Integer,String>();
	private String query;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	private class ErrorExpectedResultException extends Exception {
		//if any unexpected error occurs or the input sql query can't get any loc
		//this exception must be throw to return an empty response
		public ErrorExpectedResultException() {
			super("Your sql query isn't right!");
		}
	}

	/**
	 * main
	 */
	public String execute() throws IOException {
		ActionContext ac = ActionContext.getContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		String result;
		try {
			getLatLng();
			result = getJsonData();
		}catch(ErrorExpectedResultException e) {
			result = "{\"locinfo\":[]}";
		}
		System.out.println(result);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.getWriter().print(result);
		return null;
	}

	/**
	 * get lat_lng and siteId
	 */
	private void getLatLng() throws ErrorExpectedResultException {
		Connection conn = null;
		try {
			Statement stmt = null;
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=BeijingData;" +
					"integratedSecurity=true;");
			double latitude;
			double longitude;
			int siteId;
			String s;

			//step1: get two hash map
			stmt = conn.createStatement();
			String query_loc = "Select SiteId,Longitude,Latitude " +
					"From SiteInfo";
			ResultSet rs = stmt.executeQuery(query_loc);
			while(rs.next()) {
				s = "";
				latitude = rs.getDouble("Latitude");
				longitude = rs.getDouble("Longitude");
				if(latitude > longitude) {
					double t = latitude;
					latitude = longitude;
					longitude = t;
				}
				s += latitude + " " + longitude;
				map1.put(s,rs.getInt("SiteId"));
				map2.put(rs.getInt("SiteId"),s);
			}
			stmt.close();

			//step2: judge the input
			stmt = conn.createStatement();
			rs = stmt.executeQuery(this.query);
			String str = "";
			if(rs.next()) {
				try {
					latitude = rs.getDouble("Latitude");
				}catch(SQLException e) {
					str += "1";
				}
				try {
					longitude = rs.getDouble("Longitude");
				}catch(SQLException e) {
					str += "2";
				}
				try {
					siteId = rs.getInt("SiteId");
				}catch(SQLException e) {
					str += "3";
				}
				if(str.equals("123") || str.equals("13") || str.equals("23"))
					throw new ErrorExpectedResultException();
			}
			stmt.close();

			//step3: get lat_lng and siteId
			stmt = conn.createStatement();
			rs = stmt.executeQuery(this.query);
			if(str.equals("")) {
				//contain all
				while(rs.next()) {
					latitude = rs.getDouble("Latitude");
					longitude = rs.getDouble("Longitude");
					if(latitude > longitude) {
						double t = latitude;
						latitude = longitude;
						longitude = t;
					}
					lat.add(latitude);
					lng.add(longitude);
					id.add(rs.getInt("SiteId"));
				}
			}
			else if(str.equals("3")) {
				//only have lat_lng
				while(rs.next()) {
					s = "";
					latitude = rs.getDouble("Latitude");
					longitude = rs.getDouble("Longitude");
					if(latitude > longitude) {
						double t = latitude;
						latitude = longitude;
						longitude = t;
					}
					lat.add(latitude);
					lng.add(longitude);
					s += latitude + " " + longitude;
					siteId = map1.get(s);
					id.add(siteId);
				}
			}
			else {
				//have siteId
				String[] temp;
				while(rs.next()) {
					siteId = rs.getInt("SiteId");
					id.add(siteId);
					s = map2.get(siteId);
					temp = s.split(" ");
					lat.add(Double.parseDouble(temp[0]));
					lng.add(Double.parseDouble(temp[1]));
				}
			}
			stmt.close();
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println("An error occurs in retrievaling database!");
			throw new ErrorExpectedResultException();
		}finally {
			try {
				conn.close();
			}catch(SQLException e) {
				System.out.println("Can't close connection!");
				throw new ErrorExpectedResultException();
			}
		}
	}

	/**
	 * pack up json data
	 */
	private String getJsonData() throws ErrorExpectedResultException {
		JSONObject json_result = new JSONObject();
		JSONArray loc_list = new JSONArray();
		JSONObject pim = null;
		try {
			for(int i = 0; i < lat.size(); i++) {
				pim = new JSONObject();
				pim.put("id",id.get(i));
				pim.put("lat",lat.get(i));
				pim.put("lng",lng.get(i));
				loc_list.put(pim);
			}
			json_result.put("locinfo", loc_list);
		}catch(Exception e) {
			System.out.println("An error occurs in packing up json data!");
			throw new ErrorExpectedResultException();
		}
		return json_result.toString();
	}

}