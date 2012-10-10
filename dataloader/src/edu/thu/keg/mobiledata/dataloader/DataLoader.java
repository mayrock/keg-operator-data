/**
 * 
 */
package edu.thu.keg.mobiledata.dataloader;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * @author mayrock
 *
 */
public class DataLoader {

	public static void loadData(String dir){
		File[] files = new File(dir).listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File arg0, String arg1) {
				return !arg1.contains("finished");
			}
			
		});
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DriverManager.getConnection
					("jdbc:sqlserver://localhost:1433;databaseName=ZhuData;integratedSecurity=true;");
			stmt = conn.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int tableErr = 0, lineErr= 0, tableC = 0, lineC = 0;
		int fileId = 0;
		for (File f : files) {
			lineErr = 0;
			System.out.println("Loading file " + f.getAbsolutePath() + "... id: " + fileId++);
			SingleFileLoader loader = new SingleFileLoader(f.getAbsolutePath(), 1, fileId);
//			String create = loader.getCreateSQL();
//			try {
//				stmt.execute(create);
//				conn.commit();
//				System.out.println("Create succeed!");
//				tableC++;
//			} catch (SQLException e) {
//				tableErr ++;
//				System.out.println(e.getMessage());
//				System.out.println(create);
//			}
//			System.out.println();
			int[] ret = null;
			ArrayList<String> insert = loader.getInsertSQL();
			try {
			for (String sql : insert) {
				stmt.addBatch(sql);
			}
			ret = stmt.executeBatch();
			} catch (SQLException e) {
			}
			System.out.println("Insert complete!");
			if (ret != null & insert != null)
				System.out.println("Lines inserted: " + ret.length + "; Error: " + (insert.size() - ret.length));
			System.out.println();
//			String bulk = loader.getBulkLoad();
//			try {
//				bw.write(bulk);
//			} catch (IOException e) {
//				System.out.println(e.getMessage());
//				System.out.println(bulk);
//			}
			
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Tables inserted: " + tableC + "; Error: " + tableErr);
//		try {
//			bw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String addr = args[0];
		File[] files = new File(addr).listFiles(new FileFilter(){

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
			
		});
		for (File f : files) {
			File[] subFs = f.listFiles(new FileFilter(){

				@Override
				public boolean accept(File arg0) {
					return arg0.isDirectory() && arg0.getName().contains("WAP");
				}
				
			});
			for (File subF : subFs) {
				loadData(subF.getAbsolutePath());
			}
		}
		
	}

}
