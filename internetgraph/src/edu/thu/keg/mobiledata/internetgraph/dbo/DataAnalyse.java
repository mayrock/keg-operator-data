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

import edu.thu.keg.mobiledata.internetgraph.HostTag.HostTag;
import edu.thu.keg.mobiledata.internetgraph.dbprocesser.URIMerger;

public class DataAnalyse {
	private static int totalTime_ms=0;
	private static int count=0;
	private static int perTime_ms;
	
	
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
		String query="select Imsi,GroupNum"+
				" from dbo.new_GN_Filtered_4_Grouped_For_Character";
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
				
				System.out.println(i+"条数据读入!");
				uID=String.valueOf(rs.getBigDecimal("Imsi"));
				Addr=rs.getString("GroupNum").replace(" ", "");
//				//保留域名
//				Addr=URIMerger.processUri(Addr);
//				Location=rs.getShort("Lac")+"+"+rs.getString("Ci");
//				Calendar CT=Calendar.getInstance();
//				ConnectionTime=rs.getString("ConnectTime");
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				java.util.Date dt = sdf.parse(ConnectionTime);
//				CT.setTime(dt);
//				Date FRT=rs.getDate("RequestTime");
//				Date LPT=rs.getDate("LastPkgTime");
//				int hour=CT.get(CT.HOUR_OF_DAY);
//				long FRT_ms=FRT.getTime();
//				long LPT_ms=LPT.getTime();
//				String UserAgent=rs.getString("UserAgent");
				int timeSegment=0;
//				if(hour>=0&&hour<4)
//					timeSegment=1;
//				else if(hour>=4&&hour<8)
//					timeSegment=2;
//				else if(hour>=8&&hour<12)
//					timeSegment=3;
//				else if(hour>=12&&hour<16)
//					timeSegment=4;
//				else if(hour>=16&&hour<20)
//					timeSegment=5;
//				else if(hour>=20&&hour<24)
//					timeSegment=6;
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
	public void getHostRelation(DataAnalyse da,ConnectionGraph CG)
	{
//		int UserNum=CG.graphUsers.size();
//		int HostNum=CG.graphHosts.size();
//		int EdgeNum=CG.graphEdges.size();
//		System.out.println("Users:"+UserNum);
//		System.out.println("Hosts:"+HostNum);
//		System.out.println("Edged:"+EdgeNum);
//		Set<String> ukey_co=CG.graphUsers.keySet();
//		Object [] u_Imes=(ukey_co.toArray());
		
		
//		Host[] h_1000=getCharacterVect(CG);
		String [] Host_Addr=new String [1000];
		HashMap<String, Double>[] h_1000=readCharacterVect("HostVectors_Host1000_4.txt",Host_Addr);
//		
//		RelationMatrix=getRalationVector(h_1000);
		double RelationMatrix[][]=readRaletionMatrix("HostVectors_1000_4.txt", 1000);
		BufferedOutputStream b_f=HostTag.getBOS("HostVectors_1000_column_4.txt");
		for(int i=0;i<RelationMatrix.length;i++)
		{
			for(int j=i+1;j<RelationMatrix[i].length;j++)
			{
				writeString(b_f, Host_Addr[i]+" "+Host_Addr[j]+" "+RelationMatrix[i][j]+"\n");
			}
		}
//		
//		System.out.println("计算完成夹角ok!");
//		ArrayList<HashSet<Integer>> group_Result=getGroupArray(Host_Addr,RelationMatrix);
//		
//		BufferedOutputStream b_f=HostTag.getBOS("Host_Group.txt");
//		for(int i=0;i<group_Result.size();i++)
//		{
//			Iterator<Integer> it_v=group_Result.get(i).iterator();
//			while(it_v.hasNext())
//			  writeString(b_f, Host_Addr[it_v.next()]+" "+String.valueOf(i)+"\n");
//		}
//		try {
//			b_f.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		insertRecord(getConnection("ZhuData"), "Host_Group.txt", "new_GN_Filtered_4_Group");
	}
	
	Host[] getCharacterVect(ConnectionGraph CG)
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
		//得到标签数据库的所有数据到一个哈希表中
//		Hashtable<String, String> HostTagHashTable
//					=getHostTagHashTable(getConnection("ZhuData"),"HostTag_New");
//		System.out.println("读取标签表完毕!"+HostTagHashTable.size());
//		=getHostTop(CG ,HostTagHashTable);//得到所有HostTagHashTable里有的Host
		Host[] h_5000=getHostTop(CG,1000); //得到访问量的前1000个
		
