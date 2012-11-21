package edu.thu.keg.mobiledata.internetgraph.HostTag;

import java.sql.Statement;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AnalyseLdaResult {

	int Column=0,Line=0;
	double Lda_Matrix[][];
	String []Lable_Vec;
	public AnalyseLdaResult(Connection conn,int line,int column )
	{
		this.Column=column;
		this.Line=line;
		//get the lda result
		Lda_Matrix=readLdaResult(Line, Column,"model-final.theta");
		System.out.print("读入lda完成!");
		HashMap<String,Integer>[] Group_Info= new HashMap[Line];//每个组是个哈西表,对应着tag以及每个tag的访问数量
		int [] Goup_totalCount=new int[Line];//记录每个group的taotalcount
		setGroupInfo(conn,Group_Info,Goup_totalCount);
		ArrayList<GroupInfo[]> tag_count_group=analyseGrouInfo(Group_Info,Goup_totalCount);
		
		
		getClusterType(tag_count_group,Goup_totalCount);
		
		
	}
	double[][] readLdaResult(int line,int column,String filename)
	{
		double[][]Lda_Matrix=new double[line][column];
		LineNumberReader l_r=HostTag.getLNR(filename);
		String line_str;
		try {
			line_str = l_r.readLine();
			int i=0;
			while(line_str!=null)
			{
				String [] s=line_str.split(" ");
				for(int j=0;j<column;j++)
				{
					Lda_Matrix[i][j]=Double.valueOf(s[j]);
				}
				i++;
				line_str = l_r.readLine();
			}
			l_r.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Lda_Matrix;
	}
	void setGroupInfo(Connection conn,HashMap<String,Integer>[] Group_Info,int [] Goup_totalCount)
	{
		 
		
		for(int i=0;i<Group_Info.length;i++)
			Group_Info[i]= new HashMap<String,Integer>();
		try{		
			String query="select URI,TotalCount,GroupNum,Tag"+
					" from dbo.new2_URI_Filtered_5_Group_Tag";
			Statement stmt= conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String Tag=rs.getString("Tag");
				String URI=rs.getString("URI");
				int TotalCount=rs.getInt("TotalCount");
				int GroupNum=rs.getInt("GroupNum");
				if(Tag!=null && !Tag.equals("无用"))
				{
					if(Group_Info[GroupNum].containsKey(Tag))
					{
						Group_Info[GroupNum].put(
								Tag, Group_Info[GroupNum].get(Tag)+TotalCount);
					}
					else
						Group_Info[GroupNum].put(Tag, TotalCount);

				}
				Goup_totalCount[GroupNum]+=TotalCount;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	ArrayList<GroupInfo[]> analyseGrouInfo(HashMap<String,Integer>[] group_Info,int [] goup_totalcount)
	{
		ArrayList<GroupInfo[]> group_top=new ArrayList<GroupInfo[]>();
		for(int i=0;i<group_Info.length;i++)
		{
			HashMap<String,Integer> group_now=group_Info[i];
			GroupInfo[] gf= new GroupInfo[group_now.size()];
			
			
			Iterator<String > it_key= group_now.keySet().iterator();
			for(int j=0;j<gf.length;j++)
			{
				String s=it_key.next();
				gf[j]= new GroupInfo(s,(double)group_now.get(s)/(double)goup_totalcount[i]);
			}
			fastLine(gf, 0, gf.length-1);
			
//			GroupInfo[] gf2= new GroupInfo[0];
//			if(gf.length>0)
//			{
//			    gf2= new GroupInfo[1];
//				gf2[0]=gf[0];
//				group_top.add(gf2);
//			}else
//				group_top.add(gf2);
			group_top.add(gf);
			
		}
		int m=0;
		for(int i=0;i<group_top.size();i++)
		{
			GroupInfo gf[]=group_top.get(i);
			if(gf.length>0)
				System.out.print(i+": ");
			for(int j=0;j<gf.length;j++)
			{m++;
				System.out.print(gf[j].Tag+":"+gf[j].Totalcount+" ");
			}
			if(gf.length>0)
			System.out.println();
		}
		System.out.println(m);
		return group_top;
	}
	void getClusterType(ArrayList<GroupInfo[]> tc_group,int [] group_totalcount)
	{
		String [] type=new String[Column];
		for(int j=0;j<Column;j++)
		{
			HashMap<String ,Double> type_Result=new HashMap<>();
			int times=0;
			for(int i=0;i<Line;i++)
			{
				if(tc_group.get(i).length==0)
					continue;
				GroupInfo[] gi=tc_group.get(i);
				if(j==0)
				{
					System.out.print(Lda_Matrix[i][j]+" ");
				}
				if(j==0)
					System.out.println();
				for(int k=0;k<gi.length;k++)
				{
					times++;
					String k_tag=gi[k].Tag;
					double rate=0.7;
					if(type_Result.containsKey(k_tag))
					{
						if(Lda_Matrix[i][j]>rate)
							type_Result.put(k_tag, 
								type_Result.get(k_tag)+gi[k].Totalcount*Lda_Matrix[i][j]);
						else if(Lda_Matrix[i][j]<1-rate)
							type_Result.put(k_tag, 
									type_Result.get(k_tag)-gi[k].Totalcount*Lda_Matrix[i][j]);
					}
					else
					{
						if(Lda_Matrix[i][j]>rate)
							type_Result.put(k_tag,gi[k].Totalcount*Lda_Matrix[i][j]);
						else if(Lda_Matrix[i][j]<1-rate)
							type_Result.put(k_tag,-gi[k].Totalcount*Lda_Matrix[i][j]);
					}
				}
			}
			Iterator<String> it_key= type_Result.keySet().iterator();
			GroupInfo[] temp_GI=new GroupInfo[type_Result.size()];
			System.out.print(j+" "+times+": ");
			int h=0;
			while(it_key.hasNext())
			{
				String s=it_key.next();
				temp_GI[h]= new GroupInfo(s, type_Result.get(s));
				h++;
			
			}
			fastLine(temp_GI,0,temp_GI.length-1);
			for(int i=0;i<temp_GI.length;i++)
				System.out.print(temp_GI[i].Tag+":"+temp_GI[i].Totalcount+" ");
			System.out.println();
		}
	}
    public  void fastLine (GroupInfo [] a,int zuo,int you){
        int i,j;
        double key;
        GroupInfo temp;
        if(zuo>you)return;
        key=a[you].Totalcount;
        i=zuo-1;
        for(j=zuo;j<you;j++){
            if(a[j].Totalcount>key){
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
	public static void main(String arg[])
	{
		
		Connection conn=HostTag.getConnection();
		AnalyseLdaResult arr= new AnalyseLdaResult(conn,755,15);
//		app.mergeList();
//		app.viewTable(conn);
		
		HostTag.disConnection(conn);
	}
}
class GroupInfo
{
	String Tag="";
	double Totalcount=0;
	GroupInfo(String Tag,double Totalcount)
	{
		this.Tag=Tag;
		this.Totalcount=Totalcount;
		
	}
}
