/**
 * 
 */
package edu.thu.keg.mobiledata.dataloader.singlefileloader;

import java.io.*;
import java.util.ArrayList;

/**
 * @author mayrock
 *
 */
public class SingleFileLoader extends AbstractSingleFileReader {
	
	private File file;
	private int fileId;
	private int type = 0;
	public String getCreateSQL() {
		if (createSQL == null)
			readFile();
		return createSQL;
	}

	public ArrayList<String> getInsertSQL() {
		if (insertSQL == null)
			readFile();
		return insertSQL;
	}
	
	
	
	public String getBulkLoad() {
		String sql = "BULK INSERT " + getTableName() + 
		" FROM '" + file.getAbsolutePath() + "'" +
		" WITH ( FIELDTERMINATOR ='" + sep + "', ROWTERMINATOR ='\r\n');";
		return sql;
		
	}
	
	public String getTableName() {
		return "GN";// + file.getName().substring(0, file.getName().lastIndexOf('.')).replace('-', '_');
	}

	private String createSQL = null;
	private ArrayList<String> insertSQL = null;
	
	private int count = 0;
	private String sep = "";
	private String[] typeArr;
	private String[] nameArr;
	
	public SingleFileLoader(String path, int type, int fileId) {
		this.file = new File(path);
		this.type = type;
		this.fileId = fileId;
	}
	
	private void readFile() {
		BufferedReader reader = null;
		insertSQL = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		sb.append("IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[");
		sb.append(getTableName());
		sb.append("]') AND type in (N'U')) DROP TABLE [dbo].[");
		sb.append(getTableName());
		sb.append("];" + System.lineSeparator());
		sb.append("CREATE TABLE " + getTableName() + " (" + System.lineSeparator());
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			String strLine;
			//Read File Line By Line
			while ((strLine = reader.readLine()) != null)   {
				if (strLine.startsWith("Column")) {
					String[] arr = strLine.split("[: ]");
					count = Integer.parseInt(arr[1]);
					sep = arr[3];
					break;
				}
			}

			strLine = reader.readLine();
			strLine = strLine.replace("string", "nvarchar(max)");
			strLine = strLine.replace("smallint", "int");
			typeArr = strLine.split(sep);
			
			
			strLine = reader.readLine();
			nameArr = strLine.split(sep);
			
			for (int i = 0; i < count; ++i) {
				sb.append(nameArr[i] + " " + typeArr[i] + "," + System.lineSeparator());
			}
			sb.append("FileID" + " " + "smallint" + System.lineSeparator());
			sb.append(" )");
			
			if (type == 1) {
				while ((strLine = reader.readLine()) != null)   {
					try{
					insertSQL.add(getInsertSQL(strLine));
					}
					catch (Exception ex){
						continue;
					}
				}
			}

			reader.close();
			
			createSQL = sb.toString();
		} catch (Exception e) {
			System.out.println("Error reading file:" + file.getName());
		}
	}
	
	private String getInsertSQL(String line) {
		StringBuffer sb = new StringBuffer("INSERT INTO " + getTableName() + System.lineSeparator());
		
		StringBuffer sb1 = new StringBuffer(" (");
		StringBuffer sb2 = new StringBuffer(" VALUES (");
		String[] arr = line.split(sep);
		for (int i = 0; i < arr.length - 1; ++i) {
			
			String value;
			if (!isNumericType(typeArr[i])) {
				value = "'" + arr[i].replaceAll("\'", "\'\'") + "'";
			} else {
				value = arr[i];
			}
			if (!(isNumericType(typeArr[i]) && arr[i].equals(""))) {
				sb1.append(nameArr[i] + ", " );
				sb2.append(value + ", " );
			}
		}
		String value;
		if (!isNumericType(typeArr[arr.length - 1])) {
			value = "'" + arr[arr.length - 1].replaceAll("\'", "\'\'") + "'";
		} else {
			value = arr[arr.length - 1];
		}
		if (!(isNumericType(typeArr[arr.length - 1]) && arr[arr.length - 1].equals(""))) {
			sb1.append(nameArr[arr.length - 1]);
			sb2.append(value);
		}
		sb1.append(")" + System.lineSeparator());
		sb2.append(")" + System.lineSeparator());
		sb.append(sb1).append(sb2);
		return sb.toString();
	}
	
	public boolean isNumericType(String type) {
		return type.contains("int") || type.equals("float");
	}

}
