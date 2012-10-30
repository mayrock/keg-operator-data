package edu.thu.keg.mobiledata.dataloader.singlefileloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TrafficIntermediateFileReader extends AbstractSingleFileReader {
	File file;
	ArrayList<String> insertSQL = null;
	public void setFile(File file) {
		this.file = file;
	}
	String[] arrName = {"IMSI"
			  ,"ConnectTime"
		      ,"Lac"
		      ,"Ci"
		      ,"TotalTraffic"
		      ,"TotalCount"
		      ,"TopPort"
		      ,"PortCount"};
	String[] arrType = {"bigint"
			  ,"smalldatetime"
		      ,"int"
		      ,"int"
		      ,"float"
		      ,"int"
		      ,"int"
		      ,"int"};
	
	String insertPrefix;
	String sep = "\t";
	String tableName = "TrafficIP";
	public ArrayList<String> getInsertSQL() {
		if (insertSQL == null)
			readFile();
		return insertSQL;
	}
	private void readFile() {
		insertSQL = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			FileInputStream is = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strLine = null;
			while ((strLine = reader.readLine()) != null)   {
				try{
					String sql = getInsertSQL(strLine);
					if (sql != null) {
						insertSQL.add(sql);
					}
				}
				catch (Exception ex){
					ex.printStackTrace();
					System.out.println("Error: " + strLine);
					continue;
				}
			}
			is.close();
			reader.close();
		} catch (Exception ex) {
			
		}
	}
	private String getInsertSQL(String line) {
		String[] fields = line.split(sep);
		if (fields.length < arrType.length)
			return null;
		StringBuffer sb = new StringBuffer(getInsertPrefix() + " VALUES (");
		for (int i = 0; i < fields.length - 1; ++i) {
			
			String value = fields[i];
			if (arrName[i] == "ConnectTime") {
				value = value + ":00";
			}
			if (!isNumericType(arrType[i])) {
				value = "'" + value.replaceAll("\'", "\'\'") + "'";
			} 
			
			value = value.replace(",","");
			sb.append(value + ", " );
		}
		String value = fields[fields.length - 1];
		if (arrName[fields.length - 1] == "ConnectTime") {
			value = value + ":00";
		}
		if (!isNumericType(arrType[fields.length - 1])) {
			value = "'" + value.replaceAll("\'", "\'\'") + "'";
		} 
		value = value.replace(",","");
		sb.append(value);
		sb.append(")" + System.lineSeparator());
		return sb.toString();
	}
	private String getTableName() {
		return tableName;
	}
	private String getInsertPrefix() {
		if (insertPrefix == null) {
			StringBuffer sb = new StringBuffer("INSERT INTO " + getTableName() + System.lineSeparator());
			sb.append(" (");
			for (int i = 0; i < arrName.length - 1; i++) {
				sb.append(arrName[i] + ",");
			}
			sb.append(arrName[arrName.length - 1]);
			sb.append(")" + System.lineSeparator());
			insertPrefix = sb.toString();
		}
		return insertPrefix;
	}
	public boolean isNumericType(String type) {
		return type.contains("int") || type.equals("float");
	}
}
