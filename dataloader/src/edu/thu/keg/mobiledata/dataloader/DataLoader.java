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

import edu.thu.keg.mobiledata.dataloader.singlefileloader.SingleFileLoader;
import edu.thu.keg.mobiledata.dataloader.singlefileloader.TrafficIntermediateFileReader;

/**
 * @author mayrock
 *
 */
public class DataLoader {

	public static void loadGNData(String dir){
		File[] files = new File(dir).listFiles();
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
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Tables inserted: " + tableC + "; Error: " + tableErr);
	}

	public static void loadTrafficIpData(File dir){
		File[] files = dir.listFiles();
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
			TrafficIntermediateFileReader loader = new TrafficIntermediateFileReader();
			loader.setFile(f);

			int ret = 0;
			ArrayList<String> insert = loader.getInsertSQL();
			System.out.println("Total count:" + insert.size() + 
					". Writing DB... id: " + fileId);
			
			int i = 0;
			for (String sql : insert) {
				try {
					stmt.addBatch(sql);
					++i;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				if (i == 10000) {
					try {
						ret += stmt.executeBatch().length;
						stmt.clearBatch();
						System.out.println("Insert " + ret+"complete!");
						i = 0;
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println(sql);
						
						continue;
					}
				}
			}
			try {
				ret += stmt.executeBatch().length;
				stmt.clearBatch();
				System.out.println("Insert " + ret+" complete!");
			} catch (Exception ex) {
				ex.printStackTrace();
				continue;
			}
			if (ret != 0 & insert != null)
				System.out.println("Lines inserted: " + ret + "; Error: " + (insert.size() - ret));
			System.out.println();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Tables inserted: " + tableC + "; Error: " + tableErr);
	}

	/**
	 * @param args
	 */
	public static void main2(String[] args) {
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
				loadGNData(subF.getAbsolutePath());
			}
		}
	}
	public static void main(String[] args) {
		String addr = args[0];
		File[] files = new File(addr).listFiles(new FileFilter(){

			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
			
		});
		for (File f : files) {
			loadTrafficIpData(f);
		}
	}

}
