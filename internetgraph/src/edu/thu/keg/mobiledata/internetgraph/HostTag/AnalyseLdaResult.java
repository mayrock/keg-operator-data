package edu.thu.keg.mobiledata.internetgraph.HostTag;

import java.io.IOException;
import java.io.LineNumberReader;
import java.sql.Connection;

public class AnalyseLdaResult {

	int column=0,line=0;
	double Lda_Matrix[][];
	String []Lable_Vec;
	public AnalyseLdaResult(int column, int line)
	{
		this.column=column;
		this.line=line;
		Lda_Matrix=new double[line][column];
		Lable_Vec= new String[line];
		LineNumberReader l_r=HostTag.getLNR("model-final.theta");
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
			}
			
			String query="select ,TotalCount"+
					" from dbo.URI_New order by TotalCount desc,UserCount";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void main(String arg[])
	{
		
		Connection conn=HostTag.getConnection();
//		app.mergeList();
//		app.viewTable(conn);
		
		HostTag.disConnection(conn);
	}
}