		BufferedOutputStream f_b=getBOS("HostVectors_Host890_4.txt");
		for(int i=0;i<h_5000.length;i++)
		{
			Host  h= h_5000[i];
			h.Eigenvector= new double[UserNum];
			System.out.println(i);
//			遍历每一个host的user然后如果有的话就把该纬度的值设置成user的值
			String output_line=h.ADDR+":"+h.TotalConnectNum;
			
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
		return h_5000;
	}
	HashMap<String, Double>[] readCharacterVect(String fineName,String [] Host_Addr)
	{
		HashMap<String, Double> h_1000 []= new HashMap[1000];
		
		
		double [][]RelationMatrix=new double[1000][1000];
		LineNumberReader l_r=HostTag.getLNR(fineName);
		String line;
		try {
			line = l_r.readLine();
			int k=0;
			while(line!=null)
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
	double [][] readRaletionMatrix(String fineName,int lineNum)
	{
		double RelationMatrix[][]= new double [lineNum][lineNum];
		LineNumberReader l_r2=HostTag.getLNR(fineName);
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
	double[][] getRalationVector(Host[] h_5000)
	{
//		计算以上生成的Host的数组的夹角值,生成新的相似矩阵
		double [][]RelationMatrix= new double[h_5000.length][h_5000.length];
		BufferedOutputStream f_b=getBOS("HostVectors_1000_4.txt");
//		BufferedOutputStream f_b2=getBOS("HostVectors_New_Cloumn_Merged.txt");
		for(int i=0;i<h_5000.length;i++)
		{	
			System.out.println(i);
			Host  h2= h_5000[i];
			String str="";
		
			for(int j=0;j<h_5000.length;j++)
			{
				Host  h3= h_5000[j];
				if(i<=j)
				{
					RelationMatrix[i][j]=getCosRec(h2.Eigenvector,h3.Eigenvector);
//					if(i<j)
//					{
//						writeString(f_b2, h2.ADDR+" "+h3.ADDR+" "+
//								String.format("%.4f",RelationMatrix[i][j])+"\n");
//					}
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
	double[][] getRalationVector(HashMap<String, Double>[] h_5000)
	{
//		计算以上生成的Host的数组的夹角值,生成新的相似矩阵
		double [][]RelationMatrix= new double[h_5000.length][h_5000.length];
		BufferedOutputStream f_b=getBOS("HostVectors_1000_4.txt");
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
//					if(i<j)
//					{
//						writeString(f_b2, h2.ADDR+" "+h3.ADDR+" "+
//								String.format("%.4f",RelationMatrix[i][j])+"\n");
//					}
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
	ArrayList<HashSet<Integer>> getGroupArray(String[] Host_addr, double [][] Ral_Matrix)
	{
		double Alpha=0.7678;
//		String strGroup[] = new String[Host_addr.length];
		ArrayList<HashSet<Integer>> group= new ArrayList<HashSet<Integer>>();
		boolean Added;
		for(int i=0;i<Ral_Matrix.length;i++)
		{
			Added=compareToGroup(group,Ral_Matrix,i,Alpha);
			if(!Added)
			{
				Alpha+=0.00001;
				i=-1;
				group.clear();
//				strGroup= new String[Host_addr.length];
				System.out.println("更新alpha:"+Alpha);
			}
				
		}
		System.out.println("一共有组："+group.size());
//		for(int i=0;i<group.size();i++)
//		{
//			if(group.get(i).size()>1)
//			System.out.println("grop:"+i+" "+group.get(i).size()+" members");
//		}
		
		
		return group;
	}
	boolean compareToGroup(ArrayList<HashSet<Integer>> group,
			double [][] R_Matrix,int line_num,double alpha)
	{
		int index=-1;
		for(int i= 0;i<group.size();i++)
		{
			Iterator<Integer> it_Group=group.get(i).iterator();
			boolean isBelongTo=true;
			while(it_Group.hasNext())
			{
				int mem=it_Group.next();
				if(R_Matrix[mem][line_num]<alpha)
				{
					isBelongTo=false;
					break;
				}
			}
			if(isBelongTo)
			{
				if(index==-1)//index没设置过，还不属于任何一个group
					index=i;
				else if(index!=-1)//已经属于一个group了，返回false
					return false;
			}
		}
		if(index!=-1)
			group.get(index).add(line_num);
		else
		{
			 HashSet<Integer> h=new HashSet<Integer>();
			 h.add(line_num);
			 group.add(h);
		}
		return true;
	}
	public Hashtable<String, String> getHostTagHashTable(Connection conn,String tableName)
	{
		//读取数据
				Statement stmt = null;
				Hashtable<String, String> HostTag=new Hashtable<String, String>();
		String query="select URI,Tag"+
				" from "+tableName;
		int i=0;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				String URI=rs.getString("URI");
				String Tag=rs.getString("Tag");
				if(!Tag.equals("无用"))
				{
					HostTag.put(URI, Tag);
					i++;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}

		disConnection(conn);
		return HostTag;
	}
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
	public void insertRecord(Connection conn,String fileName, String tableName)
	{
		Statement stmt = null;
		String query3="";
//		String query4="delete from HostTag where Tag='你妈' ";
		
		try {
			stmt = conn.createStatement();
			
			
			int i=0;
			LineNumberReader f_b=HostTag.getLNR(fileName);
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
	
	public static void main(String args[]) 
	{
		ConnectionGraph app= new ConnectionGraph("test");
		
		long t1=System.currentTimeMillis();
		DataAnalyse bpp= new DataAnalyse();			
		
		try {
//			Connection conn=getConnection("Zhudata");
//			bpp.viewTable(conn,app);
//			disConnection(conn);
		} catch (Exception e) {
			// TODO: handle exception
		}
//		app=DataAnalyse.inputBinary("graphMap_Mengo_Merged_Filtered.dat");
		
//		System.out.println("内存建立完毕!");
//		System.out.println("图建立时间："+(System.currentTimeMillis()-t1)/(double)1000+"秒");
//		long t2=System.currentTimeMillis();
//
//		System.out.println("URI:"+app.graphHosts.size());
//		System.out.println("Imsi:"+app.graphUsers.size());
//		System.out.println("Edge:"+app.graphEdges.size());
//		DataAnalyse.outputBinary(app,"graphMap_Mengo_Filtered_Grouped890.dat");
//		System.out.println("序列化生成完毕!");
		bpp.getHostRelation(bpp,app);
		System.out.println("搞定HostRelation!");
//		System.out.println("关系矩阵生成时间："+(System.currentTimeMillis()-t2)/(double)1000+"秒");
		
//		app.printAllSystem();
	}
	
}
