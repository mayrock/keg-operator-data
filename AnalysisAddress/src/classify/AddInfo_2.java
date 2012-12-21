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

public class AddInfo_2 {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int[] siteId = new int[2000];
		String[][] count = new String[2000][24];
		int[][] timeZone = new int[2000][7];
		Connection conn;
		Statement stmt;
		try{
			int i = 0;
			int k = 0;
			Scanner in = new Scanner(new File("D:\\result\\location\\200_27features_4\\Result4.txt"));
			PrintWriter output = new PrintWriter(
					new BufferedWriter(new FileWriter(
							"D:\\result\\location\\200_27features_4\\Result4_new.txt",false)));
			in.nextLine();
			String front = in.nextLine();
			String[] temp = front.split("\t");
			front = temp[0];
			for(i = 1;i < temp.length - 6;i++)
				front += "\t" + temp[i];
			i = 0;
			while(in.hasNext()) {
				String str = in.nextLine();
				temp = str.split("\t");
				siteId[i] = Integer.parseInt(temp[0]);
				for(int j = 0;j < temp.length - 6;j++)
					count[i][j] = temp[j];
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
				while (rs.next()) {
					time_temp = rs.getString("ConnectTime");
					StringBuffer shortTime = new StringBuffer();
					shortTime.append(time_temp);
					shortTime.delete(13,23);
					shortTime.delete(0,11);
					time_temp = shortTime.toString();
					hour = Integer.parseInt(time_temp);
					if(hour <= 5 && hour >= 1)
						timeZone[i][0]++;
					else if(hour <= 8 && hour >= 6)
						timeZone[i][1]++;
					else if(hour <= 10 && hour >= 9)
						timeZone[i][2]++;
					else if(hour <= 13 && hour >= 11)
						timeZone[i][3]++;
					else if(hour <= 17 && hour >= 14)
						timeZone[i][4]++;
					else if(hour <= 21 && hour >= 18)
						timeZone[i][5]++;
					else
						timeZone[i][6]++;
				}
				stmt.close();
				conn.close();
			}
			output.println(front + "\t1\t2\t3\t4\t5\t6\t7");
			System.out.println(front + "\t1\t2\t3\t4\t5\t6\t7");
			for(i = 0;i < n;i++) {
				for(int j = 0;j < temp.length - 6;j++) {
					output.print(count[i][j] + "\t");
					System.out.print(count[i][j] + "\t");
				}
				for(int j = 0;j < 6;j++) {
					output.print(timeZone[i][j] + "\t");
					System.out.print(timeZone[i][j] + "\t");
				}
				output.println(timeZone[i][6]);
				System.out.println(timeZone[i][6]);
			}
			output.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}