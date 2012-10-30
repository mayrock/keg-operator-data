package Map;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

	public String execute()throws IOException{
		// TODO Auto-generated method stub

		int n = getLatLng();
		HttpServletResponse response = ServletActionContext.getResponse();
		String result = getJsonData(n);
		System.out.println(result);
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Expires", "0");
		response.setHeader("Pragma", "No-cache");
		response.getWriter().print(result);
		return null;
	}

	private static String getJsonData(int n) {
		JSONObject json_result = new JSONObject();
		JSONArray loc_list =new JSONArray();
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
	
	private static int getLatLng() {
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
				"where Imsi = 460028333886413 " +
				"order by connectTime asc";
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
			msg[i-1] += "\nEndTime : " + connTime + "\ncount = " + count;
			stmt.close();
			conn.close();
		}catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return i;
	}

}
				