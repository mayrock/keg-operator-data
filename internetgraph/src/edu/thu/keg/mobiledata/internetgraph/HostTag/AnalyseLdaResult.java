package edu.thu.keg.mobiledata.internetgraph.HostTag;

import java.sql.Statement;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.plaf.basic.BasicScrollPaneUI.HSBChangeListener;

public class AnalyseLdaResult {

	int Column=0,Line=0;
	double Lda_Matrix[][];
	String []Lable_Vec;
	HashMap<String,Integer> Hash_TagNum= new HashMap<>();
	public AnalyseLdaResult(Connection conn,int line,int column )
	{
		this.Column=column;
		this.Line=line;
		zhuanZhi("model-final.phi","model-final_zhuanzhi.phi",15,755);
		reflection("model-final_zhuanzhi.phi","wordmap.txt","model-final_zhuanzhi_Mapped.phi");
		//get the lda result
		Lda_Matrix=readLdaResult(Line, Column,"model-final_zhuanzhi_Mapped.phi");
		System.out.println("读入lda完成!");
		HashMap<String,Integer>[] Group_Info= new HashMap[Line];//每个组是个哈西表,对应着tag以及每个tag的访问数量
		int [] Goup_totalCount=new int[Line];//记录每个group的taotalcount
		
		setGroupInfo(conn,Group_Info,Goup_totalCount);//设置Group_info和Group_totalCount
		//归一化
		HashMap<String, Double> []Group_Info_Dstribt=Nomolization(Group_Info,Goup_totalCount);//
		
		getClusterType2(Group_Info_Dstribt,Goup_totalCount);
		/*原先
		//得到一个List,每个元素是一个GroupInfo的数组,其中每个记录着标签和这个标签在这个组的访问量的比例
		ArrayList<GroupInfo[]> tag_count_group=
				analyseGrouInfo(Group_Info,Goup_totalCount);
		
		
		getClusterType(tag_count_group,Goup_totalCount);
		*/
		
	}
	void zhuanZhi(String Sourcefile,String Outputfile,int Line ,int Column)
	{
		LineNumberReader l_r_s= HostTag.getLNR(Sourcefile);
		BufferedOutputStream b_f=HostTag.getBOS(Outputfile);
		String[][] matrix_s=new String[Column][Line];
		String str;
		try {
			str = l_r_s.readLine();
			int k=0;
			while(str!=null)
			{
				String s[]=str.split(" ");
				for(int i=0;i<Column;i++)
					matrix_s[i][k]=s[i];
				k++;
				str = l_r_s.readLine();
			}
			
			for(int i =0;i<matrix_s.length;i++)
			{
				String str_re="";
				for(int j=0;j<matrix_s[i].length;j++)
				{
					if(j==0)
						str_re=matrix_s[i][j];
					else
						str_re=str_re+" "+matrix_s[i][j];
				}
				HostTag.writeString(b_f, str_re+"\n");
			}
			l_r_s.close();
			b_f.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void reflection(String Sourcefile,String Mapfile,String Outputfile)
	{
		LineNumberReader l_r_s= HostTag.getLNR(Sourcefile);
		LineNumberReader l_r_m= HostTag.getLNR(Mapfile);
		BufferedOutputStream b_f=HostTag.getBOS(Outputfile);
		int LineNum=0;
		String str_m,str_s[];
		try {
			str_m = l_r_m.readLine();			
			LineNum=Integer.valueOf(str_m);
			int [] map=new int[LineNum];
			str_m=l_r_m.readLine();
			while(str_m!=null)
			{
				String f[]= str_m.split(" ");
				map[Integer.valueOf(f[0])]=Integer.valueOf(f[1]);
				str_m=l_r_m.readLine();
			}
			str_s= new String[LineNum];
			for(int i=0;i<str_s.length;i++)
			{
				str_s[i]=l_r_s.readLine();
			}
			for(int i=0;i<str_s.length;i++)
			{
				HostTag.writeString(b_f, str_s[map[i]]+"\n");
			}
			l_r_s.close();
			l_r_m.close();
			b_f.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
					if(!Hash_TagNum.containsKey(Tag))
						Hash_TagNum.put(Tag, Hash_TagNum.size());
				}
				Goup_totalCount[GroupNum]+=TotalCount;
			}
			Iterator<String> it_key= Hash_TagNum.keySet().iterator();
			while(it_key.hasNext())
			{
				String s=it_key.next();
				System.out.println(s+" "+Hash_TagNum.get(s));
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
//		int m=0;
//		for(int i=0;i<group_top.size();i++)
//		{
//			GroupInfo gf[]=group_top.get(i);
//			if(gf.length>0)
//				System.out.print(i+": ");
//			for(int j=0;j<gf.length;j++)
//			{m++;
//				System.out.print(gf[j].Tag+":"+gf[j].Totalcount+" ");
//			}
//			if(gf.length>0)
//			System.out.println();
//		}
//		System.out.println(m);
		return group_top;
	}
	void getClusterType(ArrayList<GroupInfo[]> tc_group,int [] group_totalcount)
	{
		String [] type=new String[Column];
		for(int j=0;j<Column;j++)
		{
			HashMap<String ,Double> type_Result=new HashMap<>();
			int times=0;
			for(int i=0;i<Line;i++)//755
			{
				if(tc_group.get(i).length==0)
					continue;
				GroupInfo[] gi=tc_group.get(i);
//				if(j==0)
//				{
//					System.out.print(Lda_Matrix[i][j]+" ");
//				}
//				if(j==0)
//					System.out.println();
				for(int k=0;k<gi.length;k++)
				{
					times++;
					String k_tag=gi[k].Tag;
					double rate=0.7;
					if(type_Result.containsKey(k_tag))
					{
						if(Lda_Matrix[i][j]>rate)
							type_Result.put(k_tag, 
								type_Result.get(k_tag)+gi[k].TagInThisPrecent*Lda_Matrix[i][j]);
						else if(Lda_Matrix[i][j]<1-rate)
							type_Result.put(k_tag, 
									type_Result.get(k_tag)-gi[k].TagInThisPrecent*Lda_Matrix[i][j]);
					}
					else
					{
						if(Lda_Matrix[i][j]>rate)
							type_Result.put(k_tag,gi[k].TagInThisPrecent*Lda_Matrix[i][j]);
						else if(Lda_Matrix[i][j]<1-rate)
							type_Result.put(k_tag,-gi[k].TagInThisPrecent*Lda_Matrix[i][j]);
					}
				}
			}
			Iterator<String> it_key= type_Result.keySet().iterator();
			GroupInfo[] temp_GI=new GroupInfo[type_Result.size()];
			System.out.println(j+" ");
			int h=0;
			while(it_key.hasNext())
			{
				String s=it_key.next();
				temp_GI[h]= new GroupInfo(s, type_Result.get(s));
				h++;
			
			}
			fastLine(temp_GI,0,temp_GI.length-1);
			
			for(int i=0;i<temp_GI.length;i++)
			{
				System.out.println(Hash_TagNum.get(temp_GI[i].Tag)+" "+temp_GI[i].Tag+" "+temp_GI[i].TagInThisPrecent);
				
			}
			
//			int c=0;
//			while(c<temp_GI.length)
//			{
//				for(int i=0;i<temp_GI.length;i++)
//				{
//					if(Hash_TagNum.get(temp_GI[i].Tag)==c)
//					{
//						System.out.println(Hash_TagNum.get(temp_GI[i].Tag)+" "+temp_GI[i].Tag+" "+temp_GI[i].Totalcount);
//						break;
//					}
//					
//				}
//				c++;
//			}
//			for(int i=0;i<temp_GI.length;i++)
//				System.out.println(temp_GI[i].Tag+" "+temp_GI[i].Totalcount);

			System.out.println();
		}
	}
	void getClusterType2(HashMap<String, Double>[] group_Info,int [] group_totalcount)
	{
		//村每一个的hash表
		HashMap<String, Double>[][]dis_All= new HashMap[Lda_Matrix.length][Lda_Matrix[0].length];
		for(int i=0;i<Lda_Matrix.length;i++)
		{
			HashMap<String , Double> thisGroup=group_Info[i];
			Iterator<String > it_key= thisGroup.keySet().iterator();
			for(int j=0;j<Lda_Matrix[i].length;j++)
			{   
				dis_All[i][j]= new HashMap<String, Double>();
				while(it_key.hasNext())
				{
					String s =it_key.next();
					dis_All[i][j].put(s, thisGroup.get(s)*Lda_Matrix[i][j]);
				}
			}
		}
		double [][] final_distribution= new double [Lda_Matrix[0].length][Lda_Matrix[0].length];
		String tag_Array[]= new String [Lda_Matrix[0].length];
		Iterator<String> it_key1=Hash_TagNum.keySet().iterator();
		int m=0;
		while(it_key1.hasNext())
		{
			tag_Array[m]=it_key1.next();
			m++;
		}
		for(int j=0;j<Lda_Matrix[0].length;j++)
		{
			Iterator<String> it_key=Hash_TagNum.keySet().iterator();
			int k=0;
			
			while(it_key.hasNext())
			{
				double sum_Tag=0;
				String thisTag=it_key.next();
				for(int i=0;i<Lda_Matrix.length;i++)
				{
					if(dis_All[i][j].containsKey(thisTag))
						sum_Tag=sum_Tag+dis_All[i][j].get(thisTag);//如果这个disall有新闻就+
				}
				final_distribution[k][j]=sum_Tag;
				k++;
			}
			
		}
		for(int i=0;i<final_distribution.length;i++)
		{
			for(int j=0;j<final_distribution[i].length;j++)
			{
				if(j==0)
					System.out.print(tag_Array[i]+" ");
				System.out.print(final_distribution[i][j]+" ");
			}
			System.out.println();
			
		}
	}
	HashMap<String,Double>[] Nomolization(HashMap<String,Integer>[] group_info,int [] groupTotalCount)
	{
		HashMap<String,Double>[] Group_TagDistribution = new HashMap[group_info.length];
		for(int i=0;i<group_info.length;i++)
		{
			Group_TagDistribution[i]=new HashMap<String,Double>();
			HashMap<String,Integer> thisGroup=group_info[i];
			Iterator<String> it_key= thisGroup.keySet().iterator();
			while(it_key.hasNext())
			{
				String s= it_key.next();
				Group_TagDistribution[i].put(s, new Double(thisGroup.get(s)/groupTotalCount[i]));
			}
		}
		return Group_TagDistribution;
	}
    public  void fastLine (GroupInfo [] a,int zuo,int you){
        int i,j;
        double key;
        GroupInfo temp;
        if(zuo>you)return;
        key=a[you].TagInThisPrecent;
        i=zuo-1;
        for(j=zuo;j<you;j++){
            if(a[j].TagInThisPrecent>key){
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
		String q="SELECT Imsi,count(distinct URI) as TotalCount " +
				"FROM new2_GN_Filtered_4 group by Imsi order by TotalCount";
		
		String q1="SELECT URI,count(distinct Imsi) as TotalCount " +
				"FROM new2_GN_Filtered_4 group by URI order by TotalCount";
		
		String q2="SELECT GroupNum,COUNT(distinct Imsi) as ImsiCount,COUNT(distinct URI) as URICount " +
				"FROM new2_GN_Filtered_4_Grouped_For_Character group by GroupNum order by GroupNum";
		
//		HostTag.outPutGraphToTxt(conn, q1, "URI-ImsiTotalCount.txt", 
//				new String[] {"URI", "TotalCount"});
//		HostTag.addLineNum("URI-ImsiTotalCount_dis.txt");
//		HostTag.addLineNum("Imsi-URITotalCount_dis.txt");
//		HostTag.addLineNum("URI-ImsiTotalCount.txt");
//		HostTag.addLineNum("Imsi-URITotalCount.txt");
//		app.mergeList();
//		app.viewTable(conn);
		
		HostTag.disConnection(conn);
	}
}
class GroupInfo
{
	String Tag="";
	double TagInThisPrecent=0;
	GroupInfo(String Tag,double TagInThisPrecent)
	{
		this.Tag=Tag;
		this.TagInThisPrecent=TagInThisPrecent;
		
	}
}
