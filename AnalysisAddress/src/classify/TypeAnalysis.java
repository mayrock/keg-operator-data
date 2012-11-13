package classify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

public class TypeAnalysis {

	/**
	 * @param args
	 */
	private double[] lat = new double[2000];
	private double[] lng = new double[2000];
	private int[][] count = new int[20][2000];

	private final static String[] aeraSpecies = {
		"美食","休闲娱乐","购物","结婚","宾馆酒店","旅游景点",
		"教育培育","生活服务","医疗服务","丽人","汽车服务",
		"运动健身","商务服务","机构","金融银行","楼宇小区","交通出行"};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TypeAnalysis ob = new TypeAnalysis();
		String outputAddr = "E://AeraCount//AeraCount.txt";
		ob.getLatLng();
		try {
			String city = URLEncoder.encode("呼和浩特", "UTF-8");
			PrintWriter out = new PrintWriter(outputAddr);
			for (int i = 0; i < ob.lat.length; i++) {
				System.out.println(ob.lat[i] + "*" + ob.lng[i]);
				for (int j = 0; j < aeraSpecies.length; j++) {
					// JSONObject json_result = new JSONObject();
					// JSONArray json_list = new JSONArray();
					String str = "http://openapi.aibang.com/search" +
							"?app_key=f41c8afccc586de03a99c86097e98ccb" +
							"&city=" + city +
							"&lat=" + ob.lat[i] + "&lng=" + ob.lng[i] +
							"&q=" + URLEncoder.encode(
									aeraSpecies[j],"UTF-8") +
							"&as=2500&alt=json&from=1&to=300";
					System.out.println(str);
					URL url = new URL(str);
					HttpURLConnection connection =
							(HttpURLConnection)url.openConnection();
					connection.connect();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									connection.getInputStream(),"UTF-8"));
					String line = reader.readLine();
					// json_list = JSONArray.fromObject(line);
					out.println(i + ":" + line);
					reader.close();
					connection.disconnect();
				}
			}
			out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getLatLng() {
		//得到经纬度数据
		Connection conn = null;
		Statement stmt = null;
		try{
//			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String query = "Select Latitude,Longitude " +
					"From ZhuData.dbo.new_SiteInfo";
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int i = 0;
			while(rs.next()) {
				lat[i] = rs.getDouble("Latitude");
				lng[i]= rs.getDouble("Longitude");
				i++;
			}
			stmt.close();
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
