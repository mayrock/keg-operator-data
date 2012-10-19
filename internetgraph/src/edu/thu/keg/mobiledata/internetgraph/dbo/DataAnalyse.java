package edu.thu.keg.mobiledata.internetgraph.dbo;

import java.io.BufferedOutputStream;
import java.io.File;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;

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
		
		long t1=System.currentTimeMillis();
		DataAnalyse bpp= new DataAnalyse();			
		Connection conn=getConnection();
		bpp.viewTable(conn,app);
		disConnection(conn);
		System.out.println("内存建立完毕!");
		System.out.println("图建立时间："+(System.currentTimeMillis()-t1)/(double)1000+"秒");
		long t2=System.currentTimeMillis();
//		app.printAllSystem();
//		DataAnalyse.outputBinary(app);
		bpp.getHostRelation(app);
		System.out.println("搞定!");
		System.out.println("关系矩阵生成时间："+(System.currentTimeMillis()-t2)/(double)1000+"秒");
//		app=DataAnalyse.inputBinary("graphMap.dat");
//		app.printAllSystem();
	}
	/*
	 * 序列化输出
	 */
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
	/*
	 *读取序列化文件
	 */
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
	/*
	 * 数据库连接
	 */
	public static Connection getConnection() {
		//建立连接
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=OperatorData;integratedSecurity=true;");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Connected to database");
	    return conn;
	}
	 /*
	  * 数据库表的读取
	  */
	public  void viewTable(Connection conn,ConnectionGraph CGraph)throws Exception{
		//读取数据
		Statement stmt = null;
		int t;
		CGraph.initialGraph();
//		PrintWriter out=new PrintWriter("temp");
		String query="select Imsi,Lac,Ci,ConnectTime,Host,UserAgent"+
				" from dbo.GNComplete";
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int i=0;
			while(rs.next()) {
				String uID=String.valueOf(rs.getBigDecimal("Imsi"));
				String Addr=rs.getString("Host");
				String Location=rs.getShort("Lac")+"+"+rs.getString("Ci");
				Calendar CT=Calendar.getInstance();
				String d_f=rs.getString("ConnectTime");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date dt = sdf.parse(d_f);
				CT.setTime(dt);
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
//				System.out.println(i+":已经快了...");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
//			if(stmt!=null) 
//				stmt.close();
		
//		out.close();
	}
	/*
	 * 得到Host的相似度矩阵
	 */
	public void getHostRelation(ConnectionGraph CG)
	{
		int UserNum=CG.graphUsers.size();
		int HostNum=CG.graphHosts.size();
		int EdgeNum=CG.graphEdges.size();
		System.out.println("Users:"+UserNum);
		System.out.println("Hosts:"+HostNum);
		System.out.println("Edged:"+EdgeNum);
		Enumeration<Host> h_en=CG.graphHosts.elements();
		Set<String> ukey_co=CG.graphUsers.keySet();
		Object [] u_Imes=(ukey_co.toArray());
		String output="";
		double sum=0;
		for(int i=0;i<HostNum;i++)
		{
			Host  h= h_en.nextElement();
			h.Eigenvector.setSize(UserNum);
//			System.out.println(i);
			for(int j=0;j<u_Imes.length;j++)
			{
//				System.out.println(u_Imes[j]);
				if(h.ConnectedUser.containsKey((String)u_Imes[j]+h.ADDR))
				{
					Double v=(double)(h.ConnectedUser.get((String)u_Imes[j]+h.ADDR).TotalCount);
					h.Eigenvector.setElementAt(v,j);
				}
				else
					h.Eigenvector.setElementAt(0.0, j);
			}
//			double op=0;
//			for(int k=0;k<h.Eigenvector.size();k++)
//			{
//				op=op+h.Eigenvector.get(k);
//				output=output+String.valueOf(h.Eigenvector.get(k))+" ";
//			}sum=sum+op;
//			output=String.valueOf(op)+": "+output+"\n";
//			DataAnalyse.writeFile("HostVectors.txt", output);
//			output="";
		}
		Enumeration<Host> h_en2=CG.graphHosts.elements();
	    DecimalFormat df = new DecimalFormat("#.00");
	    NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
		for(int i=0;i<HostNum;i++)
		{	
			Host  h2= h_en2.nextElement();
			Enumeration<Host> h_en3=CG.graphHosts.elements();
			String str="";
			for(int j=0;j<HostNum;j++)
			{
				Host  h3= h_en3.nextElement();
				str=str+String.valueOf(String.format("%.4f",getDistance(h2.Eigenvector,h3.Eigenvector))+" ");
			}
			DataAnalyse.writeFile("HostVectors.txt", str+h2.ADDR+"\n");
		}
		System.out.println("ok!");
	}
	public static void writeFile(String Filename,String Content)
	{
		 try {
	        	File outfile=new File(Filename);
	        	FileOutputStream  f= new FileOutputStream(outfile,true);
	            BufferedOutputStream f_b=new BufferedOutputStream(f);
	            byte [] b;
	            b=Content.getBytes();
	            f_b.write(b);
	            f_b.flush();
	            f_b.close();
	        } catch (IOException ex) {
	           System.out.println(ex);
	        }// TODO add your handling code here:
	}
	public double getDistance(Vector<Double> a,Vector<Double> b)
	{
	
		double re=0;
		for(int i=0;i<a.size();i++)
			re=re+a.get(i)*b.get(i);
		return Math.sqrt(re);
		
	}
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
