package edu.thu.keg.mobiledata.internetgraph.dbo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class DataAnalyse {
	private static int totalTime_ms=0;
	private static int count=0;
	private static int perTime_ms;
	
	public static void main(String args[]) throws Exception
	{
		ConnectionGraph app= new ConnectionGraph("test");
//		app.initialGraph();
//		app.insertEdge("0001", "baidu.com", "home", 1, "@0001");
//		app.insertEdge("0001", "baidu.com", "home", 2, "@0003");
//		app.insertEdge("0001", "baidu.com", "outside", 1, "@0005");
//		
//		app.insertEdge("0002", "baidu.com", "home", 3, "@0004");
//		app.insertEdge("0002", "baidu.com", "home", 2, "@0011");
//		app.insertEdge("0002", "baidu.com", "outside", 1, "@0006");
//		app.insertEdge("0002", "baidu.com", "outside", 1, "@0007");
//		app.insertEdge("0002", "baidu.com", "home", 1, "@0012");
//		
//		app.insertEdge("0002", "sina.com", "home", 3, "@0010");
//		
//		app.insertEdge("0001", "sina.com", "home", 1, "@0008");
//		app.insertEdge("0001", "sina.com", "home", 2, "@0009");
//		app.insertEdge("0001", "sina.com", "home", 1, "@0002");
		

		DataAnalyse bpp= new DataAnalyse();			
		Connection conn=getConnection();
		bpp.viewTable(conn,app);
		disConnection(conn);
		app.printAllSystem();
		DataAnalyse.outputBinary(app);
		
		app=DataAnalyse.inputBinary("graphMap.dat");
		app.printAllSystem();
	}
	public static void outputBinary(ConnectionGraph cg)
	{
		try {
			ObjectOutputStream f = new ObjectOutputStream(
					new FileOutputStream("graphMap.dat"));
			f.writeObject(cg);
			f.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
	}
	public static ConnectionGraph inputBinary(String filename)
	{
		ConnectionGraph re=null;
		try {
			ObjectInputStream f = new ObjectInputStream(
					new FileInputStream(filename));
			re=(ConnectionGraph)(f.readObject());
			f.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return re;
	}
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
	public  void viewTable(Connection conn,ConnectionGraph CGraph)throws Exception{
		//读取数据
		Statement stmt = null;
		int t;
		CGraph.initialGraph();
//		PrintWriter out=new PrintWriter("temp");
		String query="select Imsi,Lac,Ci,ConnectTime,Host,UserAgent"+
				" from dbo.GN";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int i=0;
			while(rs.next()&&i<10) {
				String uID=rs.getString("Imsi");
				String Addr=rs.getString("Host");
				String Location=rs.getShort("Lac")+"+"+rs.getString("Ci");
				Calendar CT=null;
				CT.setTime(rs.getDate("ConnectTime"));
//				Date FRT=rs.getDate("RequestTime");
//				Date LPT=rs.getDate("LastPkgTime");
				int hour=CT.get(CT.HOUR_OF_DAY);
//				long FRT_ms=FRT.getTime();
//				long LPT_ms=LPT.getTime();
				String UserAgent=rs.getString("UserAgent");
				int timeSegment=0;
				if(hour>=0&&hour<4)
					timeSegment=1;
				else if(hour>=4&&hour<8)
					timeSegment=2;
				else if(hour>=8&&hour<12)
					timeSegment=3;
				else if(hour>=12&&hour<16)
					timeSegment=4;
				else if(hour>=16&&hour<20)
					timeSegment=5;
				else if(hour>=20&&hour<24)
					timeSegment=6;
				CGraph.insertEdge(uID, Addr, Location, timeSegment, UserAgent);
				i++;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			if(stmt!=null) stmt.close();
		}
//		out.close();
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

}
