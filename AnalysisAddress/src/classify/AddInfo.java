package classify;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AddInfo {

	/**
	 * @param args
	 */
	private final static String[] aeraSpecies = {
		"美食","休闲娱乐","购物","结婚","宾馆酒店","旅游景点",
		"教育培育","生活服务","医疗服务","丽人","汽车服务",
		"运动健身","商务服务","机构","金融银行","楼宇小区","交通出行"};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		double[] lat = new double[2000];
		double[] lng = new double[2000];
		int[] siteId = new int[2000];
		String[][] count = new String[2000][aeraSpecies.length + 9];
		int[][] timeZone = new int[2000][6];
/*
		int[][] count = new int[2000][aeraSpecies.length];
		int[] si = new int[2000];
		int[] usc = new int[2000];
		int[] mc = new int[2000];
		int[] uc = new int[2000];
		int[] tc = new int[2000];
		int[] work = new int[2000];
		int[] life = new int[2000];
		int[] bus = new int[2000];
		int[] other = new int[2];
*/
		Connection conn;
		Statement stmt;
		try{
			int i = 0;
			int k = 0;
			Scanner in = new Scanner(new File("C:\\Users\\wuchao\\Git\\" +
					"keg-operator-data\\result\\location\\AreaCount_500_new.txt"));
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter("C:\\Users\\wuchao\\Git\\" +
							"keg-operator-data\\result\\location\\AreaCount_500_allKind_new.txt",false)));
/*			Scanner in = new Scanner(new File("C:\\Users\\wuchao\\Git\\" +
					"keg-operator-data\\result\\location\\AreaCount_500_allKind.txt"));
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter("C:\\Users\\wuchao\\Git\\" +
							"keg-operator-data\\result\\location\\AreaCount_500_new.txt",false)));
			in.nextLine();
			while(in.hasNext()) {
				String str = in.nextLine();
				String[] temp = str.split("\t");
				lat[i] = Double.parseDouble(temp[1]);
				lng[i] = Double.parseDouble(temp[2]);
				for(int j = 0;j < aeraSpecies.length;j++)
					count[i][j] = Integer.parseInt(temp[j+7]);
				i++;
			}
			in.close();
			int n = i;
			System.out.println(n);
			for (i = 0; i < n; i++) {
				conn = null;
				stmt = null;
				String query = "Select SiteId,UserCount,MinuteCount,URICount,TotalCount "
						+ "From ZhuData.dbo.new2_SiteInfo5 where "
						+ "Longitude = " + lng[i] + " and Latitude = " + lat[i];
				conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;" +
						"databaseName=ZhuData;integratedSecurity=true;");
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					si[i] = rs.getInt("SiteId");
					usc[i] = rs.getInt("UserCount");
					mc[i] = rs.getInt("MinuteCount");
					uc[i] = rs.getInt("URICount");
					tc[i] = rs.getInt("TotalCount");
				}
				else {
					other[k++] = i;
					System.out.println(i);
				}
				stmt.close();
				conn.close();
			}
			output.print("SiteId\tLatitude\tLongitude\tUserCount\tMinuteCount\tURICount\tTotalCount");
			for(i = 0;i < aeraSpecies.length;i++) {
				output.print("\t" + aeraSpecies[i]);
			}
			output.println("\t工作区\t生活区\t商业区");
			for(i = 0;i < n && i != other[0] && i != other[1];i++) {
				output.print(si[i] + "\t" + lat[i] + "\t" + lng[i] +
						"\t" + usc[i] + "\t" + mc[i] + "\t" + uc[i] + "\t" + tc[i]);
				System.out.print(si[i] + "\t" + lat[i] + "\t" + lng[i] +
						"\t" + usc[i] + "\t" + mc[i] + "\t" + uc[i] + "\t" + tc[i]);
				for(int j = 0;j < aeraSpecies.length;j++) {
					output.print("\t" + count[i][j]);
					System.out.print("\t" + count[i][j]);
				}
				work[i] = count[i][5] + count[i][10] +
						count[i][12] + count[i][13] +
						count[i][14] + count[i][15] +
						count[i][16];
				life[i] = count[i][0] + count[i][6] +
						count[i][7] + count[i][8] +
						count[i][9] + count[i][11];
				bus[i] = count[i][1] + count[i][2] +
						count[i][3] + count[i][4];
				output.println("\t" + work[i] + "\t" + life[i] + "\t" + bus[i]);
				System.out.println("\t" + work[i] + "\t" + life[i] + "\t" + bus[i]);
			}
			output.close();
*/
			String front = in.nextLine();
			while(in.hasNext()) {
				String str = in.nextLine();
				String[] temp = str.split("\t");
				siteId[i] = Integer.parseInt(temp[0]);
				for(int j = 0;j < aeraSpecies.length + 9;j++)
					count[i][j] = temp[j + 1];
				i++;
			}
			in.close();
			int n = i;
			System.out.println(n);
			for (i = 0; i < n; i++) {
				conn = null;
				stmt = null;
				String query = "SELECT ConnectTime "
						+ "From ZhuData.dbo.new2_GN_Filtered_4 where "
						+ "siteId = " + siteId[i];
				System.out.println(i);
				conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;" +
						"databaseName=ZhuData;integratedSecurity=true;");
				stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				String time_temp;
				int hour;
				timeZone[i][0] = 0;
				timeZone[i][1] = 0;
				timeZone[i][2] = 0;
				timeZone[i][3] = 0;
				while (rs.next()) {
					time_temp = rs.getString("ConnectTime");
					StringBuffer shortTime = new StringBuffer();
					shortTime.append(time_temp);
					shortTime.delete(13,23);
					shortTime.delete(0,11);
					time_temp = shortTime.toString();
					hour = Integer.parseInt(time_temp);
					if(hour <= 3 && hour >= 0)
						timeZone[i][0]++;
					else if(hour <= 7 && hour >= 4)
						timeZone[i][1]++;
					else if(hour <= 11 && hour >= 8)
						timeZone[i][2]++;
					else if(hour <= 15 && hour >= 12)
						timeZone[i][3]++;
					else if(hour <= 19 && hour >= 16)
						timeZone[i][4]++;
					else
						timeZone[i][5]++;
				}
				stmt.close();
				conn.close();
			}
			output.println(front + "\ttimeZone1\ttimeZone2\ttimeZone3\ttimeZone4\ttimeZone5\ttimeZone6");
			System.out.println(front + "\ttimeZone1\ttimeZone2\ttimeZone3\ttimeZone4\ttimeZone5\ttimeZone6");
			for(i = 0;i < n;i++) {
				output.print(siteId[i]);
				System.out.print(siteId[i]);
				for(int j = 0;j < aeraSpecies.length + 9;j++) {
					output.print("\t" + count[i][j]);
					System.out.print("\t" + count[i][j]);
				}
				for(int j = 0;j < 6;j++) {
					output.print("\t" + timeZone[i][j]);
					System.out.print("\t" + timeZone[i][j]);
				}
				output.println();
				System.out.println();
			}
			output.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}