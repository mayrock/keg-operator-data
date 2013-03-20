package edu.thu.keg.mobiledata.dataloader.singlefileloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import edu.thu.keg.mobiledata.dataloader.DataLoader;

public class BeijingTrafficReader{

	File f;
	Connection conn;
	
	public BeijingTrafficReader(File f, Connection conn) {
		super();
		this.f = f;
		this.conn = conn;
	}

	public PreparedStatement getStmt() {
		BufferedReader reader = null;
		PreparedStatement stmt = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
			stmt = conn.prepareStatement("INSERT INTO BeijingTraffic " +
					"(Time, ULTraffic, DLTraffic) VALUES (?,?,?)");
			
			String strLine;
			int count = 0;
			String sep = null;
			//Read File Line By Line
			while ((strLine = reader.readLine()) != null)   {
				if (strLine.startsWith("Column")) {
					String[] arr = strLine.split("[: ]");
					sep = arr[3];
					break;
				}
			}
			strLine = reader.readLine();
			strLine = reader.readLine();
			while ((strLine = reader.readLine()) != null)   {
				String[] arr = strLine.split(sep);
				String time = arr[1].substring(0, arr[1].length() - 5);
				double ul = 0;
				double dl = 0;
				try
				{ ul= Double.parseDouble(arr[6]);
				dl= Double.parseDouble(arr[7]);}
				catch (Exception ex) {
					continue;
				}
				stmt.setString(1, time);
				stmt.setDouble(2, ul);
				stmt.setDouble(3, dl);
				stmt.addBatch();
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return stmt;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection c = null;
		try {
			c = DriverManager.getConnection
					("jdbc:sqlserver://localhost:1433;databaseName=BeijingTraffic;integratedSecurity=true;");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String path = args[0];
		File log = new File(path + "log.txt");
		FileWriter writer = null;
		try {
			log.createNewFile();
			writer = new FileWriter(log);
		} catch (IOException e) {
			System.err.println(log.getAbsolutePath());
			e.printStackTrace();
		}
		File f = new File(path);
		File[] servers = f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File arg0) {
				return arg0.isDirectory();
			}
		});
//			Calendar date = Calendar.getInstance();
//			date.set(2012, 11, 1);
//			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			format.format(date.getTime());
		for (File server : servers) {
			File[] dates = server.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.length() == 10;
				}
			});
			for (File date : dates) {
				try {
					File wap = date.listFiles(new FilenameFilter() {
						
						@Override
						public boolean accept(File dir, String name) {
							return name.contains("TRAFFIC");
						}
					})[0];
					File[] fs = wap.listFiles();
					for (File fff : fs) {
						BeijingTrafficReader reader = new 
								BeijingTrafficReader(fff, c);
						PreparedStatement ps = reader.getStmt();
						ps.executeBatch();
						System.out.println(fff.getAbsolutePath());
					}
				}
				catch (Exception ex) {
					try {
						writer.write(date.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}
			}
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
