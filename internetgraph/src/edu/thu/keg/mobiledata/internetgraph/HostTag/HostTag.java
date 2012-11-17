package edu.thu.keg.mobiledata.internetgraph.HostTag;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.thu.keg.mobiledata.internetgraph.dbprocesser.URIMerger;


public class HostTag {

	public HostTag()
	{
		
	}
	public void mergeList()
	{
		Hashtable<String, String> Ht=new Hashtable<String, String>();
		LineNumberReader f_b=getLNR("URI_New.txt");
		String line="";
		try {
			while(true) {
				
				line=f_b.readLine();
				if(line==null)
					break;
				String [] a=line.split(" ");
				
				Ht.put(URIMerger.processUri(a[0]), a[1]);
			}
			f_b.close();
			
			BufferedOutputStream b_s=getBOS("URI_New_Merged.txt");
			Enumeration<String> e_key=Ht.keys();
			
			int i=0;
			while(e_key.hasMoreElements())
			{
				line=e_key.nextElement();
				byte b []=(line+" "+Ht.get(line)+"\n").getBytes();
				b_s.write(b);
				b_s.flush();
				
				
			}
			b_s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void insertTag(Connection conn)
	{
		Statement stmt = null;
		String query3="";
//		String query4="delete from HostTag where Tag='你妈' ";
		
		try {
			stmt = conn.createStatement();
			
			
			int i=0;
			LineNumberReader f_b=getLNR("URI_New_Merged.txt");
			String line="";
			while(true) {
				line=f_b.readLine();
				if(line==null)
					break;
				String [] a=line.split(" ");
				query3="insert into HostTag_New values('"+a[0]+"','"+a[1]+"') ";
				stmt.execute(query3);
			}

			f_b.close();
			System.out.println("ok!");
		}catch(SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public  void viewTable(Connection conn)
	{
		//读取数据
		Statement stmt = null;
		int Host_sum=0;
//		PrintWriter out=new PrintWriter("temp");
		String query="select URI,TotalCount"+
				" from dbo.URI_New order by TotalCount desc,UserCount";

		try {
			stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(query);
			int i=0;
			BufferedOutputStream f_b=getBOS("URI.txt");
			while(rs.next()&& i<350) {
				
				String URI=rs.getString("URI");
				int TotalCount=rs.getInt("TotalCount");
				Host_sum=Host_sum+TotalCount;
				URI=URI.replace(" ", "");
				writeString(f_b, URI+"\n");
				
				i++;
//				System.out.println(i+":已经快了...");
				
			}
			double sum_per=(double)Host_sum/166891967;
//			System.out.println((double)Host_sum/166891967);
			writeString(f_b, String.valueOf(sum_per)+"\n");
			closeBOS(f_b);
			System.out.println("ok!");
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static BufferedOutputStream getBOS(String Filename)
	{
		File outfile=new File(Filename);
		FileOutputStream f;
		BufferedOutputStream f_b=null;
		try {	
			f = new FileOutputStream(outfile,false);
			f_b=new BufferedOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return f_b;
	}
	public static LineNumberReader getLNR(String Filename)
	{
		File infile=new File(Filename);
		FileInputStream f;
		LineNumberReader f_b=null;
		try {	
			f = new FileInputStream(infile);
			f_b = new LineNumberReader(new InputStreamReader(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        return f_b;
	}
	public void closeBOS(BufferedOutputStream f_b)
	{
		
        try {
        	f_b.flush();
			f_b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void writeString(BufferedOutputStream f_b,String Content)
	{
		 try {
	        	
	            byte [] b;
	            b=Content.getBytes();
	            f_b.write(b);
	            f_b.flush();
	        } catch (IOException ex) {
	           System.out.println(ex);
	        }// TODO add your handling code here:
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
	public static  void disConnection(Connection conn) {
		//中断连接
		try {
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String arg[])
	{
		HostTag app= new HostTag();
		Connection conn=app.getConnection();
//		app.mergeList();
//		app.viewTable(conn);
		app.insertTag(conn);
		app.disConnection(conn);
	}
}
