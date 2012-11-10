/**
 * author ybz
 * 判断标签正确率
 */
package edu.thu.keg.mobiledata.internetgraph.dbo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;

public class AnalyseHostMatrix {

	Hashtable<String , String> hosttag;
	ArrayList<HostInfo> Hi_array;
	public AnalyseHostMatrix()
	{
		hosttag=getHostTagTable("ZhuData", "HostTag_New");
		Hi_array=getHostInfo();
		System.out.println("ok");
		System.out.println(getRightRate());
		int i=0;
		
	}
	public double getRightRate()
	{
		int RateNum=3;
		double re;
		int rightNum=0;
		for(int i=0;i<Hi_array.size();i++)
		{
			HostInfo hi=Hi_array.get(i);
			double top5_value[]=new double[RateNum];
			int top5_index[]=new int[RateNum];
			for(int j=0;j<RateNum;j++)
			{
				top5_value[j]=-1;
				top5_index[j]=-1;
			}
			for(int j=0;j<hi.value.length;j++)
			{
				if(i==j)
					continue;
				for(int k=0;k<RateNum;k++)//一次和top5的数字比较
				{
					if(hi.value[j]>top5_value[k])
					{
						
						for(int m=RateNum-1;m>k;m--)
						{
							top5_value[m]=top5_value[m-1];
							top5_index[m]=top5_index[m-1];
						}
						top5_value[k]=hi.value[j];
						top5_index[k]=j;
						break;
					}
				}
			}
			int pos=0,neg=0;
			for(int j=0;j<RateNum;j++)
			{
				if(top5_index[j]==-1)
					continue;
				if(Hi_array.get(top5_index[j]).Tag.equals(hi.Tag))
						pos++;
				else
					neg++;
			}
			if(pos>=neg)
				rightNum++;
			
		}
		return re=(double)rightNum/(double)Hi_array.size();
	}
	public ArrayList<HostInfo> getHostInfo()
	{
		ArrayList<HostInfo> Hi_a= new ArrayList<HostInfo>();
		LineNumberReader lnr=getLNR("HostVectors_New.txt");
		String s;
		
		try {
			s = lnr.readLine();
			while(s!=null)
			{
				HostInfo hi= new HostInfo();
				String s_line[]=s.split(" ");
				hi.Host=s_line[s_line.length-1];
				hi.Tag=hosttag.get(s_line[s_line.length-1]);
				hi.value= new double[s_line.length-1];
				for(int i=0;i<s_line.length-1;i++)
				{
					hi.value[i]=Double.valueOf(s_line[i]);
				}
				Hi_a.add(hi);
				s = lnr.readLine();
			}
			lnr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Hi_a;
	}
	public Hashtable<String , String> getHostTagTable(String dataBase,String tablename)
	{
		DataAnalyse Da= new DataAnalyse();
		Connection conn=DataAnalyse.getConnection(dataBase);
		Hashtable<String , String> ht=Da.getHostTagHashTable(conn, tablename);
		DataAnalyse.disConnection(conn);
		return ht;
	}
	public LineNumberReader getLNR(String Filename)
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
	public static void main(String arg[])
	{
		AnalyseHostMatrix app =new AnalyseHostMatrix();
	}	
}

class HostInfo
{
	String Tag="";
	double [] value;
	String Host="";
}