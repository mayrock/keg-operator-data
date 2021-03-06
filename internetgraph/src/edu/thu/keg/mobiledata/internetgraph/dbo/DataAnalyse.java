package edu.thu.keg.mobiledata.internetgraph.dbo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Set;

import edu.thu.keg.mobiledata.internetgraph.dbprocesser.URIMerger;

public class DataAnalyse {
	private static int totalTime_ms=0;
	private static int count=0;
	private static int perTime_ms;
	static final int BE=0;
	static final int AF=1;
	static final int ALL=2;
	private final static String DATABASE="BeijingData";
	
	/*
	 * 序列化输出
	 */
	public static void outputBinary(ConnectionGraph cg, String s)
	{
		try {
			ObjectOutputStream f = new ObjectOutputStream(
					new FileOutputStream(s));
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
	public static Connection getConnection(String databaseName) {
		//建立连接
		Connection conn=null;
		try {
			conn=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName="+databaseName+";integratedSecurity=true;");
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
		String query="select Imsi,WebsiteNum,GroupNum,ConnectTime"+
				" from dbo.group_data_filtered6";
		int i=0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			String uID="none";
			String Addr="none";
			String Location="none";
			String ConnectionTime="none";
			String UserAgent="none";
			while(rs.next() ) {
				//				System.out.println(i+"条数据读入!");
				uID=String.valueOf(rs.getBigDecimal("Imsi"));
				Addr=rs.getString("WebsiteNum");
				
				Location=rs.getString("GroupNum");
				Calendar CT=Calendar.getInstance();
				ConnectionTime=rs.getString("ConnectTime");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				java.util.Date dt = sdf.parse(ConnectionTime);
				CT.setTime(dt);

				double hour=CT.get(CT.HOUR_OF_DAY);
				hour=hour+CT.get(CT.MINUTE)/60.0;
				int day= CT.get(CT.DAY_OF_YEAR);
//				long FRT_ms=FRT.getTime();
//				long LPT_ms=LPT.getTime();
//				String UserAgent=rs.getString("UserAgent");
				int timeSegment=(int)(hour/0.5);
				
				CGraph.insertEdge(uID, Addr, Location, day, timeSegment, UserAgent);
				i++;
//				System.out.println(i+":已经快了...");
				
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}System.out.println(i+"个记录时工作日");
//			if(stmt!=null) 
//				stmt.close();
		
//		out.close();
	}
	/*
	 * 得到Host的相似度矩阵,返回阈值
	 */
	public double getHostRelation(DataAnalyse da,ConnectionGraph CG,int TopNum)
	{
		
		int UserNum=CG.graphUsers.size();
		int HostNum=CG.graphHosts.size();
		int EdgeNum=CG.graphEdges.size();
		System.out.println("Users:"+UserNum);
		System.out.println("Hosts:"+HostNum);
		System.out.println("Edged:"+EdgeNum);
//		Set<String> ukey_co=CG.graphUsers.keySet();
//		Object [] u_Imes=(ukey_co.toArray());
		//第一次,生成组
		String [] Host_Addr=new String [TopNum];
		Host[] h_1000=getCharacterVect(CG,Host_Addr,TopNum,"HostCharacterVectors_"+String.valueOf(TopNum)+".txt");//output HostCharacterVecter
		HashMap<String, Double>[] h_1000_byread=readCharacterVect("HostCharacterVectors_"+String.valueOf(TopNum)+".txt",Host_Addr);

		
	//-----------Host_Addr,h_1000都可以出来
		System.out.print(Host_Addr[0]+" "+Host_Addr[1]);
		double [][]RelationMatrix=getRalationVector(h_1000,"HostRelationVectors_"+String.valueOf(TopNum)+".txt");//Host[] 得到RalationMatrix output to file
		double [][]RelationMatrix_byread=readRaletionMatrix("HostRelationVectors_"+String.valueOf(TopNum)+".txt", TopNum);
	
//		double [][]RelationMatrix2=getRalationVector(h_1000_byread,"BJ\\ImsiCharacterVectors_BJ_CT.txt");//HashMap<String, Double>[]得到RalationMatrix
//		
		
		writeColumnRelation(RelationMatrix, Host_Addr,"HostRelationVectors_"+String.valueOf(TopNum)+".txt");//output the column RelationMatrix
		
//		
//		System.out.println("计算完成夹角ok!");
		double yu=0.0;
		while(yu<1.0)
			{
			
				ArrayList<HashSet<Integer>> group_Result=getGroupArray(Host_Addr,yu,RelationMatrix);//get Group	
				System.out.print(yu+" "+group_Result.size());
				yu+=0.05;
			}
		yu=0.5;
		//默认得到0.5阈值的分组
		ArrayList<HashSet<Integer>> group_Result=getGroupArray(Host_Addr,yu,RelationMatrix);//get Group	
		//输出到文件Host_Group.txt中,和相应的数据库Hostxxx_Groupxxx中,和每组网站的个数Group_alpha.txt
		getGroupInfo(Host_Addr,group_Result,yu);
		return group_Result.size();
}
	/**
	 * 输出到文件Host_Group.txt中,和相应的数据库Hostxxx_Groupxxx中,和每组网站的个数Group_alpha.txt
	 * @param Host_Addr
	 * @param group_Result
	 * @param yu
	 */
	public void getGroupInfo(String [] Host_Addr,ArrayList<HashSet<Integer>> group_Result,double yu)
	{
		BufferedOutputStream b_f1 = getBOS("Group_alpha-"
				+ String.valueOf(yu) + ".txt");
		for (int i = 0; i < group_Result.size(); i++) {

			Iterator<Integer> h_i = group_Result.get(i).iterator();
			String str = String.valueOf(i) + ":" + group_Result.get(i).size();
			while (h_i.hasNext()) {
				str = str + " " + Host_Addr[h_i.next()];
			}
			str = str + "\n";
			writeString(b_f1, str);
		}

		//
		BufferedOutputStream b_f = getBOS("Host_Group.txt");
		for (int i = 0; i < group_Result.size(); i++) {
			Iterator<Integer> it_v = group_Result.get(i).iterator();
			while (it_v.hasNext())
				writeString(b_f,
						Host_Addr[it_v.next()] + " " + String.valueOf(i) + "\n");
		}
		try {
			b_f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将Host_Group.txt内容写入一个表
		insertRecord(getConnection("DATABASE"), "Host_Group.txt", 
				"Host"+String.valueOf(group_Result.size())+"_Group"+String.valueOf(group_Result.size()));

	}
/**
 * 第二次website按照时间短调用
 * @param da
 * @param CG
 * @param midDayNum
 */
	public void getGroupRelation(DataAnalyse da,ConnectionGraph CG,int midDayNum)
	{
		//第二次,根据组号生成的,根据时间段生成LDA的数据
		getImsiCharacterVecByTimeslot(CG,"ImsiTimeslotLda_Allday.txt",BE,midDayNum);
		getImsiCharacterVecByTimeslot(CG,"ImsiTimeslotLda_Allday.txt",AF,midDayNum);
		getImsiCharacterVecByTimeslot(CG,"ImsiTimeslotLda_Allday.txt",ALL,midDayNum);
		
		//输出imsi对应的访问前1000个网站的特征向量
//		String [] User_Imsi=new String[CG.graphUsers.size()];
//		User[] h_40w=getImsiCharacterVec(CG,User_Imsi,CG.graphUsers.size(),"ImsiCharacterVectors_BJ_CT_Grouped_Location.txt");
		
	}
/**
 * 得到钱topNum网站的个特征向量	
 * @param CG
 * @param host_addr
 * @param topNum
 * @param filename
 * @return
 */
Host[] getCharacterVect(ConnectionGraph CG,String [] host_addr,
			int topNum,String filename)
	{
		int UserNum=CG.graphUsers.size();
		int HostNum=CG.graphHosts.size();
		int EdgeNum=CG.graphEdges.size();
		System.out.println("Users:"+UserNum);
		System.out.println("Hosts:"+HostNum);
		System.out.println("Edged:"+EdgeNum);
		Set<String> ukey_co=CG.graphUsers.keySet();
		Object [] u_Imes=(ukey_co.toArray());
//		生成Host的数组h_5000,数组的每一个纬度代表着每个Host的这个维度用户的访问次数,构成特征向量
		Host[] h_1000=getHostTop(CG,topNum); //得到访问量的前1000个
		
		BufferedOutputStream f_b=getBOS(filename);
		for(int i=0;i<h_1000.length;i++)
		{
			Host  h= h_1000[i];
			h.Eigenvector= new double[UserNum];
			System.out.println(i);
//			遍历每一个host的user然后如果有的话就把该纬度的值设置成user的值
			String output_line=h.ADDR+":"+h.TotalConnectNum;
			host_addr[i]=h.ADDR;
			for(int j=0;j<u_Imes.length;j++)
			{
//				System.out.println(j);
				String userId=(String)u_Imes[j];
				
				if(h.ConnectedUser.containsKey(userId+h.ADDR))
				{
					int v=(h.ConnectedUser.get(userId+h.ADDR).TotalCount);
					h.Eigenvector[j]=(double)v;
					output_line =output_line+" "+userId+":"+String.valueOf(v);
				}
				else
					h.Eigenvector[j]=0.0;
			}
			output_line +="\n";
			writeString(f_b, output_line);

		}
		try {
			f_b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return h_1000;
	}
	/**
	 * 读取filename特征向量文件存入host_addr
	 * @param fineName
	 * @param Host_Addr
	 * @return
	 */
	HashMap<String, Double>[] readCharacterVect(String fineName,String [] Host_Addr)
	{
		HashMap<String, Double> h_1000 []= new HashMap[Host_Addr.length];
		
		
		LineNumberReader l_r=getLNR(fineName);
		String line;
		try {
			line = l_r.readLine();
			int k=0;
			while(line!=null && k<h_1000.length)
			{
				String s[]=line.split(" ");
				h_1000[k]= new HashMap<String ,Double>();
				
				Host_Addr[k]=(s[0].substring(0, s[0].lastIndexOf(':')));
				for(int i=1;i<s.length;i++)
				{
					
					String ss[]=s[i].split(":");
					if(k==1)
						System.out.print(ss[0]+":"+ss[1]+" ");
					h_1000[k].put(ss[0], Double.valueOf(ss[1]));
				}
				k++;
				
				line=l_r.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("读入完成");
		
		return h_1000;
	}

	/**
	 * 计算User之间的cos相似度并给出矩阵输出
	 * @param CG
	 * @param user_Imsi
	 * @param topNum
	 * @param filename
	 * @return
	 */
	User[] getImsiCharacterVec(ConnectionGraph CG,String [] user_Imsi,
			int topNum,String filename)
	{
		int UserNum=CG.graphUsers.size();
		int HostNum=CG.graphHosts.size();
		int EdgeNum=CG.graphEdges.size();
		System.out.println("Users:"+UserNum);
		System.out.println("Hosts:"+HostNum);
		System.out.println("Edged:"+EdgeNum);
		Set<String> hkey_co=CG.graphHosts.keySet();
		Object [] h_Addr=(hkey_co.toArray());
		User[] u_5000=getUserTop(CG,topNum); //得到访问量的前topNum个排序好的
		
		BufferedOutputStream f_b=getBOS(filename);
		for(int i=0;i<u_5000.length;i++)//针对每一个user,过滤所有的host
		{
			User  u= u_5000[i];
//			u.Eigenvector= new double[HostNum];
			System.out.println(i);
//			遍历每一个host的user然后如果有的话就把该纬度的值设置成user的值
			String output_line=u.IMEI+" "+u.TotalConnectNum;
			user_Imsi[i]=u.IMEI;
			for(int j=0;j<h_Addr.length;j++)
			{
//				System.out.println(j);
				String hostId=(String)h_Addr[j];
				
				if(u.ConnectedHost.containsKey(u.IMEI+" "+hostId))//如果
				{
					int v=(u.ConnectedHost.get(u.IMEI+" "+hostId).TotalCount);
//					u.Eigenvector[j]=(double)v;
					output_line =output_line+" "+hostId+" "+String.valueOf(v);
				}
//				else
//					u.Eigenvector[j]=0.0;
			}
			output_line +="\n";
			writeString(f_b, output_line);

		}
		try {
			f_b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u_5000;
	}
	/**
	 * 第二次通过时间得到,根据组号生成的,根据时间段生成LDA的数据
	 * @param CG
	 * @param outfilename
	 * @param be_af_all BE,AL,ALL前几天和几天和所有的天
	 * @param midDayNum 中间切分天号
	 */
	void getImsiCharacterVecByTimeslot(ConnectionGraph CG, String outfilename,int be_af_all,int midDayNum)
	{
		BufferedOutputStream f_b_before10=null;
		BufferedOutputStream f_b_before10_ran=null;
		BufferedOutputStream f_b_before10_top=null;
		BufferedOutputStream f_b_after10=null;
		BufferedOutputStream f_b_after10_ran=null;
		BufferedOutputStream f_b_after10_top=null;
		BufferedOutputStream f_b=null;
		BufferedOutputStream f_b_ran=null;
		BufferedOutputStream f_b_top=null;
	
		
		if(be_af_all==BE)
		{
			f_b_before10= getBOS("ImsiTimeslotLda_Allday_be10.txt");
			f_b_before10_ran= getBOS("ImsiTimeslotLda_Allday_be10_ran.txt");
			f_b_before10_top= getBOS("ImsiTimeslotLda_Allday_be10_top.txt");
		}
		else if(be_af_all==AF)
		{
			f_b_after10= getBOS("ImsiTimeslotLda_Allday_af10.txt");
			f_b_after10_ran= getBOS("ImsiTimeslotLda_Allday_af10_ran.txt");
			f_b_after10_top= getBOS("ImsiTimeslotLda_Allday_af10_top.txt");
		}
		else if(be_af_all==ALL)
		{
			f_b= getBOS(outfilename);
			f_b_ran= getBOS(outfilename.replace(".txt", "_ran.txt"));			
			f_b_top= getBOS(outfilename.replace(".txt", "_top.txt"));
		}

//		BufferedOutputStream f_b_detail= getBOS("Timeslot_detail.txt");
		
		Iterator<User> i_u=CG.graphUsers.values().iterator();
		System.out.println(CG.graphUsers.values().size());
		User[] u_array=new User[CG.graphUsers.size()];
		int m=0;
		while(i_u.hasNext())
		{	
			u_array[m]=i_u.next();
			m++;
		}
		fastLine(u_array, 0, u_array.length-1);//用户排序
		for(int k=0;k<u_array.length;k++)//每一个用户
		{
			if(k==100000)
				System.out.println("user"+String.valueOf(k));
			else if(k==200000)
				System.out.println("user"+String.valueOf(k));
			else if(k==300000)
				System.out.println("user"+String.valueOf(k));
			else if(k==400000)
				System.out.println("user"+String.valueOf(k));
			
			HashMap<String, Integer> User_output=new HashMap<String, Integer>();
			User u=u_array[k];
			Iterator<Integer> i_day= CG.days_of_year.iterator();
			int day=-1;
			while(i_day.hasNext())//每一天
			{
				day= i_day.next();
				if(be_af_all==BE)
				{
					if(day>=midDayNum)
						continue;
				}
				else if(be_af_all==AF)
				{
					if(day<midDayNum)
						continue;
				}
				ArrayList<String>[] url=new ArrayList[48];
				ArrayList<Integer>[] url_count=new ArrayList[48];
				ArrayList<String>[] url_loc=new ArrayList[48];
				ArrayList<Integer>[] url_loc_maxcount=new ArrayList[48];
				
				for(int i=0;i<48;i++)//每一个时间段
				{
					Iterator<UserHost> i_uh=u.ConnectedHost.values().iterator();
					url[i]= new ArrayList<String>();//保存这一天第i个时间段的前三名url
					url_count[i]= new ArrayList<Integer>();//保存这一天第i个时间段的前三名url的访问次数
					url_loc[i]= new ArrayList<String>();//保存这一天第i个时间段的前三名url的location
					url_loc_maxcount[i]=new ArrayList<Integer>();//保存这一天第i个时间访问前三名最多地点的访问次数
					
					
					while(i_uh.hasNext())//根据每个userHost找出在这一天这个时间段访问量最大的前三个url
					{
						
						UserHost uh=i_uh.next();
						String key_DTs=String.valueOf(day)+" "+String.valueOf(i);
						if(uh.TimeConTable.containsKey(key_DTs))//找到这一天这个时段的数据
						{
							String url_temp=uh.host.ADDR;
							int url_count_temp=uh.TimeConTable.get(key_DTs);
							if(url[i].size()==0)
							{
								url[i].add(url_temp);
								url_count[i].add(url_count_temp);
							}
							else
							{
								int us=url[i].size();
								for(int j=0;j<us;j++)
								{
									if(url_count_temp>url_count[i].get(j))//添加到起前3个中
									{
										url[i].add(j, url_temp);
										url_count[i].add(j, url_count_temp);
										break;
									}
									if(j==us-1)//添加到最后一个
									{
										url[i].add(url_temp);
										url_count[i].add(url_count_temp);
									}
									
								}
							}
							if(url[i].size()>3)
							{
								url[i].remove(3);
								url_count[i].remove(3);
							}
						}
					}
					for(int j=0;j<url[i].size();j++)//获得这个时间段这一天的前三个的url的位置信息储存到url_loc
					{
						UserHost uh=u.ConnectedHost.get(u.IMEI+" "+url[i].get(j));
						Iterator<String> i_loc=uh.LocationConTable.keySet().iterator();
						String max_loc="";
						while(i_loc.hasNext())
						{
							String i_loc_key=i_loc.next().split(" ")[0];
							if(max_loc.equals("") && uh.LocationConTable.get(i_loc_key+" "+String.valueOf(day)+" "+String.valueOf(i))!=null)
							{
								max_loc=i_loc_key;
								continue;
							}
							if(uh.LocationConTable.get(i_loc_key+" "+String.valueOf(day)+" "+String.valueOf(i))
									==null||uh.LocationConTable.get(max_loc+" "+String.valueOf(day)+" "+String.valueOf(i))==null)
								continue;
							if(	uh.LocationConTable.get(i_loc_key+" "+String.valueOf(day)+" "+String.valueOf(i))
									>uh.LocationConTable.get(max_loc+" "+String.valueOf(day)+" "+String.valueOf(i)))
							{
								max_loc=i_loc_key;
							}
						}
						
						url_loc[i].add(j, max_loc);
						url_loc_maxcount[i].add(j, uh.LocationConTable.get(max_loc+" "+String.valueOf(day)+" "+String.valueOf(i)));
								
					}
					for(int j=0;j<url[i].size();j++)//把每个时间段的url,location加入到输出中User_output
					{
						String key_out=url[i].get(j)+" "+url_loc[i].get(j);
						if(User_output.containsKey(key_out))
						{
							User_output.put(key_out, User_output.get(key_out)+1);
						}
						else
							User_output.put(key_out,1);
					}
					//生成详细数据库中用的
//					String line="";
//					for(int k=0;k<url[i].size();k++)
//					{
//						line=u.IMEI
//								+" "+String.valueOf(url[i].get(k))
//								+" "+String.valueOf(url_loc[i].get(k))
//								+" "+String.valueOf(day)
//								+" "+String.valueOf(i)
//								+" "+String.valueOf(k)
//								+" "+String.valueOf(url_count[i].get(k))
//								+" "+String.valueOf(url_loc_maxcount[i].get(k))
//								+"\n";
//						writeString(f_b_detail, line);
//					}
					url[i]=null;
					url_count[i]= null;
					url_loc[i]= null;
					url_loc_maxcount[i]=null;
				}
				
			}//结束了每一天的循环
	//这块是第一次生成lda数据的时候用的,user,website,按照半小时的次数,在这个半小时内访问次数最多的location		
			Iterator<String> i_o=User_output.keySet().iterator();
			String content=u.IMEI,key_t="";
			while(i_o.hasNext())
			{
				key_t=i_o.next();
				content=content+" "+key_t+" "+User_output.get(key_t);
			}
			if(be_af_all==BE)
			{
				if(k%8==0 && k<=400000)
					writeString(f_b_before10_ran, content.trim()+"\n");	
				if(k<50000)
					writeString(f_b_before10_top, content.trim()+"\n");
				writeString(f_b_before10, content.trim()+"\n");
			}
			else if(be_af_all==AF)
			{
				if(k%8==0 && k<=400000)
					writeString(f_b_after10_ran, content.trim()+"\n");	
				if(k<50000)
					writeString(f_b_after10_top, content.trim()+"\n");
				writeString(f_b_after10, content.trim()+"\n");
			}
			else if(be_af_all==ALL)
			{
				if(k%8==0 && k<=400000)
					writeString(f_b_ran, content.trim()+"\n");	
				if(k<50000)
					writeString(f_b_top, content.trim()+"\n");
				writeString(f_b, content.trim()+"\n");
			}
		}
		if(be_af_all==BE)
		{
			closeBOS(f_b_before10);
			closeBOS(f_b_before10_ran);
			closeBOS(f_b_before10_top);
		}
		else if(be_af_all==AF)
		{
			closeBOS(f_b_after10);
			closeBOS(f_b_after10_ran);
			closeBOS(f_b_after10_top);
		}
		else if(be_af_all==ALL)
		{
			closeBOS(f_b);	
			closeBOS(f_b_ran);
			closeBOS(f_b_top);
		}
//		closeBOS(f_b_detail);
	}
	/**
	 * 读取相似度矩阵向量,把最后一栏url,存入host_addr
	 * @param fineName
	 * @param lineNum
	 * @param host_addr
	 * @return 矩阵向量
	 */
	double [][] readRaletionMatrix(String fineName,int lineNum,String [] host_addr)
	{
		double RelationMatrix[][]= new double [lineNum][lineNum];
		LineNumberReader l_r2=getLNR(fineName);
		String line2;
		try {
			line2 = l_r2.readLine();
			int k=0;
			while(line2!=null)
			{
				String s[]=line2.split(" ");
				
				for(int i=0;i<s.length;i++)
				{
					if(i<s.length-1)
						RelationMatrix[k][i]=Double.valueOf(s[i]);
					else
						host_addr[k]=s[i];
				}
				k++;
				
				line2=l_r2.readLine();
			}
			l_r2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RelationMatrix;
	}
	/**
	 * 读取相似度矩阵向量
	 * @param fineName
	 * @param lineNum
	 * @return 矩阵向量
	 */
	double [][] readRaletionMatrix(String fineName,int lineNum)
	{
		double RelationMatrix[][]= new double [lineNum][lineNum];
		LineNumberReader l_r2=getLNR(fineName);
		String line2;
		try {
			line2 = l_r2.readLine();
			int k=0;
			while(line2!=null)
			{
				String s[]=line2.split(" ");
				
				for(int i=0;i<s.length;i++)
				{
					
						RelationMatrix[k][i]=Double.valueOf(s[i]);
				
				}
				k++;
				
				line2=l_r2.readLine();
			}
			l_r2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return RelationMatrix;
	}
	/**
	 * 算出相似度矩阵,topurl和topurl之间的
	 * @param h_5000
	 * @param filename
	 * @return
	 */
	double[][] getRalationVector(Host[] h_5000,String filename)
	{
//		计算以上生成的Host的数组的夹角值,生成新的相似矩阵
		double [][]RelationMatrix= new double[h_5000.length][h_5000.length];
		BufferedOutputStream f_b=getBOS(filename);
//		BufferedOutputStream f_b2=getBOS("HostVectors_New_Cloumn_Merged.txt");
		for(int i=0;i<h_5000.length;i++)
		{	
			System.out.println("R"+i);
			Host  h2= h_5000[i];
			String str="";
		
			for(int j=0;j<h_5000.length;j++)
			{
				Host  h3= h_5000[j];
				if(i<=j)
				{
					RelationMatrix[i][j]=getCosRec(h2.Eigenvector,h3.Eigenvector);
				}
				else
					RelationMatrix[i][j]=RelationMatrix[j][i];
				writeString(f_b, String.format("%.5f",RelationMatrix[i][j])+" ");
			}
			writeString(f_b, h2.ADDR+"\n");
		}
		closeBOS(f_b);
//		closeBOS(f_b2);
		System.out.println("计算完成夹角ok!");
		return RelationMatrix;
	}
	/**
	 * 算出相似度矩阵,topurl和topurl之间的
	 * @param h_5000
	 * @param outputFilename
	 * @return
	 */
	double[][] getRalationVector(HashMap<String, Double>[] h_5000,String outputFilename)
	{
//		计算以上生成的Host的数组的夹角值,生成新的相似矩阵
		double [][]RelationMatrix= new double[h_5000.length][h_5000.length];
		BufferedOutputStream f_b=getBOS(outputFilename);
//		BufferedOutputStream f_b2=getBOS("HostVectors_New_Cloumn_Merged.txt");
		for(int i=0;i<h_5000.length;i++)
		{	if(i%500==0)
			System.out.println(i);
			HashMap<String, Double>  h2= h_5000[i];
			String str="";
		
			for(int j=0;j<h_5000.length;j++)
			{
				HashMap<String, Double>  h3= h_5000[j];
				if(i<=j)
				{
					RelationMatrix[i][j]=getCosRec(h2,h3);
				}
				else
					RelationMatrix[i][j]=RelationMatrix[j][i];
				writeString(f_b, String.format("%.5f",RelationMatrix[i][j])+" ");
			}
			writeString(f_b, "\n");
		}
		closeBOS(f_b);
//		closeBOS(f_b2);
		
		return RelationMatrix;
	}
	/**
	 * 得到url的分组信息
	 * @param Host_addr
	 * @param alpha
	 * @param Ral_Matrix
	 * @return 一个list,每个预算努是个hashmap里面对应url对应的组号
	 */
	ArrayList<HashSet<Integer>> getGroupArray(String[] Host_addr,double alpha, double [][] Ral_Matrix)
	{
		double Alpha=alpha;
//		String strGroup[] = new String[Host_addr.length];
		ArrayList<HashSet<Integer>> group= new ArrayList<HashSet<Integer>>();
		boolean Added;
		for(int i=0;i<Ral_Matrix.length;i++)
		{
			Added=compareToGroup(group,Ral_Matrix,i,Alpha,Host_addr);
			if(!Added)
			{
				Alpha+=0.0001;
				i=-1;
				group.clear();
//				strGroup= new String[Host_addr.length];
				System.out.println("更新alpha:"+Alpha);
			}
				
		}
		System.out.println(Alpha+":"+group.size());
//		for(int i=0;i<group.size();i++)
//		{
//			if(group.get(i).size()>1)
//			System.out.println("grop:"+i+" "+group.get(i).size()+" members");
//		}
		
		
		return group;
	}
	/**
	 * 分组用,让每个组的元素都和其中之一有大于alpha值得
	 * @param group
	 * @param R_Matrix
	 * @param line_num
	 * @param alpha
	 * @param host_addr
	 * @return
	 */
	boolean compareToGroup(ArrayList<HashSet<Integer>> group,
			double [][] R_Matrix,int line_num,double alpha,String [] host_addr)
	{
//		R_Matrix[342][278]=0.7;
//		R_Matrix[278][342]=0.7;
//		R_Matrix[11][3]=0.7;
//		R_Matrix[3][11]=0.7;
//		R_Matrix[46][32]=0.7;
//		R_Matrix[32][46]=0.7;
//		R_Matrix[39][34]=0.7;
//		R_Matrix[34][39]=0.7;
		int index=-1;
		ArrayList<Integer> index_g=new ArrayList<Integer>();
		for(int i= 0;i<group.size();i++)
		{
			Iterator<Integer> it_Group=group.get(i).iterator();
			boolean isBelongTo=false;
			while(it_Group.hasNext())
			{
				int mem=it_Group.next();
				if(R_Matrix[mem][line_num]>=alpha)
				{
					isBelongTo=true;
					break;
				}
			}
			if(isBelongTo)
			{
				index_g.add(i);
				if(index==-1)//index没设置过，还不属于任何一个group
					index=i;
				else if(index!=-1)//已经属于一个group了，返回false
				{
//					Iterator<Integer> it_Group1=group.get(i).iterator();
//					Iterator<Integer> it_Group2=group.get(index).iterator();
//					System.out.println(alpha);
//					System.out.println(host_addr[line_num]+"和下两组都相似");
//					System.out.print("第一组:");
//					int ii=0,jj=0;
//					while(it_Group1.hasNext())
//					{
//						ii=it_Group1.next();
//						System.out.print(host_addr[ii]+" ");
//					}
//					System.out.println();
//					System.out.print("第二组:");
//					while(it_Group2.hasNext())
//					{
//						jj=it_Group2.next();
//						System.out.print(host_addr[jj]+":");
//						System.out.print(ii+" "+jj+" "+R_Matrix[jj][ii]+" ");
//					}
//					System.out.println();
//					return false;
				}
			}
		}
		if(index!=-1)
		{
//			group.get(index).add(line_num);//没有重复的时候,不可以传递合并
		//---------------------------------------------------------------//	
			int mom_group=index_g.get(0);
			for(int i=1;i<index_g.size();i++)
			{
				
				group.get(mom_group).addAll(group.get(index_g.get(i)));
				
			}
			group.get(mom_group).add(line_num);
			for(int i=index_g.size()-1;i>0;i--)
			{
				int del_member=index_g.get(i);
				group.remove(del_member);
			}
		}
		else
		{
			 HashSet<Integer> h=new HashSet<Integer>();
			 h.add(line_num);
			 group.add(h);
		}
		return true;
	}
	
	/**
	 * 得到前topnum的host
	 * @param CG
	 * @param HostTag
	 * @return 前topnum的host数组
	 */
	public Host[] getHostTop(ConnectionGraph CG, Hashtable<String, String> HostTag)
	{
		int HostNum=CG.graphHosts.size();
		Enumeration<String> h_en=HostTag.keys();
		Host[] h_result= new Host[HostTag.size()];
		for(int i=0;i<h_result.length;i++)
		{
			String HostURI=h_en.nextElement();
			h_result[i]=CG.graphHosts.get(HostURI);
		}
		return h_result;
	}
	public Host[] getHostTop(ConnectionGraph CG, int HostMatrixSize)
	{
		int HostNum=CG.graphHosts.size();
//		Iterator<Host> h_en=CG.graphHosts.elements();
		Iterator<Host> h_en=CG.graphHosts.values().iterator();
		Host[] h_all= new Host[HostNum];
		Host[] h_result= new Host[HostMatrixSize];
		for(int i=0;i<h_all.length;i++)
		{
			h_all[i]=h_en.next();
		}
		//Host排序
		fastLine(h_all, 0, h_all.length-1);
		//取得访问次数最多的前5000个Host
		for(int i=0;i<h_result.length;i++)
			h_result[i]=h_all[i];
		return h_result;
	}
	/**
	 * 得到前topnum的user
	 * @param CG
	 * @param UserMatrixSize
	 * @return
	 */
	public User[] getUserTop(ConnectionGraph CG, int UserMatrixSize)
	{
		int UserNum=CG.graphUsers.size();
//		Iterator<Host> h_en=CG.graphHosts.elements();
		Iterator<User> u_en=CG.graphUsers.values().iterator();
		User[] u_all= new User[UserNum];
		User[] u_result= new User[UserMatrixSize];
		for(int i=0;i<u_all.length;i++)
		{
			u_all[i]=u_en.next();
		}
		//Host排序
		fastLine(u_all, 0, u_all.length-1);
		//取得访问次数最多的前5000个Host
		for(int i=0;i<u_result.length;i++)
			u_result[i]=u_all[i];
		return u_result;
	}
	//得到lda输入的随即Num行
	public void getRandomLDAInput(int Num, String filename)
	{
		LineNumberReader l_r=null;
		BufferedOutputStream b_f=null;
		try {
			l_r= getLNR(filename);
			b_f=getBOS(filename.replace(".txt", "")+"_Ran"+String.valueOf(Num)+".txt");
			String line;
			line = l_r.readLine();
			int i=0,j=0;
			while(line!=null && j<Num)
			{
				if(i%5==0)
				{
					writeString(b_f,line+"\n");
					j++;
				}
				i++;
				
				line = l_r.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				l_r.close();
				b_f.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}
	//得到lda输入的前Num行
	public void getTopLDAInput(int Num, String filename)
	{
		LineNumberReader l_r=null;
		BufferedOutputStream b_f=null;
		try {
			l_r= getLNR(filename);
			b_f=getBOS(filename.replace(".txt", "")+"_Top"+String.valueOf(Num)+".txt");
			String line;
			line = l_r.readLine();
			int i=0;
			while(line!=null && i<Num)
			{
				writeString(b_f,line+"\n");
				i++;
				line = l_r.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				l_r.close();
				b_f.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	//将关系矩阵变换成向量
	public void writeColumnRelation(double[][] RelationMatrix,String[] Host_Addr,String filename)
	{
		BufferedOutputStream b_f=getBOS(filename);
		for(int i=0;i<RelationMatrix.length;i++)
		{
			for(int j=i+1;j<RelationMatrix[i].length;j++)
			{
				writeString(b_f, Host_Addr[i]+" "+Host_Addr[j]+" "+RelationMatrix[i][j]+"\n");
			}
		}
		try {
			b_f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/**
 * 帮助方法,通过f_b写入Content内容
 * @param f_b
 * @param Content
 */
	public void writeString(BufferedOutputStream f_b,String Content)
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
	/**
	 * 帮助方法,读取文件流的封装
	 * @param Filename
	 * @return
	 */
	public  LineNumberReader getLNR(String Filename)
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
/**
 * 	帮助方法,输出数据流的封装
 * @param Filename
 * @return
 */
public BufferedOutputStream getBOS(String Filename)
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
	public double getMol(double[] a)
	{
		double re=0;
		for(int i=0;i<a.length;i++)
		{	if(Math.abs(a[i])<0.0001 )
				continue;
			re=re+a[i]*a[i];
		}
		
		return Math.sqrt(re);
	}
	public double getMol(HashMap<String, Double> a)
	{
		double re=0;
		Iterator<Double> it_value;
		if(a.size()>0)
		{
			it_value=a.values().iterator();
			while(it_value.hasNext())
			{
				double v=it_value.next();
				re=re+v*v;
			}
		}
		
		return Math.sqrt(re);
	}
	public double getCosRec(double[] a,double[]  b)
	{
	
		double re=0;
		for(int i=0;i<a.length;i++)
		{	if(Math.abs(a[i])<1e-3 || Math.abs(b[i])<1e-3)
				continue;
			re=re+a[i]*b[i];
		}
		
		return re/(getMol(a)*getMol(b));
		
	}
	public double getCosRec(HashMap<String, Double> a,HashMap<String, Double> b)
	{
	
		double re=0;
		Iterator<String> it_key;
		if(a.size()<=b.size())
		{
			it_key=a.keySet().iterator();
			while(it_key.hasNext())
			{
				String temp_key=it_key.next();
				if(b.containsKey(temp_key))
					re=re+a.get(temp_key)*b.get(temp_key);
			}
		}
		else 
		{
			it_key=b.keySet().iterator();
			while(it_key.hasNext())
			{
				String temp_key=it_key.next();
				if(a.containsKey(temp_key))
					re=re+a.get(temp_key)*b.get(temp_key);
			}
		}
		
		return re/(getMol(a)*getMol(b));
		
	}
   //快速排序
	public  void fastLine (Host [] a,int zuo,int you){
        int i,j;
        int key;
        Host temp;
        if(zuo>you)return;
        key=a[you].TotalConnectNum;
        i=zuo-1;
        for(j=zuo;j<you;j++){
            if(a[j].TotalConnectNum>key){
                i++;
                temp=a[j];
                a[j]=a[i];
                a[i]=temp;
            }
        }
        i++;
        temp=a[j];
        a[j]=a[i];
        a[i]=temp;
        fastLine(a, zuo, i-1);
        fastLine(a, i+1, you);
    }
    public  void fastLine (User [] a,int zuo,int you){
        int i,j;
        int key;
        User temp;
        if(zuo>you)return;
        key=a[you].TotalConnectNum;
        i=zuo-1;
        for(j=zuo;j<you;j++){
            if(a[j].TotalConnectNum>key){
                i++;
                temp=a[j];
                a[j]=a[i];
                a[i]=temp;
            }
        }
        i++;
        temp=a[j];
        a[j]=a[i];
        a[i]=temp;
        fastLine(a, zuo, i-1);
        fastLine(a, i+1, you);
    }
    public  void fastLine (int [] a,int zuo,int you){
        int i,j;
        int key;
        int temp;
        if(zuo>you)return;
        key=a[you];
        i=zuo-1;
        for(j=zuo;j<you;j++){
            if(a[j]>key){
                i++;
                temp=a[j];
                a[j]=a[i];
                a[i]=temp;
            }
        }
        i++;
        temp=a[j];
        a[j]=a[i];
        a[i]=temp;
        fastLine(a, zuo, i-1);
        fastLine(a, i+1, you);
    }
	public static void disConnection(Connection conn) {
		//中断连接
		try {
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 向一个tableName的表中插入,filename的数据,前两列,在数据库联接con的情况下
	 * @param conn
	 * @param fileName
	 * @param tableName
	 */
	public void insertRecord(Connection conn,String fileName, String tableName)
	{
		Statement stmt = null;
		String query3="";
		
		try {
			stmt = conn.createStatement();
			
			
			int i=0;
			LineNumberReader f_b=getLNR(fileName);
			String line="";
			while(true) {
				line=f_b.readLine();
				if(line==null)
					break;
				String [] a=line.split(" ");
				query3="insert into "+tableName+" values('"+a[0]+"','"+a[1]+"') ";
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
	/**
	 * 主要函数入口
	 * @param args
	 */
	public static void main(String args[]) 
	{
		ConnectionGraph app= new ConnectionGraph("test");
		
		long t1=System.currentTimeMillis();
		DataAnalyse bpp= new DataAnalyse();			
		
		try {
			Connection conn=getConnection("DATABASE");
			bpp.viewTable(conn,app);
			disConnection(conn);
		} catch (Exception e) {
			// TODO: handle exception
		}
//		app=DataAnalyse.inputBinary("graphMap_BeijingData.dat");
		
		System.out.println("内存建立完毕!");
		System.out.println("图建立时间："+(System.currentTimeMillis()-t1)/(double)1000+"秒");
		long t2=System.currentTimeMillis();
////
		System.out.println("URI:"+app.graphHosts.size());
		System.out.println("Imsi:"+app.graphUsers.size());
		System.out.println("Edge:"+app.graphEdges.size());
		DataAnalyse.outputBinary(app,"graphMap_BeijingData.dat");
	
		System.out.println("序列化生成完毕!");
		//第一生成分组的数据并且储存在数据库中
		bpp.getHostRelation(bpp,app,1000);
		//*************************************************
		//这是第二次读取数据库文件的时候,利用时间分析lda输入的函数,所以第一编分析url到website不执行
		//对于分组后的数据,生成根据时间的LDA的东西,
		//bpp.getGroupRelation(bpp,app,308);
		//*************************************************
		System.out.println("搞定HostRelation!");
		System.out.println("关系矩阵生成时间："+(System.currentTimeMillis()-t2)/(double)1000+"秒");
	}	
}
