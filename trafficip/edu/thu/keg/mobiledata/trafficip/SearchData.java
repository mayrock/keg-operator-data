package edu.thu.keg.mobiledata.trafficip;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class SearchData {
	//
//	private static DecimalFormat totalTime=new DecimalFormat("000000000000000000.00");
//	private static DecimalFormat perTime=new DecimalFormat("000000000000000000.00");
	private static int totalTime_ms=0;
	private static int count=0;
	private static int perTime_ms;
	public static Connection getConnection() {
		//建立连接
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=ZhuData;integratedSecurity=true;");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database");
	    return conn;
	}
	//
	@SuppressWarnings("null")
	public static void viewTable(Connection conn)throws Exception{
		//读取数据
		Statement stmt = null;
		int t;
		PrintWriter out=new PrintWriter("temp");
		String query="select Imsi,Ci,CONVERT(smalldatetime,Connecttime)as ConnTime,"+
				"SUM(case when LastPkgTime>RequestTime then cast(datediff(s,RequestTime,LastPkgTime)as decimal(18,2)) end)as ConnLength"+
				" into NewTable"+
				" from dbo.GN"+
				" group by Imsi,Ci,CONVERT(smalldatetime,Connecttime)";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				long ID=rs.getLong("Imsi");
				int CI=rs.getInt("Ci");
				Calendar CT=null;
				CT.setTime(rs.getDate("ConnectTime"));
				Date FRT=rs.getDate("RequestTime");
				Date LPT=rs.getDate("LastPkgTime");
				int hour=CT.get(CT.HOUR_OF_DAY);
				long FRT_ms=FRT.getTime();
				long LPT_ms=LPT.getTime();
				if((hour==8||hour==9||hour==10||hour==18||hour==19||hour==20)&&LPT_ms>FRT_ms) 
				{
					t=(int)(LPT_ms-FRT_ms);
					totalTime_ms+=t;
					count++;
					perTime_ms=totalTime_ms/count;
					if(t>2*perTime_ms) out.println(ID+" "+CI+" "+t);
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(stmt!=null) stmt.close();
		}
		out.close();
	}
	//
/*	public static void dealData() {
		//
		Scanner in=new Scanner("temp");
		while(in.hasNextLine()) {
			long ID=in.nextLong();
			short CI=in.nextShort();
			int t=in.nextInt();
			if(t>2*perTime_ms&&needRemove(CI)) {
				System.out.println(ID+"*"+CI);
			}
		}
		in.close();
	}
	public static boolean isSteady(int CI) {
		
	}*/
	//
	public static void disConnection(Connection conn) {
		//中断连接
		try {
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	//
	public static void main(String[] args)throws Exception{
		//
		Connection conn=getConnection();
		viewTable(conn);
		disConnection(conn);
//		dealData();
	}
	//
}
