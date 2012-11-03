package Map;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.opensymphony.xwork2.ActionContext;
/**
 * 
 * @author WuChao
 *
 */
public class GetLoc {

	/**
	 * @param args
	 */
	public static double[] lat = new double[2000];
	public static double[] lng = new double[2000];
	public static String[] msg = new String[2000];
	private String imsi;
	private String begin;
	private String end;

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String execute()throws IOException{
		// TODO Auto-generated method stub

		int n = getLatLng();
//		arrangeMsg(n);
		ActionContext ac = ActionContext.getContext();
		HttpServletResponse response = ServletActionContext.getResponse();
		HttpServletRequest request = (HttpServletRequest)ac.get(ServletActionContext.HTTP_REQUEST);
		String result = getJsonData(n);
		System.out.println(result);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.getWriter().print(result);
		return null;
	}

	public void arrangeMsg(int n) {
		// TODO Auto-generated method stub
		HashMap<String,String> map = new HashMap<String,String>();
		for(int i = 0;i < n;i++) {
			String latlng = String.valueOf(lat[i]) + String.valueOf(lng[i]);
			System.out.println(latlng);
			if(map.containsKey(latlng)) {
				String message = map.get(latlng);
				String[] arr = message.split("*");
				int k = Integer.parseInt(arr[1]);
				msg[k] = null;
				msg[i] = arr[0] + "\n" + msg[i];
				System.out.println(arr[1] + arr[0]);
				map.put(latlng,msg[i] + "*" + i);
			}
			else map.put(latlng,msg[i] + "*" + i);
		}
	}

	private String getJsonData(int n) {
		//打包经纬度数据
		JSONObject json_result = new JSONObject();
		JSONArray loc_list = new JSONArray();
		JSONObject pim = null;
		try {
			for(int i = 0;i < n;i++) {
				pim = new JSONObject();
				pim.put("lat",lat[i]);
				pim.put("lng",lng[i]);
				pim.put("msg",msg[i]);
				System.out.println(i + ":" + lat[i] + "\t" + lng[i] + "\t" + msg[i]);
				loc_list.put(pim);
			}
			json_result.put("locinfo", loc_list);
		}catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(),e);
		}
		return json_result.toString();
	}
	
	private int getLatLng() {
		//得到经纬度数据
		String imsi_new = this.imsi.trim();
		String begin_new = this.begin.trim();
		String end_new = this.end.trim();
		//去除不必要的空格
		System.out.println(imsi_new + "\t" + begin_new + "\t" + end_new);
		Connection conn = null;
		Statement stmt = null;
		int i = 0;
		int count = 1;
		double latitude;
		double longitude;
		String message;
		String connTime = null;
		String query = "select LocationInfo.Latitude,LocationInfo.Longitude,GN.ConnectTime " +
				"from ZhuData.dbo.GN " +
				"inner join ZhuData.dbo.LocationInfo " +
				"on GN.LAC = LocationInfo.LAC and GN.CI = LocationInfo.CI " +
				"where Imsi = " + imsi_new +
				" and ConnectTime between " + "'" + begin_new + "'" + " and " + "dateadd(day,1,'" + end_new + "') " +
				"order by connectTime asc";
		System.out.println(query);
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				latitude = rs.getDouble("Latitude");
				longitude = rs.getDouble("Longitude");
				message = rs.getString("ConnectTime");
				if(i == 0) {
					lat[i] = latitude;
					lng[i] = longitude;
					msg[i] = "BeginTime : " + message;
					i++;
				}
				else {
					if((lat[i-1] != latitude) || (lng[i-1] != longitude)) {
						lat[i] = latitude;
						lng[i] = longitude;
						msg[i-1] += "\nEndTime : " + connTime + "\ncount = " + count;
						connTime = null;
						count = 1;
						msg[i] = "BeginTime : " + message;
						i++;
					}
					else {
						connTime = message;
						count++;
					}
				}
			}
			if(i > 0) msg[i-1] += "\nEndTime : " + connTime + "\ncount = " + count;
			stmt.close();
			conn.close();
		}catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return i;
	}

}
				