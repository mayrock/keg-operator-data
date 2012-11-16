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

public class Temp {

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
		int[][] count = new int[aeraSpecies.length][2000];
		int[] si = new int[2000];
		int[] usc = new int[2000];
		int[] mc = new int[2000];
		int[] uc = new int[2000];
		int[] tc = new int[2000];
		int[] work = new int[2000];
		int[] life = new int[2000];
		int[] bus = new int[2000];
		Connection conn = null;
		Statement stmt = null;
		try{
			int i = 0;
			Scanner in = new Scanner(new File("E:\\AeraCount\\AeraCount_500_all.txt"));
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter("E://AeraCount//AeraCount.txt",false)));
			in.nextLine();
			while(in.hasNext()) {
				String str = in.nextLine();
				String[] temp = str.split("\t");
				lat[i] = Double.parseDouble(temp[1]);
				lng[i] = Double.parseDouble(temp[2]);
				for(int j = 0;j < aeraSpecies.length;j++)
					count[j][i] = Integer.parseInt(temp[j+3]);
				i++;
			}
			in.close();
			int n = i;
			String query = "Select SiteId,UserCount,MinuteCount,URICount,TotalCount " +
					"From ZhuData.dbo.new_SiteInfo_2 " +
					"Order By UserCount desc";
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:1433;" +
					"databaseName=ZhuData;" +
					"integratedSecurity=true;");
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			i = 0;
			while(rs.next()) {
				si[i] = rs.getInt("SiteId");
				usc[i] = rs.getInt("UserCount");
				mc[i] = rs.getInt("MinuteCount");
				uc[i] = rs.getInt("URICount");
				tc[i] = rs.getInt("TotalCount");
				i++;
			}
			stmt.close();
			conn.close();
			output.print("SiteId\tLatitude\tLongitude\tUserCount\tMinuteCount\tURICount\tTotalCount");
			for(int j = 0;j < aeraSpecies.length;j++) {
				output.print("\t" + aeraSpecies[j]);
			}
			output.println("\t工作区\t生活区\t商业区");
			for(i = 0;i < n;i++) {
				output.print(si[i] + "\t" + lat[i] + "\t" + lng[i] +
						"\t" + usc[i] + "\t" + mc[i] + "\t" + uc[i] + "\t" + tc[i]);
				System.out.print(si[i] + "\t" + lat[i] + "\t" + lng[i] +
						"\t" + usc[i] + "\t" + mc[i] + "\t" + uc[i] + "\t" + tc[i]);
				for(int j = 0;j < aeraSpecies.length;j++) {
					output.print("\t" + count[j][i]);
					System.out.print("\t" + count[j][i]);
				}
				work[i] = count[5][i] + count[10][i] +
						count[12][i] + count[13][i] +
						count[14][i] + count[15][i] +
						count[16][i];
				life[i] = count[0][i] + count[6][i] +
						count[7][i] + count[8][i] +
						count[9][i] + count[11][i];
				bus[i] = count[1][i] + count[2][i] +
						count[3][i] + count[4][i];
				output.println("\t" + work[i] + "\t" + life[i] + "\t" + bus[i]);
				System.out.println("\t" + work[i] + "\t" + life[i] + "\t" + bus[i]);
			}
			output.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
