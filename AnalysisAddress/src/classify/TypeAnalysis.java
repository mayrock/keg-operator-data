package classify;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

import net.sf.json.JSONObject;

public class TypeAnalysis {

	/**
	 * @param args
	 */
	private double[] lat = new double[2000];
	private double[] lng = new double[2000];

	private final static String[] aeraSpecies = {
		"美食","休闲娱乐","购物","结婚","宾馆酒店","旅游景点",
		"教育培育","生活服务","医疗服务","丽人","汽车服务",
		"运动健身","商务服务","机构","金融银行","楼宇小区","交通出行"};

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TypeAnalysis ob = new TypeAnalysis();
		String outputAddr = "E://AeraCount//AeraCount2.txt";
		int n = ob.getLatLng();
		try {
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter(outputAddr,false)));
			for (int i = 0; i < n; i++) {
				output.println(ob.lat[i] + "\t" + ob.lng[i]);
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	try {
			String city = URLEncoder.encode("呼和浩特", "UTF-8");
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter(outputAddr,false)));
			output.println("\t经度\t纬度\t总数");
			for (int i = 0; i < ob.lat.length; i++) {
				output.print(i + "\t" + ob.lat[i] + "\t" + ob.lng[i]);
				System.out.print(i + "\t" + ob.lat[i] + "\t" + ob.lng[i]);
//				for (int j = 0; j < aeraSpecies.length; j++) {
					JSONObject json_result = new JSONObject();
					String str = "http://openapi.aibang.com/search" +
							"?app_key=f41c8afccc586de03a99c86097e98ccb" +
							"&city=" + city +
							"&lat=" + ob.lat[i] + "&lng=" + ob.lng[i] +
//							"&q=" + URLEncoder.encode(
//									aeraSpecies[j],"UTF-8") +
							"&as=1000&alt=json&from=1&to=300";
					System.out.print("\t" + str);
					URL url = new URL(str);
					HttpURLConnection connection =
							(HttpURLConnection)url.openConnection();
					connection.connect();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(
									connection.getInputStream(),"UTF-8"));
					String line = reader.readLine();
					json_result = JSONObject.fromObject(line);
					int count = json_result.getInt("index_num");
					output.println("\t" + count);
					System.out.println("\t" + count);
					reader.close();
					connection.disconnect();
//				}
			}
			output.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private int getLatLng() {
		//得到经纬度数据
		Connection conn = null;
		Statement stmt = null;
		int i = 0;
		try{
			String query = "Select SiteId,UserCount,MinuteCount,URICount,TotalCount " +
					"From ZhuData.dbo.new_SiteInfo_2 " +
					"Where Latitude = "+""+" and Longitude = " +
					"Order By UserCount desc";
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				lat[i] = rs.getDouble("Latitude");
				lng[i] = rs.getDouble("Longitude");
				i++;
			}
			stmt.close();
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

}
